package kuvaldis.play.dropwizard.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Class representing user. Contains only user id
 */
public class User {

    /**
     * User id
     */
    @NotNull
    @JsonProperty
    private final Long userId;

    public User(final Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
