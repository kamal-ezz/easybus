package com.easybus.services;

import com.easybus.dtos.TripRequest;
import com.easybus.dtos.TripResponse;
import com.easybus.exceptions.ResourceNotFoundException;
import com.easybus.mappers.TripMapper;
import com.easybus.mock.MockDataProvider;
import com.easybus.repository.TripRepository;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {
  private final TripRepository tripRepository;

  @Value("${app.mock-mode:false}")
  private boolean mockMode;

  @Autowired(required = false)
  private MockDataProvider mockDataProvider;

  public List<TripResponse> getAllTrips() {
    if (mockMode && mockDataProvider != null) {
      return mockDataProvider.getAllTrips();
    }
    return tripRepository.findAll().stream()
        .map(TripMapper::toTripDTO)
        .collect(Collectors.toList());
  }

  public List<TripResponse> getAvailableTrips() {
    if (mockMode && mockDataProvider != null) {
      return mockDataProvider.getAllTrips();
    }
    return tripRepository.getAvailableTrips().stream()
        .map(TripMapper::toTripDTO)
        .collect(Collectors.toList());
  }

  public TripResponse getTripById(long id) {
    if (mockMode && mockDataProvider != null) {
      return mockDataProvider.getTripById(id);
    }
    return TripMapper.toTripDTO(
        tripRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id)));
  }

  public void addTrip(TripRequest trip) {
    tripRepository.save(TripMapper.toTrip(trip));
  }

  public void updateTrip(long id, TripRequest newTrip) {
    var trip =
        tripRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
    trip.setOperator(newTrip.operator());
    trip.setEquipments(newTrip.equipments());
    trip.setFromCity(newTrip.fromCity());
    trip.setToCity(newTrip.toCity());
    trip.setDate(newTrip.date());
    trip.setDeparture(newTrip.departure());
    trip.setArrival(newTrip.arrival());
    trip.setPrice(newTrip.price());
    trip.setAvailableSeats(newTrip.availableSeats());
    tripRepository.save(trip);
  }

  public void deleteTrip(long id) {
    var trip =
        tripRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));

    tripRepository.delete(trip);
  }

  public List<TripResponse> searchTrips(String departureCity, String arrivalCity, Date date) {
    if (mockMode && mockDataProvider != null) {
      return mockDataProvider.searchTrips(departureCity, arrivalCity, date);
    }
    return tripRepository.findTrips(departureCity, arrivalCity, date).stream()
        .map(TripMapper::toTripDTO)
        .collect(Collectors.toList());
  }
}
