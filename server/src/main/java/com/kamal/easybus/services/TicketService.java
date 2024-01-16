package com.kamal.easybus.services;

import com.kamal.easybus.dtos.TicketData;
import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
@Slf4j
public class TicketService {
    public Resource generateTicket(TicketData ticketData) throws IOException, DocumentException {
        String htmlContent = String.format("""
                        <html lang="en">
                         <body>
                             <h1>Your E-Ticket</h1>
                             <div>
                                 Last Name : <strong>%s</strong>
                             </div>
                             <div>
                                 First Name : <strong>%s</strong>
                             </div>
                             <div>
                                 Amount : <strong>%s</strong>
                             </div>
                             <div>
                                 Departure City : <strong>%s</strong>
                             </div>
                             <div>
                                 Arrival City : <strong>%s</strong>
                             </div>
                             <div>
                                 Seat Number : <strong>%s</strong>
                             </div>
                         </body>
                        </html>
                         """, ticketData.getLastName(),
                ticketData.getFirstName(),
                ticketData.getPrice(),
                ticketData.getDeparture(),
                ticketData.getDestination(),
                ticketData.getSeatNumber());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        byte[] pdfBytes = outputStream.toByteArray();
        return new ByteArrayResource(pdfBytes);
    }
}
