package com.easybus.controllers;

import com.easybus.dtos.Operator;
import com.easybus.services.ReferenceDataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meta")
@RequiredArgsConstructor
public class ReferenceDataController {

  private final ReferenceDataService referenceDataService;

  @GetMapping("/operators")
  public ResponseEntity<List<Operator>> getOperators() {
    return new ResponseEntity<>(referenceDataService.getOperators(), HttpStatus.OK);
  }
}
