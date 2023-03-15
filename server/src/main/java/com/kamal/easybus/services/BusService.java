package com.kamal.easybus.services;

import com.kamal.easybus.entities.Bus;
import com.kamal.easybus.exceptions.ResourceNotFoundException;
import com.kamal.easybus.repos.BusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusService {

    BusRepo busRepo;

    @Autowired
    public BusService(BusRepo busRepo) {
        this.busRepo = busRepo;
    }

    public Bus getBusByCompany(String company){
        return busRepo.findByCompany(company).orElseThrow(() -> new ResourceNotFoundException("Bus", "company", company));
    }


    public void addBus(Bus bus){
        busRepo.save(bus);
    }

    public void updateBus(Long id, Bus newBus){
        Bus _bus = busRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bus", "id", id));
        _bus.setCompany(newBus.getCompany());
        _bus.setLogo(newBus.getLogo());
        _bus.setEquipments(newBus.getEquipments());
        busRepo.save(_bus);
    }

    public void deleteBus(Long id){
        Bus bus = busRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bus", "id", id));
        busRepo.delete(bus);
    }


}
