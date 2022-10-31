package com.kamal.easybus.repo;

import com.kamal.easybus.model.Bus;
import com.kamal.easybus.model.Equipments;
import com.kamal.easybus.model.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TripRepoTest {

    @Autowired
    private TripRepo underTest;

    @Autowired
    private BusRepo busRepo;

    @Test
    void itShouldFindTrips(){

        List<Equipments> equips = Arrays.asList(new Equipments[]{Equipments.WIFI});
       /* Bus test = ;
        busRepo.save(test); */

        Trip trip = new Trip(new Bus("Test","",equips),
                    "Rabat",
                    "Marrakech",
                    Date.valueOf("2022-10-30"),
                    Time.valueOf("08:00:00"),
                    Time.valueOf("13:00:00"),
                    80,
                    Arrays.asList(new Integer[]{1}),
                    true
                );

        underTest.save(trip);

        Page<Trip> foundTrips = underTest.findTrips("Rabat", "Marrakech", Date.valueOf("2022-10-30"), PageRequest.of(0,2));

        assertThat(foundTrips).hasFieldOrPropertyWithValue("departureCity", "Rabat");
    }

}
