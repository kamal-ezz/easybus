package com.easybus.repository;

import com.easybus.entities.Trip;
import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripRepository extends JpaRepository<Trip, Long> {

  @Query("select t from Trip t where t.fromCity=?1 and t.toCity=?2 and t.date=?3")
  List<Trip> findTrips(String fromCity, String toCity, Date date);

  @Query("select t from Trip t where size(t.availableSeats) > 0")
  List<Trip> getAvailableTrips();
}
