package kuvaldis.play.java;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class WatchServiceExample {

    public static void main(String[] args) throws IOException {
        new WatchServiceExample().watch();
    }

    public void watch() throws IOException {

        final WatchService watcher = FileSystems.getDefault().newWatchService();
        final Path dir = Paths.get("c:/SO");
        dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);

        while (true) {
            WatchKey key;
            try {
                // wait for a key to be available
                key = watcher.take();
            } catch (InterruptedException ex) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                // get event type
                WatchEvent.Kind<?> kind = event.kind();

                // get file name
                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();

                System.out.println(kind.name() + ": " + fileName);

                if (kind == ENTRY_CREATE) {
                    System.out.println("Entry create");
                } else if (kind == ENTRY_MODIFY) {
                    System.out.println(" Entry modify");
                }
            }

            // IMPORTANT: The key must be reset after processed
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}
