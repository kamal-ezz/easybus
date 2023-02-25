package com.kamal.easybus.web;

import java.sql.Date;
import java.util.List;

import com.kamal.easybus.dtos.TripDTO;
import com.kamal.easybus.exceptions.ResourceNotFoundException;
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
	public ResponseEntity<List<TripDTO>> getTrips() {
		List<TripDTO> trips = tripService.getAllTrips();
		if(trips.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(trips,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getTripById(@PathVariable("id") long id) {
		try{
			return new ResponseEntity<>(tripService.getTripById(id),HttpStatus.OK);
		}catch (ResourceNotFoundException e){
			return new ResponseEntity<>("Trip Not Found",HttpStatus.OK);
		}

	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addTrip(@RequestBody TripDTO tripDTO){
		tripService.addTrip(tripDTO);
		return new ResponseEntity<>("Trip successfully added",HttpStatus.CREATED);
	}
	
	 @PutMapping("/{id}")
	 @PreAuthorize("hasRole('ADMIN')")
	  public ResponseEntity<?> updateTrip(@PathVariable("id") long id, @RequestBody TripDTO trip) {
	    try {
			tripService.updateTrip(id,trip);
	        return new ResponseEntity<>("Trip successfully updated", HttpStatus.OK);
	    } catch (ResourceNotFoundException e){
	      return new ResponseEntity<>("Trip not found",HttpStatus.OK);
	    }
	  }
	
	 @DeleteMapping("/{id}")
	 @PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<?> deleteTrip(@PathVariable("id") long id) {
		tripService.deleteTrip(id);
		return new ResponseEntity<>("Trip successfully deleted", HttpStatus.OK);
	 }
	 
	 @GetMapping("/search")
	 public ResponseEntity<List<TripDTO>> searchTrips(
			 @RequestParam("departureCity") String departureCity,
			 @RequestParam("destinationCity") String destinationCity,
			 @RequestParam("date") String date,
			 Pageable pageable){

		 Date d = Date.valueOf(date);		
		 return new ResponseEntity<>(tripService.searchTrips(departureCity,destinationCity, d, pageable), HttpStatus.OK);
	 }

}