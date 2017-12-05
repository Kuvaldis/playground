import kuvaldis.play.java9.theater.impl.Friends;

module kuvaldis.play.java9.theater {
    requires kuvaldis.play.java9.spectacle;

    exports kuvaldis.play.java9.theater;

    // when ServiceLoader requiring Theater class is used, then Friends instance will be returned as one of the instances.
    provides kuvaldis.play.java9.theater.Theater
            with Friends;
}