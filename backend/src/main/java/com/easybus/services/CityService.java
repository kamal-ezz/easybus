package com.easybus.services;

import com.easybus.dtos.City;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CityService {
  private final ObjectMapper objectMapper;
  private List<City> citiesCache;

  public CityService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public List<City> getCities() {
    if (citiesCache == null) {
      citiesCache = loadCities();
    }
    return citiesCache;
  }

  private List<City> loadCities() {
    try {
      return objectMapper.readValue(
          new ClassPathResource("data/cities.json").getInputStream(),
          new TypeReference<List<City>>() {});
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to load cities data.", ex);
    }
  }
}
