module kuvaldis.play.java9.spectacle {
    // transitive makes kuvaldis.play.java9.actors module exported as well,
    // i.e. all modules require this one will also get actors as well.
    requires transitive kuvaldis.play.java9.actors;

    exports kuvaldis.play.java9.spectacle;
}