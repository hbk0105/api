package com.rest.api.domain;

import java.io.Serializable;
import java.util.Objects;

public class UserRolesId implements Serializable {

    private Long userRoles_id;

    // equals, hashcode 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRolesId that = (UserRolesId) o;
        return Objects.equals(userRoles_id, that.userRoles_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRoles_id);
    }
}
