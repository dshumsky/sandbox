package dshumsky.core.reactjsexample.model;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class AuthorizedUser {

    public interface View {}

    private User user;
    private Set<Long> permissions;

    public AuthorizedUser() {
    }

    public AuthorizedUser(User user, Set<PermissionType> permissionTypes) {
        this.user = user;
        this.permissions = permissionTypes.stream().map(PermissionType::getId).collect(Collectors.toSet());
    }

    @JsonView(View.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonView(View.class)
    public Set<Long> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Long> permissions) {
        this.permissions = permissions;
    }
}
