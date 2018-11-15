package kuvaldis.play.lombok;

public class Extensions {
    public static String toTitleCase(String in) {
        if (in.isEmpty()) return in;
        return "" + Character.toTitleCase(in.charAt(0)) +
                in.substring(1).toLowerCase();
    }
}
