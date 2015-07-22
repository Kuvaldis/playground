package kuvaldis.play.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final Integer PORT_NUMBER = 8080;

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        System.out.println("Start listening to port: " + PORT_NUMBER);
        final ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
        while (true) {
            final Socket clientSocket = serverSocket.accept();
            System.out.println("Got a request");
            final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            final BufferedWriter out = new BufferedWriter(new PrintWriter(clientSocket.getOutputStream(), true));
            executorService.submit(() -> {
                try {
                    new RequestProcessor(in, out).process();
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
