package com.kamal.easybus.repos;


import com.kamal.easybus.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);

    List<User> findByIdIn(List<Long> userIds);

    Boolean existsByEmail(String email);

}