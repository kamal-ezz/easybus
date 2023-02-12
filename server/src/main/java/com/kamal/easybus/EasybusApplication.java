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

	Seeder seeder;

	@Autowired
	public EasybusApplication(Seeder seeder) {
		this.seeder = seeder;
	}

	public static void main(String[] args) {
		SpringApplication.run(EasybusApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		seeder.seed();
	}
}
