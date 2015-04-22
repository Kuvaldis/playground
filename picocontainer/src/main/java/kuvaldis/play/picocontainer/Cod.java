package kuvaldis.play.picocontainer;

public class Cod implements Fish {
    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }
}
