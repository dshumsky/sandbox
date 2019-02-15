package dshumsky.core.reactjsexample.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.inject.persist.Transactional;
import dshumsky.core.reactjsexample.api.UserDao;
import dshumsky.core.reactjsexample.model.Permission;
import dshumsky.core.reactjsexample.model.Permission.Id;
import dshumsky.core.reactjsexample.model.Permission.Table;
import dshumsky.core.reactjsexample.model.PermissionType;
import dshumsky.core.reactjsexample.model.User;
import dshumsky.core.reactjsexample.model.User.Props;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
@Transactional
class UserDaoImpl extends AbstractDao implements UserDao {

    @Inject
    public UserDaoImpl(Provider<EntityManager> emProvider) {
        super(emProvider);
    }

    @Override
    public User findByUsername(String username) {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq(Props.username, username));
        return (User) criteria.uniqueResult();
    }

    @Override
    public User findById(long id) {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq(Props.id, id));
        return (User) criteria.uniqueResult();
    }

    @Override
    public long createOrUpdate(User user, Set<Long> permissions) {
        long userId;
        if (user.getId()==null) {
            userId = (long) getSession().save(user);
        } else {
            getSession().update(user);
            userId = user.getId();
        }
        updatePermissions(userId, permissions);
        return userId;
    }

    @Override
    public void updatePermissions(long userId, Set<Long> permissions) {
        Session session = getSession();
        SQLQuery sqlQuery = session.createSQLQuery("DELETE FROM " + Table.NAME
                + "\n where " + Table.C03_USER_ID + "=:userId");
        sqlQuery.setParameter("userId", userId);
        sqlQuery.executeUpdate();
        getSession().flush();

        for (Long permissionTypeId : permissions) {
            Permission permission = new Permission(new Id(userId, permissionTypeId));
            session.save(permission);
        }
    }

    @Override
    public Set<PermissionType> loadPermissions(long userId) {
        Criteria criteria = getSession().createCriteria(Permission.class);
        criteria.add(Restrictions.eq(Permission.PROP_userId, userId));
        criteria.addOrder(Order.asc(Permission.PROP_permissionTypeId));
        //noinspection unchecked
        List<Permission> list = criteria.list();
        HashSet<PermissionType> result = new HashSet<>(list.size());
        for (Permission permission : list) {
            result.add(PermissionType.getById(permission.getId().getPermissionTypeId()));
        }
        return result;
    }


    public List<User> findAll() {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.addOrder(Order.asc(Props.id));
        //noinspection unchecked
        return criteria.list();
    }
}
