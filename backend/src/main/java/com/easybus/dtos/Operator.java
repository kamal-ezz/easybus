package com.easybus.dtos;

import com.easybus.enums.Equipment;
import java.util.List;

public record Operator(String company, String logo, List<Equipment> equipments) {}
