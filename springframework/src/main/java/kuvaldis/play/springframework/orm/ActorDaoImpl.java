package kuvaldis.play.springframework.orm;

import kuvaldis.play.springframework.dataaccess.object.Actor;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.util.Collection;

public class ActorDaoImpl implements ActorDao {

    private SessionFactory sessionFactory;

    public ActorDaoImpl(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public Collection<Actor> findByFirstName(final String firstName) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Actor actor where actor.firstName = 'Vasiliy'")
//                .setParameter("firstName", firstName)
                .list();
    }
}
