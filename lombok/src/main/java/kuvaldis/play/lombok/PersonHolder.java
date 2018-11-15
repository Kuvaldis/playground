package kuvaldis.play.lombok;

import lombok.Data;
import lombok.NonNull;

@Data
public class PersonHolder {

    private final Person person;

    public PersonHolder(final @NonNull Person person) {
        this.person = person;
    }
}
