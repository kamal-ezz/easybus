package com.kamal.easybus.repos;

import com.kamal.easybus.entities.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusRepo extends JpaRepository<Bus, Long> {
    Optional<Bus> findByCompany(String company);
}
