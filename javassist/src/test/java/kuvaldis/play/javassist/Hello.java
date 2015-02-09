package kuvaldis.play.javassist;

import java.io.IOException;

public class Hello {

    private String world = "Puppet";

    public void say() {
        System.out.println("Hello, " + world + "!");
    }

    public void sayWithParams(final String first, final String second) {
        System.out.println(first + second);
    }
    public void sayThrowsIOException() throws IOException {
        throw new IOException();
    }

    public void callPrintln(final String string) {
        System.out.println(string);
    }

    public String getWorld() {
        return world;
    }
}