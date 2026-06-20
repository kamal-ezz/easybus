package com.easybus.dtos;

public record TicketData(
    String lastName,
    String firstName,
    Double price,
    String departure,
    String destination,
    String seatNumber) {}
