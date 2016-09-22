package kuvaldis.play.java;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NIO {

    @Test
    public void testFileChannel() throws Exception {
        final RandomAccessFile file = new RandomAccessFile("nio-data.txt", "rw");
        final FileChannel fileChannel = file.getChannel();
        final ByteBuffer buffer = ByteBuffer.allocate(48);

        // write to file
        buffer.put("data".getBytes());
        buffer.flip();
        final int bytesWritten = fileChannel.write(buffer);
        assertEquals(4, bytesWritten);

        // read from file
        readDataFromChannel(fileChannel, buffer, "data");

        // channel transfer
        final RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        final FileChannel toChannel = toFile.getChannel();
        toChannel.transferFrom(fileChannel, 0, 4);
        readDataFromChannel(toChannel, buffer, "data");
        toFile.close();

        file.close();

        Files.delete(Paths.get("nio-data.txt"));
        Files.delete(Paths.get("toFile.txt"));
    }

    private void readDataFromChannel(final FileChannel fileChannel, final ByteBuffer buffer, final String data) throws IOException {
        fileChannel.position(0);
        buffer.clear();
        int bytesRead = fileChannel.read(buffer);
        assertEquals(data.getBytes().length, bytesRead);
        // makes buffer ready to read. switching from write to read mode
        buffer.flip();
        final char[] chars = new char[bytesRead];
        for (int i = 0; buffer.hasRemaining(); i++) {
            chars[i] = (char) buffer.get();
        }
        assertEquals(data, new String(chars).intern());
        fileChannel.position(0);
        buffer.clear();
    }

    @Test
    public void testGatherScatter() throws Exception {
        final ByteBuffer header = ByteBuffer.allocate(16);
        final ByteBuffer body = ByteBuffer.allocate(128);

        // should be exactly 16 bytes, or else scattering read will not work
        header.put("header data!!!!!".getBytes());
        body.put("body data".getBytes());
        header.flip();
        body.flip();

        final RandomAccessFile file = new RandomAccessFile("gather-file.txt", "rw");
        final FileChannel channel = file.getChannel();

        final long write = channel.write(new ByteBuffer[]{header, body});
        assertEquals(25, write);

        header.clear();
        body.clear();
        channel.position(0);

        final long read = channel.read(new ByteBuffer[]{header, body});
        assertEquals(25, read);
        header.flip();
        body.flip();

        final char[] chars = new char[((int) read)];
        int i;
        for (i = 0; header.hasRemaining(); i++) {
            chars[i] = (char) header.get();
        }
        for (; body.hasRemaining(); i++) {
            chars[i] = (char) body.get();
        }

        assertEquals("header data!!!!!body data", new String(chars).intern());

        channel.close();
        Files.delete(Paths.get("gather-file.txt"));
    }

    @Test
    public void testSelector() throws Exception {
        final ServerThread serverThread = new ServerThread(2);
        serverThread.start();
        serverThread.waitServerStart();

        final ClientThread clientThread1 = new ClientThread("Hi", "How are you", "That's it");
        final ClientThread clientThread2 = new ClientThread("Hello", "SHMEBULOCK!!!", "That's it");
        clientThread1.start();
        clientThread2.start();
        clientThread1.waitClientSendMessages();
        clientThread2.waitClientSendMessages();

        final List<String> receivedMessages = serverThread.getReceivedMessages();
        System.out.println(receivedMessages);
        assertEquals(6, receivedMessages.size());
        assertEquals("That's it", receivedMessages.get(5));
        assertTrue(receivedMessages.contains("Hi"));
        assertTrue(receivedMessages.contains("How are you"));
        assertTrue(receivedMessages.contains("That's it"));
        assertTrue(receivedMessages.contains("Hello"));
        assertTrue(receivedMessages.contains("SHMEBULOCK!!!"));
    }

    private static class ServerThread extends Thread {

        private final int serverNumberOfClients;
        private final CountDownLatch serverStartedLatch = new CountDownLatch(1);
        private final CountDownLatch serverStoppedLatch = new CountDownLatch(1);

        private final List<String> receivedMessages = new ArrayList<>();

        private ServerThread(final int serverNumberOfClients) {
            this.serverNumberOfClients = serverNumberOfClients;
        }

        @Override
        public void run() {
            try {
                final ServerSocketChannel serverSocket = ServerSocketChannel.open();
                final InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5454);
                serverSocket.bind(hostAddress);
                serverSocket.configureBlocking(false);
                final int serverOps = serverSocket.validOps();
                assertEquals(SelectionKey.OP_ACCEPT, serverOps);

                final Selector selector = Selector.open();
                final SelectionKey selectionKey = serverSocket.register(selector, serverOps, null);

                int clientServedCounter = 0;

                // start serve
                serverStartedLatch.countDown();
                while (clientServedCounter < serverNumberOfClients) {
                    final int noOfKeys = selector.select();
                    // basically, how many clients are connected now
                    System.out.println("Number of selected keys: " + noOfKeys);

                    final Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    final Iterator<SelectionKey> iterator = selectedKeys.iterator();

                    while (iterator.hasNext()) {
                        final SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            // accept non-blocking connection
                            final SocketChannel client = serverSocket.accept();
                            client.configureBlocking(false);

                            // add new connection to the selector
                            client.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            final SocketChannel client = (SocketChannel) key.channel();
                            final ByteBuffer buffer = ByteBuffer.allocate(256);
                            client.read(buffer);
                            final String[] outputs = new String(buffer.array()).trim().split("" + (char) 0);
                            for (String output : outputs) {
                                receivedMessages.add(output);
                                System.out.println("Message read from client: " + output);
                                if (output.equals("That's it")) {
                                    client.close();
                                    clientServedCounter++;
                                    System.out.println("Client messages are complete; close.");
                                }
                            }
                        }
                        iterator.remove();
                    }
                }
                serverSocket.close();
                serverStoppedLatch.countDown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        void waitServerStart() throws InterruptedException {
            serverStartedLatch.await();
        }

        List<String> getReceivedMessages() throws InterruptedException {
            serverStoppedLatch.await();
            return receivedMessages;
        }
    }

    private static class ClientThread extends Thread {

        private final CountDownLatch latch = new CountDownLatch(1);

        private final List<String> messages;

        private ClientThread(final String... messages) {
            this.messages = Arrays.asList(messages);
        }


        @Override
        public void run() {
            try {
                final InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5454);
                final SocketChannel client = SocketChannel.open(hostAddress);
                for (String message : messages) {
                    final byte[] bytes = message.getBytes();
                    final ByteBuffer buffer = ByteBuffer.allocate(bytes.length + 1);
                    buffer.put(bytes);
                    buffer.put((byte) 0);
                    buffer.flip();
                    client.write(buffer);
                    buffer.clear();
                    // just to add some chaos
                    Thread.sleep(1);
                }
                latch.countDown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        void waitClientSendMessages() throws InterruptedException {
            latch.await();
        }
    }

    @Test
    public void testPipe() throws Exception {
        final Pipe pipe = Pipe.open();
        final PipeSender sender = new PipeSender("data", pipe);
        final PipeReceiver receiver = new PipeReceiver(pipe);
        sender.start();
        receiver.start();
        final String readData = receiver.getReadData();
        assertEquals("data", readData);
    }

    private static class PipeSender extends Thread {

        private String data;
        private final Pipe pipe;

        private PipeSender(final String data, final Pipe pipe) {
            this.data = data;
            this.pipe = pipe;
        }

        @Override
        public void run() {
            try {
                final Pipe.SinkChannel sinkChannel = pipe.sink();
                final ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
                sinkChannel.write(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class PipeReceiver extends Thread {

        private final ReentrantLock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();

        private final Pipe pipe;

        private volatile String readData;

        private PipeReceiver(final Pipe pipe) {
            this.pipe = pipe;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                final Pipe.SourceChannel sourceChannel = pipe.source();
                final ByteBuffer buffer = ByteBuffer.allocate(16);
                final int readBytes = sourceChannel.read(buffer);
                buffer.flip();
                final char[] read = new char[readBytes];
                for (int i = 0; buffer.hasRemaining(); i++) {
                    read[i] = (char) buffer.get();
                }
                this.readData = new String(read).intern();
                condition.signalAll();
                lock.unlock();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        String getReadData() throws InterruptedException {
            lock.lock();
            try {
                while(readData == null) {
                    condition.await();
                }
            } finally {
                lock.unlock();
            }
            return readData;
        }
    }
}
