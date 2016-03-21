package kuvaldis.play.java;

public class Call {

    public void call() {
        func(1);        // do'h
        func((byte) 1); // do'h
        func();
//        func(null); compile error
    }

    private void func(final Number n) {

    }

    private void func(final Byte b) {

    }

    private void func(final Object o) {

    }

    private void func(final byte... bs) {

    }
}
