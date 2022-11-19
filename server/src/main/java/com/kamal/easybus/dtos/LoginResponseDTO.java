package com.kamal.easybus.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String token;
}
