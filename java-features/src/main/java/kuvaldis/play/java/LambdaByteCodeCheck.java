package kuvaldis.play.java;

public class LambdaByteCodeCheck {
    public static void main(String[] args) {
        final Runnable r1 = () -> System.out.println("Hello1");
        final Runnable r2 = () -> System.out.println("Hello2");
        r1.run();
        r2.run();
    }
}
