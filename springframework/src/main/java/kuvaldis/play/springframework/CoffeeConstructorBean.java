package kuvaldis.play.springframework;

public class CoffeeConstructorBean {

    private final String sort;

    private final String producedBy;

    public CoffeeConstructorBean(String sort, String producedBy) {
        this.sort = sort;
        this.producedBy = producedBy;
    }

    public String getSort() {
        return sort;
    }

    public String getProducedBy() {
        return producedBy;
    }
}
