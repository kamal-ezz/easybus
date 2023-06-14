package com.kamal.easybus.services;

import com.kamal.easybus.entities.Trip;
import com.kamal.easybus.enums.Status;
import com.kamal.easybus.exceptions.ResourceNotFoundException;
import com.kamal.easybus.repos.ReservationRepo;
import com.kamal.easybus.repos.TripRepo;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    private TripRepo tripRepo;
    private ReservationRepo reservationRepo;
}
