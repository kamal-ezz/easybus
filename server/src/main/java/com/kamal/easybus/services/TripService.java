package com.kamal.easybus.services;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.kamal.easybus.dtos.TripDTO;
import com.kamal.easybus.entities.Bus;
import com.kamal.easybus.entities.Seat;
import com.kamal.easybus.exceptions.ResourceNotFoundException;
import com.kamal.easybus.repos.BusRepo;
import com.kamal.easybus.repos.SeatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kamal.easybus.entities.Trip;
import com.kamal.easybus.repos.TripRepo;


@Service
public class TripService {
    TripRepo tripRepo;
    BusRepo busRepo;
    SeatRepo seatRepo;

	@Autowired
	public TripService(TripRepo tripRepo, BusRepo busRepo, SeatRepo seatRepo) {
		this.tripRepo = tripRepo;
        this.busRepo = busRepo;
        this.seatRepo = seatRepo;
	}

	public List<TripDTO> getAllTrips() {
        return tripRepo.findAll().stream().map(this::mapTripToTripDTO).collect(Collectors.toList());
    }

    public TripDTO getTripById(long id) {
        return mapTripToTripDTO(tripRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id)));
    }

    public void addTrip(TripDTO tripDTO) {
        List<Seat> seats = tripDTO.getAvailableSeats().stream().map(Seat::new).collect(Collectors.toList());
        seatRepo.saveAll(seats);
        tripRepo.save(mapTripDTOToTrip(tripDTO));
    }

    public void updateTrip(long id, TripDTO tripDTO) {
        Trip _trip = tripRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
        Trip newTrip = mapTripDTOToTrip(tripDTO);
        _trip.setBus(newTrip.getBus());
        _trip.setDepartureCity(newTrip.getDepartureCity());
        _trip.setDestinationCity(newTrip.getDestinationCity());
        _trip.setDate(newTrip.getDate());
        _trip.setDepartureTime(newTrip.getDepartureTime());
        _trip.setDestinationTime(newTrip.getDestinationTime());
        _trip.setPrice(newTrip.getPrice());
        _trip.setAvailableSeats(newTrip.getAvailableSeats());
        _trip.setIsAvailable(newTrip.getIsAvailable());
        tripRepo.save(_trip);
    }

    public void deleteTrip(long id) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));

        tripRepo.delete(trip);
    }

    public Page<Trip> searchTrips(String departureCity,
                                  String arrivalCity,
                                  Date date,
                                  Pageable pageable) {
        //return tripRepo.findByDepartureCityAndDestinationCityAndDate(departureCity,arrivalCity, date, pageable);
        return tripRepo.findTrips(departureCity, arrivalCity, date, pageable);
    }

    public TripDTO mapTripToTripDTO(Trip trip) {
        return TripDTO.builder()
                .busCompany(trip.getBus().getCompany())
                .busLogo(trip.getBus().getLogo())
                .departureCity(trip.getDepartureCity())
                .destinationCity(trip.getDestinationCity())
                .date(trip.getDate())
                .departureTime(trip.getDepartureTime())
                .destinationTime(trip.getDestinationTime())
                .price(trip.getPrice())
                .isAvailable(trip.getIsAvailable())
                .availableSeats(trip.getAvailableSeats().stream().map(Seat::getNumber).collect(Collectors.toList()))
                .build();
    }


	public Trip mapTripDTOToTrip(TripDTO tripDTO){
		Bus bus = busRepo.findByCompany(tripDTO.getBusCompany())
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "company", tripDTO.getBusCompany()));
        List<Seat> seats = tripDTO.getAvailableSeats().stream().map(Seat::new).collect(Collectors.toList());
		return Trip.builder()
				.bus(bus)
				.departureCity(tripDTO.getDepartureCity())
				.destinationCity(tripDTO.getDestinationCity())
				.date(tripDTO.getDate())
				.departureTime(tripDTO.getDepartureTime())
				.destinationTime(tripDTO.getDestinationTime())
				.price(tripDTO.getPrice())
				.isAvailable(tripDTO.getIsAvailable())
				.availableSeats(seats)
				.build();
	}
}
