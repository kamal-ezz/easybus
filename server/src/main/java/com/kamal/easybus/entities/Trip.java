package com.kamal.easybus.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trips")
public class Trip {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private Bus bus;
	private String departureCity;
	private String destinationCity;
	private Date date;
	private Time departureTime;
	private Time destinationTime;
	private double price;
	private Boolean isAvailable;

	@ElementCollection
	private List<Integer> availableSeats = new ArrayList<>();

	public Trip(Bus bus,
				String departureCity,
				String destinationCity,
				Date date,
				Time departureTime,
				Time destinationTime,
				double price,
				List<Integer> availableSeats,
				Boolean isAvailable) {
		this.bus = bus;
		this.departureCity = departureCity;
		this.destinationCity = destinationCity;
		this.date = date;
		this.departureTime = departureTime;
		this.destinationTime = destinationTime;
		this.price = price;
		this.availableSeats = availableSeats;
		this.isAvailable = isAvailable;
	}
}
