package com.jencarnacion.securedApp.role.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jencarnacion.securedApp.role.Role;
import com.jencarnacion.securedApp.role.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
