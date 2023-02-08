package com.kamal.easybus;

import com.kamal.easybus.dtos.BusDTO;
import com.kamal.easybus.dtos.TripDTO;
import com.kamal.easybus.dtos.UserDTO;
import com.kamal.easybus.entities.*;
import com.kamal.easybus.enums.Equipment;
import com.kamal.easybus.enums.Role;
import com.kamal.easybus.services.BusService;
import com.kamal.easybus.services.TripService;
import com.kamal.easybus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class EasybusApplication implements CommandLineRunner {

	BusService busService;
	TripService tripService;
	UserService userService;

	@Autowired
	public EasybusApplication(BusService busService, TripService tripService, UserService userService) {
		this.busService = busService;
		this.tripService = tripService;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(EasybusApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

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
				.date(Date.valueOf("2022-04-24"))
				.departureCity("Casablanca")
				.destinationCity("Marrakech")
				.departureTime(Time.valueOf("10:00:00"))
				.destinationTime(Time.valueOf("14:15:00"))
				.isAvailable(true)
				.availableSeats(List.of(10,11))
				.build();

		tripService.addTrip(trip1);

		TripDTO trip2 = TripDTO.builder()
				.busCompany(ghazala.getCompany())
				.busLogo(ghazala.getLogo())
				.busEquipments(ghazalaEquipment)
				.date(Date.valueOf("2022-04-24"))
				.departureCity("Casablanca")
				.destinationCity("Marrakech")
				.departureTime(Time.valueOf("15:00:00"))
				.destinationTime(Time.valueOf("19:00:00"))
				.isAvailable(true)
				.availableSeats(List.of(10,11))
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
