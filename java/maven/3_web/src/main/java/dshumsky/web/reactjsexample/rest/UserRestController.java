package dshumsky.web.reactjsexample.rest;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import dshumsky.core.reactjsexample.api.UserDao;
import dshumsky.core.reactjsexample.model.AuthorizedUser;
import dshumsky.core.reactjsexample.model.PermissionType;
import dshumsky.core.reactjsexample.model.User;
import dshumsky.web.reactjsexample.security.PermissionChecker;
import dshumsky.web.reactjsexample.security.SecurityTokenUtil;

import static dshumsky.core.reactjsexample.model.PermissionType.CRUD_USERS;

@RestController
public class UserRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final SecurityTokenUtil securityTokenUtil;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserRestController(@Autowired SecurityTokenUtil securityTokenUtil,
                              @Autowired UserDao userDao,
                              @Autowired PasswordEncoder passwordEncoder) {
        this.securityTokenUtil = securityTokenUtil;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    @JsonView(AuthorizedUser.View.class)
    public AuthorizedUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = securityTokenUtil.getUsernameFromToken(token);
        User user = userDao.findByUsername(username);
        Set<PermissionType> permissionTypes = userDao.loadPermissions(user.getId());
        return new AuthorizedUser(user, permissionTypes);
    }

    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    @JsonView(AuthorizedUser.View.class)
    public AuthorizedUser getUser(@PathVariable("id") long id) {
        getPermissionChecker().check(CRUD_USERS);
        User user = userDao.findById(id);
        Set<PermissionType> permissionTypes = userDao.loadPermissions(user.getId());
        return new AuthorizedUser(user, permissionTypes);
    }

    @RequestMapping(value = "user/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public long updateUser(@PathVariable("id") long id, @RequestBody AuthorizedUser authorizedUser) {
        getPermissionChecker().check(CRUD_USERS);
        User user = authorizedUser.getUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.createOrUpdate(user, authorizedUser.getPermissions());
        return id;
    }

    @RequestMapping(value = "users", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public long createUser(@RequestBody AuthorizedUser authorizedUser) {
        PermissionChecker permissionChecker = getPermissionChecker();
        User user = authorizedUser.getUser();
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Long> permissions;
        if (permissionChecker.has(CRUD_USERS)) {
            permissions = authorizedUser.getPermissions();
        } else {
            user.setEnabled(true);
            permissions = Collections.singleton(PermissionType.CRUD_OWN_TRIPS.getId());
        }
        return userDao.createOrUpdate(user, permissions);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "users")
    @JsonView(User.ViewUsers.class)
    public List<User> getUsers() {
        getPermissionChecker().check(CRUD_USERS);
        return userDao.findAll();
    }

    private PermissionChecker getPermissionChecker() {
        return new PermissionChecker(SecurityContextHolder.getContext().getAuthentication());
    }
}
