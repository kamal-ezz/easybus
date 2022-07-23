package com.kamal.easybus.repo;

import com.kamal.easybus.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepo extends JpaRepository<Reservation, Long>{

}
