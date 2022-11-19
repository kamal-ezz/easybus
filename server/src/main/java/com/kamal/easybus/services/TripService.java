package com.kamal.easybus.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.kamal.easybus.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kamal.easybus.entities.Trip;
import com.kamal.easybus.repos.TripRepo;


@Service
@AllArgsConstructor
public class TripService {
	
	@Autowired
	private TripRepo tripRepo;

	public List<Trip> getAllTrips(){
		return tripRepo.findAll();
	}

	public Trip getTripById(long id) {
		return tripRepo.findById(id)
					   .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
	}
	
	public Trip addTrip(Trip trip) {
		return tripRepo.save(trip);
	}
	
	public Trip updateTrip(long id, Trip trip) {
		Optional<Trip> tripData = tripRepo.findById(id);
		
		if (tripData.isPresent()) {
		      Trip _trip = tripData.get();
		      _trip.setBus(trip.getBus());
		      _trip.setDepartureCity(trip.getDepartureCity());
		      _trip.setDestinationCity(trip.getDestinationCity());
		      _trip.setDate(trip.getDate());
		      _trip.setDepartureTime(trip.getDepartureTime());
		      _trip.setDestinationTime(trip.getDestinationTime());
		      _trip.setPrice(trip.getPrice());
		      _trip.setAvailableSeats(trip.getAvailableSeats());
			  _trip.setIsAvailable(trip.getIsAvailable());
			  tripRepo.save(_trip);
		      return _trip;		   
		}else {
			return null;
		}
	}
	
	public void deleteTrip(long id) {
		Trip trip = tripRepo.findById(id)
							.orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
		
		tripRepo.delete(trip);  
	}

	public Page<Trip> searchTrips(String departureCity,
			String arrivalCity,
		    Date date,
		    Pageable pageable){
		//return tripRepo.findByDepartureCityAndDestinationCityAndDate(departureCity,arrivalCity, date, pageable);
		return tripRepo.findTrips(departureCity,arrivalCity, date, pageable);
	}
}