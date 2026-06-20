package com.easybus.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.easybus.dtos.City;
import com.easybus.services.CityService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CityControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CityService cityService;

  @Test
  void getCities_returns200WithCitiesList() throws Exception {
    List<City> cities =
        List.of(
            new City("casablanca", "Casablanca"),
            new City("rabat", "Rabat"),
            new City("marrakech", "Marrakech"));
    when(cityService.getCities()).thenReturn(cities);

    mockMvc
        .perform(get("/api/cities"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].id").value("casablanca"))
        .andExpect(jsonPath("$[0].name").value("Casablanca"))
        .andExpect(jsonPath("$[1].id").value("rabat"))
        .andExpect(jsonPath("$[2].id").value("marrakech"));
  }

  @Test
  void getCities_withEmptyList_returnsEmptyArray() throws Exception {
    when(cityService.getCities()).thenReturn(List.of());

    mockMvc
        .perform(get("/api/cities"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }
}
