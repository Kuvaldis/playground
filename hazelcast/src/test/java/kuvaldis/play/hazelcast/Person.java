package kuvaldis.play.hazelcast;

import java.io.Serializable;

public class Person implements Serializable {

    private Long id;

    private String name;

    private Integer age;

    private Boolean active;

    public Person(Long id, String name, Integer age, Boolean active) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
