package com.kamal.easybus;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

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
