package com.kamal.easybus.dtos;

import com.kamal.easybus.enums.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripDTO {
    private Long id;
    private String busCompany;
    private String busLogo;
    private List<Equipment> busEquipments;
    private String departureCity;
    private String destinationCity;
    private Date date;
    private Time departureTime;
    private Time destinationTime;
    private double price;
    private List<Integer> availableSeats;
}
