package kuvaldis.play.picocontainer;

import java.util.List;
import java.util.Map;

public class Sea {

    private final Fish[] fishes;
    private final List<Cod> cods;
    private final Map<String, Shark> sharks;

    public Sea(Fish[] fishes, List<Cod> cods, Map<String, Shark> sharks) {
        this.fishes = fishes;
        this.cods = cods;
        this.sharks = sharks;
    }

    public Fish[] getFishes() {
        return fishes;
    }

    public List<Cod> getCods() {
        return cods;
    }

    public Map<String, Shark> getSharks() {
        return sharks;
    }
}
