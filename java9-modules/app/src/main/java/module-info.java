module kuvaldis.play.java9.app {
    requires kuvaldis.play.java9.theater;

    // required when ServiceLoader tries to load Theater instances
    uses kuvaldis.play.java9.theater.Theater;
}