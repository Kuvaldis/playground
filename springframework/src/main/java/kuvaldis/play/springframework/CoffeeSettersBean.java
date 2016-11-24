package kuvaldis.play.springframework;

public class CoffeeSettersBean {

    private String sort;

    private String producedBy;

    public CoffeeSettersBean() {
    }

    public CoffeeSettersBean(String sort, String producedBy) {
        this.sort = sort;
        this.producedBy = producedBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getProducedBy() {
        return producedBy;
    }

    public void setProducedBy(String producedBy) {
        this.producedBy = producedBy;
    }
}
