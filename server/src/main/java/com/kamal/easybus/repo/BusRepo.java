package com.kamal.easybus.repo;

import com.kamal.easybus.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepo extends JpaRepository<Bus, Long> {
}
