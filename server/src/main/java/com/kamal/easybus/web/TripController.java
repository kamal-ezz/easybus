package com.kamal.easybus.web;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.kamal.easybus.entities.Trip;
import com.kamal.easybus.services.TripService;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

	private TripService tripService;

	@Autowired
	public TripController(TripService tripService) {
		this.tripService = tripService;
	}

	@GetMapping
	public ResponseEntity<List<Trip>> getTrips() {
		List<Trip> trips = tripService.getAllTrips();
		if(trips.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(trips,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Trip> getTripById(@PathVariable("id") long id) {
		Trip trip = tripService.getTripById(id);
		return new ResponseEntity<>(trip,HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Trip> addTrip(@RequestBody Trip trip){
		Trip _trip = tripService.addTrip(new Trip(
				trip.getBus(),
				trip.getDepartureCity(),
				trip.getDestinationCity(),
				trip.getDate(),
				trip.getDepartureTime(),
				trip.getDestinationTime(),
				trip.getPrice(),
				trip.getAvailableSeats(),
				trip.getIsAvailable()
			));
		return new ResponseEntity<>(_trip,HttpStatus.CREATED);
	}
	
	 @PutMapping("/{id}")
	 @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<?> updateTrip(@PathVariable("id") long id, @RequestBody Trip trip) {
		Trip _trip = tripService.updateTrip(id,trip);
	    if (_trip != null) {
	      return new ResponseEntity<>(_trip, HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>("Trip not found",HttpStatus.NOT_FOUND);
	    }
	  }
	
	 @DeleteMapping("/{id}")
	 @PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<?> deleteTrip(@PathVariable("id") long id) {
		tripService.deleteTrip(id);
		return new ResponseEntity<>("Trip successfully deleted", HttpStatus.OK);
	 }
	 
	 @GetMapping("/search")
	 public Page<Trip> searchTrips(
			 @RequestParam("departureCity") String departureCity,
			 @RequestParam("destinationCity") String destinationCity,
			 @RequestParam("date") String date,
			 Pageable pageable){

		 Date d = Date.valueOf(date);		
		 return tripService.searchTrips(departureCity,destinationCity, d, pageable);
	 }

}