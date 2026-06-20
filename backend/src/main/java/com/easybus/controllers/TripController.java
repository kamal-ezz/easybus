package com.easybus.controllers;

import com.easybus.dtos.TripRequest;
import com.easybus.dtos.TripResponse;
import com.easybus.exceptions.ResourceNotFoundException;
import com.easybus.services.TripService;
import java.sql.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

  private final TripService tripService;

  @GetMapping("/all")
  public ResponseEntity<List<TripResponse>> getAllTrips() {
    return new ResponseEntity<>(tripService.getAllTrips(), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<TripResponse>> getAvailableTrips() {
    return new ResponseEntity<>(tripService.getAvailableTrips(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getTripById(@PathVariable long id) {
    try {
      return new ResponseEntity<>(tripService.getTripById(id), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>("Trip Not Found", HttpStatus.OK);
    }
  }

  @PostMapping
  public ResponseEntity<String> addTrip(@RequestBody TripRequest trip) {
    tripService.addTrip(trip);
    return new ResponseEntity<>("Trip successfully added", HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateTrip(@PathVariable long id, @RequestBody TripRequest trip) {
    try {
      tripService.updateTrip(id, trip);
      return new ResponseEntity<>("Trip successfully updated", HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>("Trip not found", HttpStatus.OK);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTrip(@PathVariable long id) {
    tripService.deleteTrip(id);
    return new ResponseEntity<>("Trip successfully deleted", HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<List<TripResponse>> searchTrips(
      @RequestParam("departureCity") String departureCity,
      @RequestParam("destinationCity") String destinationCity,
      @RequestParam("date") String date) {

    var d = Date.valueOf(date);
    return new ResponseEntity<>(
        tripService.searchTrips(departureCity, destinationCity, d), HttpStatus.OK);
  }
}
