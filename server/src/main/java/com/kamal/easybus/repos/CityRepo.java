package com.kamal.easybus.repos;

import com.kamal.easybus.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepo extends JpaRepository<City, Long> {
}
