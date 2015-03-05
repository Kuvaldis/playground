package kuvaldis.play.plexus;

public class ParmesanCheese implements Cheese {
    @Override
    public void slice(int slices) {
        throw new UnsupportedOperationException("No can do");
    }

    @Override
    public String getAroma() {
        return "Strong";
    }
}
