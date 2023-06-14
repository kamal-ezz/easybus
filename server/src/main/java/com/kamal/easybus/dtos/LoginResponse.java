package com.kamal.easybus.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String fullName;
    private String email;
    private String phone;
}
