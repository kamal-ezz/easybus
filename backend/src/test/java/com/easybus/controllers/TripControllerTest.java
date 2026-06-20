package com.easybus.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.easybus.dtos.TripResponse;
import com.easybus.enums.Equipment;
import com.easybus.exceptions.ResourceNotFoundException;
import com.easybus.services.TripService;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TripControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private TripService tripService;

  private TripResponse createSampleTrip() {
    return new TripResponse(
        1L,
        "CTM",
        List.of(Equipment.AIR_CONDITIONER, Equipment.WIFI),
        "casablanca",
        "rabat",
        Date.valueOf("2026-02-15"),
        Time.valueOf("08:00:00"),
        Time.valueOf("10:30:00"),
        120.0,
        List.of("1A", "1B", "2A", "2B"));
  }

  @Test
  void searchTrips_returns200WithTrips() throws Exception {
    TripResponse trip = createSampleTrip();
    when(tripService.searchTrips(eq("casablanca"), eq("rabat"), any(Date.class)))
        .thenReturn(List.of(trip));

    mockMvc
        .perform(
            get("/api/trips/search")
                .param("departureCity", "casablanca")
                .param("destinationCity", "rabat")
                .param("date", "2026-02-15"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].operator").value("CTM"))
        .andExpect(jsonPath("$[0].fromCity").value("casablanca"))
        .andExpect(jsonPath("$[0].toCity").value("rabat"));
  }

  @Test
  void searchTrips_withNoResults_returnsEmptyArray() throws Exception {
    when(tripService.searchTrips(any(), any(), any())).thenReturn(List.of());

    mockMvc
        .perform(
            get("/api/trips/search")
                .param("departureCity", "agadir")
                .param("destinationCity", "fes")
                .param("date", "2026-02-15"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void getTripById_returns200WithTrip() throws Exception {
    TripResponse trip = createSampleTrip();
    when(tripService.getTripById(1L)).thenReturn(trip);

    mockMvc
        .perform(get("/api/trips/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.operator").value("CTM"));
  }

  @Test
  void getTripById_withInvalidId_returnsNotFoundMessage() throws Exception {
    when(tripService.getTripById(999L))
        .thenThrow(new ResourceNotFoundException("Trip", "id", 999L));

    mockMvc
        .perform(get("/api/trips/999"))
        .andExpect(status().isOk())
        .andExpect(content().string("Trip Not Found"));
  }

  @Test
  void getAvailableTrips_returns200WithTrips() throws Exception {
    TripResponse trip = createSampleTrip();
    when(tripService.getAvailableTrips()).thenReturn(List.of(trip));

    mockMvc
        .perform(get("/api/trips"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].availableSeats").isArray())
        .andExpect(jsonPath("$[0].availableSeats.length()").value(4));
  }

  @Test
  @WithMockUser
  void getAllTrips_returns200WithAllTrips() throws Exception {
    TripResponse trip = createSampleTrip();
    when(tripService.getAllTrips()).thenReturn(List.of(trip));

    mockMvc
        .perform(get("/api/trips/all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value(1));
  }
}
