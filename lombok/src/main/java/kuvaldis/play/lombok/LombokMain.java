package kuvaldis.play.lombok;

import lombok.val;

public class LombokMain {

    public String method1() {
        val a = "123";
//        a = "234";
        return a;
    }
}
