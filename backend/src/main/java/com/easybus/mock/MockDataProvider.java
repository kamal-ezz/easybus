package com.easybus.mock;

import com.easybus.dtos.BookingRequest;
import com.easybus.dtos.BookingResponse;
import com.easybus.dtos.City;
import com.easybus.dtos.Operator;
import com.easybus.dtos.TripResponse;
import com.easybus.enums.BookingStatus;
import com.easybus.enums.Equipment;
import com.easybus.enums.Role;
import com.easybus.security.UserPrincipal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Provides mock data for all services when mock mode is enabled. This component is only created
 * when app.mock-mode=true.
 */
@Component
@ConditionalOnProperty(name = "app.mock-mode", havingValue = "true")
public class MockDataProvider {

  private static final Logger log = LoggerFactory.getLogger(MockDataProvider.class);

  private static final int SEAT_ROWS = 10;
  private static final String[] SEAT_COLS = {"A", "B", "C", "D"};
  private static final int TRIPS_PER_ROUTE = 3;
  private static final int DAYS_AHEAD = 14;

  private final ObjectMapper objectMapper;
  private final AtomicLong bookingIdGenerator = new AtomicLong(1000);
  private final AtomicLong tripIdGenerator = new AtomicLong(1);

  private List<City> cities;
  private List<Operator> operators;
  private List<TripResponse> mockTrips;

