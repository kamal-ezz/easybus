package com.kamal.easybus.repo;

import java.util.Optional;

import com.kamal.easybus.model.ERole;
import com.kamal.easybus.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long>{
    Optional<Role> findByName(ERole roleName);
}
