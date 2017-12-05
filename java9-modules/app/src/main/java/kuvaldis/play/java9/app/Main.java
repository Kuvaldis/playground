package kuvaldis.play.java9.app;

import kuvaldis.play.java9.theater.Theater;

import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) {
        final ServiceLoader<Theater> loader = ServiceLoader.load(Theater.class);
        loader.stream()
                .map(ServiceLoader.Provider::get)
                .forEach(Theater::playSpectacle);
    }
}
