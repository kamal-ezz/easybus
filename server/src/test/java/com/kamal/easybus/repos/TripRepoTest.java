package com.kamal.easybus.repos;

import com.kamal.easybus.entities.Bus;
import com.kamal.easybus.enums.Equipment;
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

    }

}
