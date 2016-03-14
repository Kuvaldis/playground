package kuvaldis.play.dropwizard.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Class representing one user view entry. Does not contain the actual user, only who and when viewed it
 */
public class UserView {

    /**
     * Viewer id of the user
     */
    @NotNull
    @JsonProperty
    private Long userViewerId;

    /**
     * View date and time of the user
     */
    @NotNull
    @JsonProperty
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss.SSS")
    private Date viewDateTime;

    public UserView() {
    }

    public UserView(final Long userViewerId, final Date viewDateTime) {
        this.userViewerId = userViewerId;
        this.viewDateTime = viewDateTime;
    }

    public Long getUserViewerId() {
        return userViewerId;
    }

    public Date getViewDateTime() {
        return viewDateTime;
    }

    public void setUserViewerId(final Long userViewerId) {
        this.userViewerId = userViewerId;
    }

    public void setViewDateTime(final Date viewDateTime) {
        this.viewDateTime = viewDateTime;
    }
}
