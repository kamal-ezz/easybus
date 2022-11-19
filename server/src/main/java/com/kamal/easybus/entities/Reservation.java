package com.kamal.easybus.entities;

import com.kamal.easybus.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Trip trip;

	private Integer seatNumber;
	private String email;
	private String phone;
	private PaymentMethod paymentMethod;
}
