package com.kamal.easybus;

import com.kamal.easybus.dtos.BusDTO;
import com.kamal.easybus.dtos.TripDTO;
import com.kamal.easybus.dtos.UserDTO;
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

        BusDTO globus = BusDTO.builder()
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

        BusDTO ghazala = BusDTO.builder()
                .company("Ghazala")
                .logo("")
                .equipments(ghazalaEquipment)
                .build();
        busService.addBus(ghazala);

        //Trips
        TripDTO trip1 = TripDTO.builder()
                .busCompany(globus.getCompany())
                .busLogo(globus.getLogo())
                .busEquipments(globusEquipment)
                .date(Date.valueOf(LocalDate.now()))
                .departureCity("Casablanca")
                .destinationCity("Marrakech")
                .departureTime(Time.valueOf("10:00:00"))
                .destinationTime(Time.valueOf("14:15:00"))
                .isAvailable(true)
                .availableSeats(List.of(10,11))
                .price(100)
                .build();

        tripService.addTrip(trip1);

        TripDTO trip2 = TripDTO.builder()
                .busCompany(ghazala.getCompany())
                .busLogo(ghazala.getLogo())
                .busEquipments(ghazalaEquipment)
                .date(Date.valueOf(LocalDate.now()))
                .departureCity("Casablanca")
                .destinationCity("Marrakech")
                .departureTime(Time.valueOf("15:00:00"))
                .destinationTime(Time.valueOf("19:00:00"))
                .isAvailable(true)
                .availableSeats(List.of(10,11))
                .price(100)
                .build();

        tripService.addTrip(trip2);

        //Users
        //User adminUser = new User("Kamal","Ezzarmou","kamal@admin.com",(new BCryptPasswordEncoder()).encode("admin00"),"000");

        UserDTO adminUserDTO = UserDTO.builder()
                .firstName("Kamal")
                .lastName("Ezzarmou")
                .email("kamal@admin.com")
                .phone("000")
                .build();

        User adminUser = userService.mapUserDTOToUser(adminUserDTO, (new BCryptPasswordEncoder()).encode("admin00"));
        Set<Role> roles = new HashSet<>();
        Role adminRole = Role.ADMIN;
        roles.add(adminRole);
        adminUser.setRoles(roles);
        userService.addUser(adminUser);
    }


}
