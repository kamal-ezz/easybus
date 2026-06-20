package com.easybus.entities;

import com.easybus.enums.Equipment;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Trip {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String operator;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  private List<Equipment> equipments;

  private String fromCity;
  private String toCity;
  private Date date;
  private Time departure;
  private Time arrival;
  private double price;

  @ElementCollection private List<String> availableSeats;

  @OneToMany(mappedBy = "trip")
  private List<Booking> bookings;
}
