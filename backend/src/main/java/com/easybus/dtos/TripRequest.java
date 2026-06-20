package com.easybus.dtos;

import com.easybus.enums.Equipment;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public record TripRequest(
    String operator,
    List<Equipment> equipments,
    String fromCity,
    String toCity,
    Date date,
    Time departure,
    Time arrival,
    double price,
    List<String> availableSeats) {}
