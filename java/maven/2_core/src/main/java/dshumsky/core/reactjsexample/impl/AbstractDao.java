package dshumsky.core.reactjsexample.impl;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.hibernate.Session;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public abstract class AbstractDao {

    private Provider<EntityManager> emProvider;

    @Inject
    public AbstractDao(Provider<EntityManager> emProvider) {
        this.emProvider = emProvider;
    }

    protected EntityManager getEntityManager() {
        return emProvider.get();
    }

    protected Session getSession() {
        return getEntityManager().unwrap(Session.class);
    }

    protected Provider<Session> getSessionProvider() {
        return new Provider<Session>(){
            @Override
            public Session get() {
                return getEntityManager().unwrap(Session.class);
            }
        };
    }
}
