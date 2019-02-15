package dshumsky.web.reactjsexample.security;

import java.util.Arrays;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import dshumsky.core.reactjsexample.model.PermissionType;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class PermissionChecker {
    private final Authentication authentication;

    public PermissionChecker(Authentication authentication) {
        this.authentication = authentication;
    }

    public void checkOneOf(PermissionType... permissionTypes) throws AccessDeniedException{
        for (PermissionType permissionType : permissionTypes) {
            if (has(permissionType))
                return;
        }
        throw new AccessDeniedException(String.format("User has no permissions=%s", Arrays.toString(permissionTypes)));
    }

    public void check(PermissionType permissionType) throws AccessDeniedException{
        if (!has(permissionType))
            throw new AccessDeniedException(String.format("User has no permission=%s", permissionType));
    }

    public long getUserId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUserId();
    }

    public boolean has(PermissionType permissionType) {
        if (UserDetailsImpl.class.isInstance(authentication.getPrincipal())) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getPermissions().contains(permissionType);
        } else {
            return false;
        }
    }
}
