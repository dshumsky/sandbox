package dshumsky.web.reactjsexample.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dshumsky.core.reactjsexample.model.PermissionType;
import dshumsky.core.reactjsexample.model.User;

public class UserDetailsImpl implements UserDetails {

    private final Collection<SimpleGrantedAuthority> authorities;
    private final Set<PermissionType> permissions;
    private final long userId;
    private String username;
    private String password;
    private boolean enabled;

    public UserDetailsImpl(User user, Set<PermissionType> permissions) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.getEnabled();
        this.userId = user.getId();
        this.authorities = new ArrayList<>(permissions.size());
        this.permissions = permissions;
        for (PermissionType permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.name()));
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public long getUserId() {
        return userId;
    }

    public Set<PermissionType> getPermissions() {
        return permissions;
    }
}
