package com.kamal.easybus.entities;

import com.kamal.easybus.enums.PaymentMethod;
import com.kamal.easybus.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "trip_id")
	private Trip trip;

	private Integer seatNumber;
	private String email;
	private String phone;
	private PaymentMethod paymentMethod;
}
