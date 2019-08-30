package dshumsky.web.reactjsexample.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dshumsky.core.reactjsexample.api.UserDao;
import dshumsky.core.reactjsexample.model.PermissionType;
import dshumsky.core.reactjsexample.model.User;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            Set<PermissionType> permissions = userDao.loadPermissions(user.getId());
            return new UserDetailsImpl(user, permissions);
        }
    }
}
