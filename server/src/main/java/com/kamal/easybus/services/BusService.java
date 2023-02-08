package com.kamal.easybus.services;

import com.kamal.easybus.dtos.BusDTO;
import com.kamal.easybus.entities.Bus;
import com.kamal.easybus.enums.Equipment;
import com.kamal.easybus.exceptions.ResourceNotFoundException;
import com.kamal.easybus.repos.BusRepo;
import com.kamal.easybus.repos.EquipmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BusService {

    BusRepo busRepo;
    EquipmentRepo equipmentRepo;

    @Autowired
    public BusService(BusRepo busRepo, EquipmentRepo equipmentRepo) {
        this.busRepo = busRepo;
        this.equipmentRepo = equipmentRepo;
    }

    public Bus getBusByCompany(String company){
        return busRepo.findByCompany(company).orElseThrow(() -> new ResourceNotFoundException("Bus", "company", company));
    }


    public void addBus(BusDTO bus){
        busRepo.save(mapBusDTOToBus(bus));
    }

    public void updateBus(Long id, BusDTO bus){
        Bus _bus = busRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bus", "id", id));
        _bus.setCompany(bus.getCompany());
        _bus.setLogo(bus.getLogo());
        _bus.setEquipments(bus.getEquipments());
        busRepo.save(_bus);
    }

    public void deleteBus(Long id){
        Bus bus = busRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bus", "id", id));
        busRepo.delete(bus);
    }

    public void addEquipment(Long id, Equipment equipments){
        Bus bus = busRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bus", "id", id));
    }

    public Bus mapBusDTOToBus(BusDTO busDTO){

        /*busDTO.getEquipments().forEach(e -> {
            equipmentRepo.save(e);
        });*/

        return Bus.builder()
                .company(busDTO.getCompany())
                .logo(busDTO.getLogo())
                .equipments(busDTO.getEquipments())
                .build();
    }


}
