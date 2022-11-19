package com.kamal.easybus.repos;


import com.kamal.easybus.entities.Trip;

import java.sql.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripRepo extends JpaRepository<Trip, Long> {
	
	/*Page<Trip> findByDepartureCityAndDestinationCityAndDate(String departureCity,
							String destinationCity, Date date, Pageable pageable);*/

	@Query(value = "select t FROM Trip t WHERE t.departureCity=?1 and t.destinationCity=?2 and t.date=?3")
	Page<Trip> findTrips(String departureCity,
						 String destinationCity, Date date, Pageable pageable);

}
