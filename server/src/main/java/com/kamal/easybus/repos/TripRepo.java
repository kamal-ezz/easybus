package com.kamal.easybus.repos;


import com.kamal.easybus.dtos.TripDTO;
import com.kamal.easybus.entities.Trip;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripRepo extends JpaRepository<Trip, Long> {
	
	/*Page<Trip> findByDepartureCityAndDestinationCityAndDate(String departureCity,
							String destinationCity, Date date, Pageable pageable);*/

	@Query("select t from Trip t where t.departureCity=?1 and t.destinationCity=?2 and t.date=?3")
	List<Trip> findTrips(String departureCity,
						 String destinationCity, Date date);

	@Query("select t from Trip t where t.availableSeats.size > 0")
	List<TripDTO> getAvailableTrips();
}
