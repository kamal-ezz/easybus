package com.kamal.easybus.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.*;

import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
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
	@OneToMany(mappedBy = "trip")
	private List<Seat> availableSeats;
	@OneToMany(mappedBy = "trip")
	private List<Reservation> reservations;

}
