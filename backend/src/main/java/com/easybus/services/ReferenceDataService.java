package com.easybus.services;

import com.easybus.dtos.Operator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class ReferenceDataService {
  private final ObjectMapper objectMapper;
  private List<Operator> operatorsCache;

  public ReferenceDataService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public List<Operator> getOperators() {
    if (operatorsCache == null) {
      operatorsCache = readOperators();
    }
    return operatorsCache;
  }

  private List<Operator> readOperators() {
    try {
      return objectMapper.readValue(
          new ClassPathResource("data/bus-companies.json").getInputStream(),
          new TypeReference<List<Operator>>() {});
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to load reference operators data.", ex);
    }
  }
}
