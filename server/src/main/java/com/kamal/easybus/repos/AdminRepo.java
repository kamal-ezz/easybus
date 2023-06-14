package com.kamal.easybus.repos;


import com.kamal.easybus.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Admin, Long>{

	Optional<Admin> findByEmail(String email);

    List<Admin> findByIdIn(List<Long> userIds);

    Boolean existsByEmail(String email);

}
