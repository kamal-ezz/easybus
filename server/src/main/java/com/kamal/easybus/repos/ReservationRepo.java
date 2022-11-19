package com.kamal.easybus.repos;

import com.kamal.easybus.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepo extends JpaRepository<Reservation, Long>{

}
