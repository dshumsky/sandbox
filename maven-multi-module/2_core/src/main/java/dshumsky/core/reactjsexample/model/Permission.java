package dshumsky.core.reactjsexample.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T03_PERMISSION")
public class Permission {

    //@formatter:off
    public interface Table {
        String NAME                   = "T03_PERMISSION",
               C03_USER_ID            = "C03_USER_ID",
               C03_PERMISSION_TYPE_ID = "C03_PERMISSION_TYPE_ID";
    }
    public interface Props {
        String id               = "id",
               userId           = id + ".userId",
               permissionTypeId = id + ".destination";
    }
    //@formatter:on
    public static final String PROP_ID = "id";
    public static final String PROP_userId = PROP_ID + "." + Id.PROP_userId;
    public static final String PROP_permissionTypeId = PROP_ID + "." + Id.PROP_permissionTypeId;

    @EmbeddedId
    private Id id;

    public Permission() {
    }

    public Permission(Id id) {
        this.id = id;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @Embeddable
    public static final class Id implements Serializable {

        public static final String PROP_userId = "userId";
        public static final String PROP_permissionTypeId = "permissionTypeId";

        public Id() {
        }

        public Id(Long userId, Long permissionTypeId) {
            this.userId = userId;
            this.permissionTypeId = permissionTypeId;
        }

        @Column(name = "C03_USER_ID", nullable = false)
        private Long userId;

        @Column(name = "C03_PERMISSION_TYPE_ID", nullable = false)
        private Long permissionTypeId;

        public Long getUserId() {
            return userId;
        }

        public Long getPermissionTypeId() {
            return permissionTypeId;
        }
    }
}