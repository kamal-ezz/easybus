package com.kamal.easybus.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketData {
    private String lastName;
    private String firstName;
    private Double price;
    private String departure;
    private String destination;
    private String seatNumber;
}
