package com.easybus.mappers;

import com.easybus.dtos.TripRequest;
import com.easybus.dtos.TripResponse;
import com.easybus.entities.Trip;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class TripMapper {

  public static TripResponse toTripDTO(Trip trip) {
    return new TripResponse(
        trip.getId(),
        trip.getOperator(),
        trip.getEquipments(),
        trip.getFromCity(),
        trip.getToCity(),
        trip.getDate(),
        trip.getDeparture(),
        trip.getArrival(),
        trip.getPrice(),
        trip.getAvailableSeats());
  }

  public static Trip toTrip(TripRequest request) {
    return Trip.builder()
        .operator(request.operator())
        .equipments(request.equipments())
        .fromCity(request.fromCity())
        .toCity(request.toCity())
        .date(request.date())
        .departure(request.departure())
        .arrival(request.arrival())
        .price(request.price())
        .availableSeats(request.availableSeats())
        .build();
  }
}
