package com.kamal.easybus;

import com.kamal.easybus.model.*;
import com.kamal.easybus.repo.BusRepo;
import com.kamal.easybus.repo.RoleRepo;
import com.kamal.easybus.repo.TripRepo;
import com.kamal.easybus.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

@SpringBootApplication
public class EasybusApplication implements CommandLineRunner {

	@Autowired
	BusRepo busRepo;

	@Autowired
	TripRepo tripRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(EasybusApplication.class, args);
	}
	
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}


	@Override
	public void run(String... args) throws Exception {

		//Buses
		List<Equipments> globusEquipments = Arrays.asList(new Equipments[]{Equipments.AIR_CONDITIONER, Equipments.LAMP, Equipments.TV, Equipments.HIGHWAY});
		Bus globus = new Bus("Globus","",globusEquipments);
		busRepo.save(globus);

		List<Equipments> ghazalaEquipments = Arrays.asList(new Equipments[]{Equipments.WIFI, Equipments.AIR_CONDITIONER, Equipments.LAMP, Equipments.TV, Equipments.HIGHWAY, Equipments.PHARMACY_BOX, Equipments.USB_CHARGER});
		Bus ghazala = new Bus("Ghazala","",ghazalaEquipments);
		busRepo.save(ghazala);

		//Trips
		Date date1 = Date.valueOf("2022-04-24");
		Time departureTime1 = Time.valueOf("10:00:00");
		Time destinationTime1 = Time.valueOf("14:15:00");
		List<Integer> availableSeats1 = Arrays.asList(new Integer[]{10,11});
		Trip trip1 = new Trip(globus,"Casablanca","Marrakech",date1, departureTime1, destinationTime1,60.0, availableSeats1, true);
		tripRepo.save(trip1);

		Date date2 = Date.valueOf("2022-04-24");
		Time departureTime2 = Time.valueOf("15:00:00");
		Time destinationTime2 = Time.valueOf("19:00:00");
		List<Integer> availableSeats2 = Arrays.asList(new Integer[]{10,11});
		Trip trip2 = new Trip(ghazala,"Casablanca","Marrakech",date2, departureTime2, destinationTime2,80.0, availableSeats2, true);
		tripRepo.save(trip2);

		//Roles
		Role admin = new Role(ERole.ROLE_ADMIN);
		Role user = new Role(ERole.ROLE_USER);
		roleRepo.save(admin);
		roleRepo.save(user);

		//Users
		User adminUser = new User("Kamal","Ezzarmou","kamal@admin.com",(new BCryptPasswordEncoder()).encode("admin00"),"000");
		Set<Role> roles = new HashSet<>();
		Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(adminRole);
		adminUser.setRoles(roles);
		userRepo.save(adminUser);
	}
}
