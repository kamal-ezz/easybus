package com.kamal.easybus.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "buses")
@NoArgsConstructor
@Getter
@Setter
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String logo;

    @ElementCollection
    private List<Equipments> equipments;

    public Bus(String company, String logo, List<Equipments> equipments) {
        this.company = company;
        this.logo = logo;
        this.equipments = equipments;
    }
}