package kuvaldis.play.java;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortFile {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String inputFile = args[0];
        final String outputFile = args[1];
        final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        final BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        final List<String> strings = new ArrayList<>();
        System.out.println("Start loading");
        String line;
        while ((line = reader.readLine()) != null) {
            strings.add(line);
        }
        System.out.println("Start sort");
        Collections.sort(strings);
        for (String string : strings) {
            writer.write(string);
            writer.newLine();
        }
        System.out.println("Done");
        writer.flush();
        writer.close();
    }
}
