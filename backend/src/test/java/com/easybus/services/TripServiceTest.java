package com.easybus.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.easybus.dtos.TripResponse;
import com.easybus.entities.Trip;
import com.easybus.enums.Equipment;
import com.easybus.exceptions.ResourceNotFoundException;
import com.easybus.repository.TripRepository;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

  @Mock private TripRepository tripRepository;

  @InjectMocks private TripService tripService;

  private Trip sampleTrip;

  @BeforeEach
  void setUp() {
    sampleTrip =
        Trip.builder()
            .id(1L)
            .operator("CTM")
            .equipments(List.of(Equipment.AIR_CONDITIONER, Equipment.WIFI))
            .fromCity("casablanca")
            .toCity("rabat")
            .date(Date.valueOf("2026-02-15"))
            .departure(Time.valueOf("08:00:00"))
            .arrival(Time.valueOf("10:30:00"))
            .price(120.0)
            .availableSeats(List.of("1A", "1B", "2A", "2B"))
            .build();
  }

  @Test
  void getAllTrips_returnsAllTrips() {
    when(tripRepository.findAll()).thenReturn(List.of(sampleTrip));

    List<TripResponse> result = tripService.getAllTrips();

    assertEquals(1, result.size());
    assertEquals("CTM", result.get(0).operator());
    assertEquals("casablanca", result.get(0).fromCity());
    verify(tripRepository).findAll();
  }

  @Test
  void getAvailableTrips_returnsTripsWithSeats() {
    when(tripRepository.getAvailableTrips()).thenReturn(List.of(sampleTrip));

    List<TripResponse> result = tripService.getAvailableTrips();

    assertEquals(1, result.size());
    assertEquals(4, result.get(0).availableSeats().size());
    verify(tripRepository).getAvailableTrips();
  }

  @Test
  void getTripById_withValidId_returnsTrip() {
    when(tripRepository.findById(1L)).thenReturn(Optional.of(sampleTrip));

    TripResponse result = tripService.getTripById(1L);

    assertNotNull(result);
    assertEquals(1L, result.id());
    assertEquals("CTM", result.operator());
    verify(tripRepository).findById(1L);
  }

  @Test
  void getTripById_withInvalidId_throwsException() {
    when(tripRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> tripService.getTripById(999L));
    verify(tripRepository).findById(999L);
  }

  @Test
  void searchTrips_withValidParams_returnsMatchingTrips() {
    Date searchDate = Date.valueOf("2026-02-15");
    when(tripRepository.findTrips("casablanca", "rabat", searchDate))
        .thenReturn(List.of(sampleTrip));

    List<TripResponse> result = tripService.searchTrips("casablanca", "rabat", searchDate);

    assertEquals(1, result.size());
    assertEquals("casablanca", result.get(0).fromCity());
    assertEquals("rabat", result.get(0).toCity());
    verify(tripRepository).findTrips("casablanca", "rabat", searchDate);
  }

  @Test
  void searchTrips_withNoMatches_returnsEmptyList() {
    Date searchDate = Date.valueOf("2026-02-15");
    when(tripRepository.findTrips("agadir", "fes", searchDate)).thenReturn(List.of());

    List<TripResponse> result = tripService.searchTrips("agadir", "fes", searchDate);

    assertTrue(result.isEmpty());
    verify(tripRepository).findTrips("agadir", "fes", searchDate);
  }

  @Test
  void deleteTrip_withValidId_deletesTrip() {
    when(tripRepository.findById(1L)).thenReturn(Optional.of(sampleTrip));
    doNothing().when(tripRepository).delete(any(Trip.class));

    assertDoesNotThrow(() -> tripService.deleteTrip(1L));
    verify(tripRepository).findById(1L);
    verify(tripRepository).delete(sampleTrip);
  }

  @Test
  void deleteTrip_withInvalidId_throwsException() {
    when(tripRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> tripService.deleteTrip(999L));
    verify(tripRepository).findById(999L);
    verify(tripRepository, never()).delete(any());
  }
}
