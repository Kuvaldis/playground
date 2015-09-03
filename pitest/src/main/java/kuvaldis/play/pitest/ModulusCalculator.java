package kuvaldis.play.pitest;

public class ModulusCalculator {

    private Integer modulus;

    public ModulusCalculator() {
    }

    public ModulusCalculator(Integer modulus) {
        this.modulus = modulus;
    }

    public Long sum(final Integer first, final Integer second) {
        final Long sum =  first.longValue() + second.longValue();
        if (modulus != null) {
            return sum % modulus;
        } else {
            return sum % 10;
        }
    }

}