  public MockDataProvider(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  public void init() {
    loadCities();
    loadOperators();
    generateMockTrips();
    log.info(
        "Mock mode enabled - loaded {} cities, {} operators, {} trips",
        cities.size(),
        operators.size(),
        mockTrips.size());
  }

  private void loadCities() {
    try {
      cities =
          objectMapper.readValue(
              new ClassPathResource("data/cities.json").getInputStream(),
              new TypeReference<List<City>>() {});
    } catch (IOException e) {
      log.warn("Failed to load cities.json, using default cities");
      cities =
          List.of(
              new City("casablanca", "Casablanca"),
              new City("rabat", "Rabat"),
              new City("marrakech", "Marrakech"),
              new City("fes", "Fès"),
              new City("tangier", "Tangier"),
              new City("agadir", "Agadir"));
    }
  }

  private void loadOperators() {
    try {
      operators =
          objectMapper.readValue(
              new ClassPathResource("data/bus-companies.json").getInputStream(),
              new TypeReference<List<Operator>>() {});
    } catch (IOException e) {
      log.warn("Failed to load bus-companies.json, using default operators");
      operators =
          List.of(
              new Operator(
                  "CTM",
                  "",
                  List.of(Equipment.AIR_CONDITIONER, Equipment.WIFI, Equipment.USB_CHARGER)),
              new Operator(
                  "Supratours",
                  "",
                  List.of(Equipment.AIR_CONDITIONER, Equipment.TV, Equipment.WIFI)));
    }
  }

  private void generateMockTrips() {
    mockTrips = new ArrayList<>();
    List<String> allSeats = buildSeatList();

    // Generate trips for popular routes
    String[][] popularRoutes = {
      {"casablanca", "rabat"},
      {"casablanca", "marrakech"},
      {"rabat", "fes"},
      {"marrakech", "agadir"},
      {"tangier", "casablanca"},
      {"fes", "meknes"}
    };

    long now = System.currentTimeMillis();
    int operatorIndex = 0;

    for (String[] route : popularRoutes) {
      for (int day = 0; day < DAYS_AHEAD; day++) {
        Date date = new Date(now + (long) day * 24 * 60 * 60 * 1000);

        for (int slot = 0; slot < TRIPS_PER_ROUTE; slot++) {
          Operator operator = operators.get(operatorIndex % operators.size());
          operatorIndex++;

          int depHour = 6 + slot * 4; // 06:00, 10:00, 14:00
          int arrHour = depHour + 2 + (route[0].hashCode() % 2);
          if (arrHour >= 24) arrHour = arrHour - 24;

          double price = 80 + (route[0].hashCode() + route[1].hashCode()) % 100;
          if (price < 80) price = 80;

          TripResponse trip =
              new TripResponse(
                  tripIdGenerator.getAndIncrement(),
                  operator.company(),
                  operator.equipments(),
                  route[0],
                  route[1],
                  date,
                  Time.valueOf(String.format("%02d:00:00", depHour)),
                  Time.valueOf(String.format("%02d:00:00", arrHour)),
                  price,
                  new ArrayList<>(allSeats));
          mockTrips.add(trip);

          // Also add reverse route
          TripResponse reverseTrip =
              new TripResponse(
                  tripIdGenerator.getAndIncrement(),
                  operator.company(),
                  operator.equipments(),
                  route[1],
                  route[0],
                  date,
                  Time.valueOf(String.format("%02d:00:00", depHour + 1)),
                  Time.valueOf(String.format("%02d:00:00", arrHour + 1)),
                  price,
                  new ArrayList<>(allSeats));
          mockTrips.add(reverseTrip);
        }
      }
    }
  }

  private List<String> buildSeatList() {
    List<String> seats = new ArrayList<>();
    for (int row = 1; row <= SEAT_ROWS; row++) {
      for (String col : SEAT_COLS) {
        seats.add(row + col);
      }
    }
    return seats;
  }

  // === Public API ===

  public List<City> getCities() {
    return cities;
  }

  public List<Operator> getOperators() {
    return operators;
  }

  public List<TripResponse> getAllTrips() {
    return mockTrips;
  }

  /**
   * Search trips by route and date. Returns matching trips if found, otherwise returns sample trips
   * with the requested cities (always returns results).
   */
  public List<TripResponse> searchTrips(String fromCity, String toCity, Date date) {
    // First, try to find exact matches
    List<TripResponse> matches =
        mockTrips.stream()
            .filter(t -> t.fromCity().equalsIgnoreCase(fromCity))
            .filter(t -> t.toCity().equalsIgnoreCase(toCity))
            .filter(t -> t.date().equals(date))
            .toList();

    if (!matches.isEmpty()) {
      return matches;
    }

    // If no exact match, return sample trips for any date with those cities
    matches =
        mockTrips.stream()
            .filter(t -> t.fromCity().equalsIgnoreCase(fromCity))
            .filter(t -> t.toCity().equalsIgnoreCase(toCity))
            .limit(TRIPS_PER_ROUTE)
            .toList();

    if (!matches.isEmpty()) {
      // Adjust date to requested date
      return matches.stream()
          .map(
              t ->
                  new TripResponse(
                      t.id(),
                      t.operator(),
                      t.equipments(),
                      t.fromCity(),
                      t.toCity(),
                      date,
                      t.departure(),
                      t.arrival(),
                      t.price(),
                      t.availableSeats()))
          .toList();
    }

    // If still no match, generate trips on-the-fly for the requested route
    return generateTripsForRoute(fromCity, toCity, date);
  }

  private List<TripResponse> generateTripsForRoute(String fromCity, String toCity, Date date) {
    List<TripResponse> trips = new ArrayList<>();
    List<String> allSeats = buildSeatList();

    for (int slot = 0; slot < TRIPS_PER_ROUTE; slot++) {
      Operator operator = operators.get(slot % operators.size());
      int depHour = 6 + slot * 4;
      int arrHour = depHour + 3;

      trips.add(
          new TripResponse(
              tripIdGenerator.getAndIncrement(),
              operator.company(),
              operator.equipments(),
              fromCity,
              toCity,
              date,
              Time.valueOf(String.format("%02d:00:00", depHour)),
              Time.valueOf(String.format("%02d:00:00", arrHour)),
              100.0 + slot * 20,
              new ArrayList<>(allSeats)));
    }
    return trips;
  }

  public TripResponse getTripById(Long id) {
    return mockTrips.stream()
        .filter(t -> t.id().equals(id))
        .findFirst()
        .orElseGet(
            () -> {
              // Return a default trip if not found
              Date today = new Date(System.currentTimeMillis());
              return new TripResponse(
                  id,
                  operators.get(0).company(),
                  operators.get(0).equipments(),
                  "casablanca",
                  "rabat",
                  today,
                  Time.valueOf("08:00:00"),
                  Time.valueOf("10:00:00"),
                  120.0,
                  buildSeatList());
            });
  }

  public UserPrincipal getMockUser() {
    return new UserPrincipal(
        1L,
        "Demo User",
        "demo@easybus.com",
        "demouser",
        null,
        "en",
        true,
        Role.USER,
        null,
        List.of(new SimpleGrantedAuthority("ROLE_USER")));
  }

  public BookingResponse createMockBooking(BookingRequest request) {
    return new BookingResponse(
        bookingIdGenerator.getAndIncrement(),
        request.tripId(),
        request.seat(),
        request.fullName(),
        request.email(),
        request.phone(),
        request.amount(),
        request.currency(),
        request.paypalOrderId(),
        request.paymentMethod(),
        BookingStatus.CONFIRMED,
        Instant.now());
  }

  public List<Long> createMockBookingsForPaypal(
      Long tripId,
      List<String> seats,
      String fullName,
      String email,
      String phone,
      Double amount,
      String currency,
      String paypalOrderId) {
    List<Long> ids = new ArrayList<>();
    for (int i = 0; i < seats.size(); i++) {
      ids.add(bookingIdGenerator.getAndIncrement());
    }
    return ids;
  }
}
