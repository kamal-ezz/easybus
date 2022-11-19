package com.kamal.easybus.repos;

import com.kamal.easybus.entities.Bus;
import com.kamal.easybus.enums.Equipments;
import com.kamal.easybus.entities.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@DataJpaTest
public class TripRepoTest {

    @Autowired
    private TripRepo underTest;

    @Autowired
    private BusRepo busRepo;

    @Test
    void itShouldFindTrips(){

        List<Equipments> equips = Arrays.asList(new Equipments[]{Equipments.WIFI});

        Bus bus = new Bus("Test","",equips);
        busRepo.save(bus);

        Trip trip = new Trip(bus,
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

        //assertThat(foundTrips).isNotNull();

        assertThat(foundTrips.stream().count()).isEqualTo(1);
    }

}
