package com.easybus.repository;

import com.easybus.entities.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
  List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
}
