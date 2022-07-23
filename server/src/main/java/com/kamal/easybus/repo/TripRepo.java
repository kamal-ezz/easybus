package com.kamal.easybus.repo;


import com.kamal.easybus.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepo extends JpaRepository<Trip, Long> {
	
	Page<Trip> findByDepartureCityAndDestinationCityAndDate(String departureCity,
							String arrivalCity, String date, Pageable pageable);
	
	Page<Trip> findByDate(String date, Pageable pageable);

}
