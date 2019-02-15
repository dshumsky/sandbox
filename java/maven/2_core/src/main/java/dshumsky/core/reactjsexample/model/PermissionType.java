package dshumsky.core.reactjsexample.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public enum PermissionType {
    CRUD_USERS(1L),
    CRUD_ALL_TRIPS(2L),
    CRUD_OWN_TRIPS(3L);

    private static final Map<Long, PermissionType> map = new HashMap<>();

    static {
        for (PermissionType permissionType : PermissionType.values()) {
            map.put(permissionType.id, permissionType);
        }
    }

    private final long id;

    PermissionType(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public static PermissionType getById(long id) {
        return map.get(id);
    }
}
