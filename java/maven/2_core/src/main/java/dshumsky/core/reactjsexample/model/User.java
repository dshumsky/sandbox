package dshumsky.core.reactjsexample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import dshumsky.core.reactjsexample.model.TripResource.ViewTrips;

@Entity
@Table(name = User.Table.NAME)
public class User {

    private static final String SEQUENCE_GENERATOR = "SEQ_T01_USER";

    //@formatter:off
    public interface Table {
        String NAME          = "T01_USER",
               C01_USER_ID   = "C01_USER_ID",
               C01_USERNAME  = "C01_USERNAME",
               C01_PASSWORD  = "C01_PASSWORD",
               C01_FIRSTNAME = "C01_FIRSTNAME",
               C01_LASTNAME  = "C01_LASTNAME",
               C01_EMAIL     = "C01_EMAIL",
               C01_ENABLED   = "C01_ENABLED";
    }
    public interface Props {
        String id        = "id",
               username  = "username",
               password  = "password",
               firstname = "firstname",
               lastname  = "lastname",
               email     = "email",
               enabled   = "enabled";
    }
    //@formatter:on

    public interface ViewUser extends ViewUsers{}
    public interface ViewUsers {}

    @Column(name = Table.C01_USER_ID)
    @Id
    @SequenceGenerator(name = SEQUENCE_GENERATOR, sequenceName = "SEQ_T01_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR)
    private Long id;

    @Column(name = Table.C01_USERNAME, unique = true)
    private String username;

    @Column(name = Table.C01_PASSWORD)
    private String password;

    @Column(name = Table.C01_FIRSTNAME)
    private String firstname;

    @Column(name = Table.C01_LASTNAME)
    private String lastname;

    @Column(name = Table.C01_EMAIL)
    private String email;

    @Column(name = Table.C01_ENABLED)
    private boolean enabled;

    @JsonView({ ViewUsers.class, AuthorizedUser.View.class })
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView({ ViewTrips.class, ViewUsers.class, AuthorizedUser.View.class })
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView({ ViewUsers.class, AuthorizedUser.View.class })
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @JsonView({ ViewUsers.class, AuthorizedUser.View.class })
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @JsonView({ViewUsers.class, AuthorizedUser.View.class})
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonView({ViewUsers.class, AuthorizedUser.View.class})
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}