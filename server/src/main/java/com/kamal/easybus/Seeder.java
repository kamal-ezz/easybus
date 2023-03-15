package com.kamal.easybus;

import com.kamal.easybus.entities.Bus;
import com.kamal.easybus.entities.Seat;
import com.kamal.easybus.entities.Trip;
import com.kamal.easybus.entities.User;
import com.kamal.easybus.enums.Equipment;
import com.kamal.easybus.enums.Role;
import com.kamal.easybus.services.BusService;
import com.kamal.easybus.services.TripService;
import com.kamal.easybus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Seeder {

    BusService busService;
    TripService tripService;
    UserService userService;

    @Autowired
    public Seeder(BusService busService, TripService tripService, UserService userService) {
        this.busService = busService;
        this.tripService = tripService;
        this.userService = userService;
    }

    public void seed(){
        //Buses
        List<Equipment> globusEquipment = Arrays.asList(Equipment.AIR_CONDITIONER,
                Equipment.LAMP,
                Equipment.TV,
                Equipment.HIGHWAY);

        Bus globus = Bus.builder()
                .company("Globus")
                .logo("")
                .equipments(globusEquipment)
                .build();
        busService.addBus(globus);

        List<Equipment> ghazalaEquipment = Arrays.asList(Equipment.WIFI,
                Equipment.AIR_CONDITIONER,
                Equipment.LAMP,
                Equipment.TV,
                Equipment.HIGHWAY,
                Equipment.PHARMACY_BOX,
                Equipment.USB_CHARGER);

        Bus ghazala = Bus.builder()
                .company("Ghazala")
                .logo("")
                .equipments(ghazalaEquipment)
                .build();
        busService.addBus(ghazala);

        //Trips

        Seat seat1 = new Seat(10);
        Seat seat2 = new Seat(11);

        Seat seat3 = new Seat(10);
        Seat seat4 = new Seat(11);


        Trip trip1 = Trip.builder()
                .bus(globus)
                .date(Date.valueOf(LocalDate.now()))
                .departureCity("Casablanca")
                .destinationCity("Marrakech")
                .departureTime(Time.valueOf("10:00:00"))
                .destinationTime(Time.valueOf("14:15:00"))
                .availableSeats(List.of(seat1,seat2))
                .price(100)
                .build();

        tripService.addTrip(trip1);

        Trip trip2 = Trip.builder()
                .bus(ghazala)
                .date(Date.valueOf(LocalDate.now()))
                .departureCity("Casablanca")
                .destinationCity("Marrakech")
                .departureTime(Time.valueOf("15:00:00"))
                .destinationTime(Time.valueOf("19:00:00"))
                .availableSeats(List.of(seat3,seat4))
                .price(100)
                .build();

        tripService.addTrip(trip2);

        tripService.addSeats(List.of(seat1,seat2), trip1);
        tripService.addSeats(List.of(seat3,seat4), trip2);


        User adminUser = User.builder()
                .firstName("Kamal")
                .lastName("Ezzarmou")
                .email("kamal@admin.com")
                .password((new BCryptPasswordEncoder()).encode("admin00"))
                .phone("000")
                .build();

        Set<Role> roles = new HashSet<>();
        Role adminRole = Role.ADMIN;
        roles.add(adminRole);
        adminUser.setRoles(roles);
        userService.addUser(adminUser);
    }


}
