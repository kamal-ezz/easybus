package com.kamal.easybus.repo;


import com.kamal.easybus.model.Trip;

import java.sql.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripRepo extends JpaRepository<Trip, Long> {
	
	Page<Trip> findByDepartureCityAndDestinationCityAndDate(String departureCity,
							String destinationCity, Date date, Pageable pageable);

	/*@Query(value = "SELECT * FROM trips t WHERE t.departure_city=?1 AND t.destination_city=?2 AND t.date=?3")
	Page<Trip> findTrips(String departureCity,
						 String destinationCity, Date date, Pageable pageable);*/

}
