package dshumsky.core.reactjsexample.api;

import java.util.List;
import java.util.Set;

import dshumsky.core.reactjsexample.model.PermissionType;
import dshumsky.core.reactjsexample.model.User;

public interface UserDao {
    User findByUsername(String username);

    List<User> findAll();

    Set<PermissionType> loadPermissions(long userId);

    User findById(long id);

    long createOrUpdate(User user, Set<Long> permissions);

    void updatePermissions(long userId, Set<Long> permissions);
}
