package com.easybus.repository;

import com.easybus.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  List<User> findByIdIn(List<Long> userIds);

  boolean existsByEmail(String email);

  Optional<User> findByGoogleSub(String googleSub);
}
