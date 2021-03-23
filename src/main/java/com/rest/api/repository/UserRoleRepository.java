package com.rest.api.repository;

import com.rest.api.domain.Privilege;
import com.rest.api.domain.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoles, Long> {

}
