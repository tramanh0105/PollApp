package com.pollapp.pollapp.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleName name);
}
