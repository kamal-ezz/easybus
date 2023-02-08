package com.kamal.easybus.entities;

import com.kamal.easybus.enums.Equipment;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String company;
    private String logo;

    @ElementCollection
    private List<Equipment> equipments;

}