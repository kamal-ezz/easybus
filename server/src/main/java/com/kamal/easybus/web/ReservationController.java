package com.kamal.easybus.web;

import com.kamal.easybus.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/paypal")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping(value = "/reserve/{id}")
    public Map<String, Object> reserveTrip(@RequestParam("sum") String sum, @PathVariable("id") Long id){
        return reservationService.createPayment(sum);
    }

    @PostMapping(value = "/accept/{id}")
    public Map<String, Object> acceptReservation(HttpServletRequest request, @PathVariable("id") Long id){
        reservationService.acceptReservation(id);
        return reservationService.completePayment(request);
    }
}
