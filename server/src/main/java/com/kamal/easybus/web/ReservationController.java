package com.kamal.easybus.web;

import com.kamal.easybus.dtos.TicketData;
import com.kamal.easybus.services.ReservationService;
import com.kamal.easybus.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reserve")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final TicketService ticketService;
    @PostMapping
    public ResponseEntity<Resource> reserve(@RequestBody TicketData ticketData) throws Exception {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ticket.pdf\"")
                .body(ticketService.generateTicket(ticketData));
    }
}
