package com.kamal.easybus.repos;

import com.kamal.easybus.entities.Seat;
import com.kamal.easybus.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Long> {
    //List<Seat> findSeatsByTrip(Trip trip);

}
