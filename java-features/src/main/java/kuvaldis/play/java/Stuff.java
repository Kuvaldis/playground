package kuvaldis.play.java;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class Stuff {
    public static void main(String[] args) throws Exception {
        final String s = new String(new byte[]{-1}, "UTF-8");
        System.out.println(Arrays.toString(s.getBytes("UTF-8")));
        System.out.println(Math.pow(2, 1 / 3f));

        LocalDate start = LocalDate.of(2016, 1, 1);
        LocalDate end = start.plusYears(1);
        Period everyMonth = Period.ofMonths(1);
        for (;start.isBefore(end); start = start.plus(everyMonth)) {
            System.out.println(Period.between(start, start.plus(everyMonth)).getDays());
            System.out.println(ChronoUnit.DAYS.between(start, start.plus(everyMonth)));
        }
    }
}
