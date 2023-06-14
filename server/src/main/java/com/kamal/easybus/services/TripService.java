package com.kamal.easybus.services;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.kamal.easybus.dtos.TripDTO;
import com.kamal.easybus.entities.Seat;
import com.kamal.easybus.exceptions.ResourceNotFoundException;
import com.kamal.easybus.repos.BusRepo;
import com.kamal.easybus.repos.SeatRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<TripDTO> getAvailableTrips(){
        return tripRepo.getAvailableTrips();
    }

    public TripDTO getTripById(long id) {
        return mapTripToTripDTO(tripRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id)));
    }

    public void addTrip(Trip trip) {
        tripRepo.save(trip);
    }

    public void updateTrip(long id, Trip newTrip) {
        Trip trip = tripRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
        trip.setBus(newTrip.getBus());
        trip.setDepartureCity(newTrip.getDepartureCity());
        trip.setDestinationCity(newTrip.getDestinationCity());
        trip.setDate(newTrip.getDate());
        trip.setDepartureTime(newTrip.getDepartureTime());
        trip.setDestinationTime(newTrip.getDestinationTime());
        trip.setPrice(newTrip.getPrice());
        trip.setAvailableSeats(newTrip.getAvailableSeats());
        tripRepo.save(trip);
    }

    public void deleteTrip(long id) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));

        tripRepo.delete(trip);
    }

    public List<TripDTO> searchTrips(String departureCity,
                                  String arrivalCity,
                                  Date date) {
        //return tripRepo.findByDepartureCityAndDestinationCityAndDate(departureCity,arrivalCity, date, pageable);
        return tripRepo.findTrips(departureCity, arrivalCity, date).stream().map(this::mapTripToTripDTO).collect(Collectors.toList());
    }

    public void addSeat(Seat seat, Trip trip){
        seat.setTrip(trip);
        seatRepo.save(seat);
    }

    public void addSeats(List<Seat> seats, Trip trip){
        seats.forEach(seat -> seat.setTrip(trip));
        seatRepo.saveAll(seats);
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
                .availableSeats(trip.getAvailableSeats().stream().map(Seat::getNumber).collect(Collectors.toList()))
                .build();
    }


	/*public Trip mapTripDTOToTrip(TripDTO tripDTO){
		Bus bus = busRepo.findByCompany(tripDTO.getBusCompany())
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "company", tripDTO.getBusCompany()));
        List<Seat> seats = tripDTO.getAvailableSeats().stream().map(
                seat -> {

                    return new Seat(seat);
                }
        ).collect(Collectors.toList());
		return Trip.builder()
				.bus(bus)
				.departureCity(tripDTO.getDepartureCity())
				.destinationCity(tripDTO.getDestinationCity())
				.date(tripDTO.getDate())
				.departureTime(tripDTO.getDepartureTime())
				.destinationTime(tripDTO.getDestinationTime())
				.price(tripDTO.getPrice())
				.availableSeats(seats)
				.build();
	}*/
}
