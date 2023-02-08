package com.kamal.easybus.dtos;


import com.kamal.easybus.enums.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusDTO {
    private String company;
    private String logo;
    private List<Equipment> equipments;
}
