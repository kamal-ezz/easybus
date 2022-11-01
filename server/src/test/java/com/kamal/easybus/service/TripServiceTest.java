package com.kamal.easybus.service;

import com.kamal.easybus.repo.TripRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @Mock
    private TripRepo tripRepo;
    private TripService underTest;


    @BeforeEach
    void setUp() {
        underTest = new TripService(tripRepo);
    }

    @Test
    void canGetAllTrips(){
        underTest.getAllTrips();
        verify(tripRepo).findAll();
    }

}
