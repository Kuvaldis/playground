package kuvaldis.play.springframework.orm;

import kuvaldis.play.springframework.dataaccess.object.Actor;

import java.util.Collection;

public interface ActorDao {

    Collection<Actor> findByFirstName(final String firstName);
}
