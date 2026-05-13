package co.vivaeventos.notificationservice.controller;

import co.vivaeventos.notificationservice.model.NotificationRequest;
import co.vivaeventos.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/payment-confirmation")
    public ResponseEntity<Map<String, String>> sendPaymentConfirmation(@RequestBody NotificationRequest request) {
        log.info("Recibida solicitud de notificación de pago para orden: {}", request.getOrderNumber());
        
        notificationService.sendPaymentConfirmation(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "sent");
        response.put("message", "Notificación de pago enviada exitosamente");
        response.put("orderNumber", request.getOrderNumber());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/ticket")
    public ResponseEntity<Map<String, String>> sendTicket(@RequestBody TicketRequest request) {
        log.info("Recibida solicitud de envío de boleta para orden: {}", request.getOrderNumber());
        
        notificationService.sendTicketGenerated(
            request.getEmail(), 
            request.getOrderNumber(), 
            request.getQrCode()
        );
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "sent");
        response.put("message", "Boleta digital enviada exitosamente");
        
        return ResponseEntity.ok(response);
    }
    
    public static class TicketRequest {
        private String email;
        private String orderNumber;
        private String qrCode;
        
        // Getters y setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        public String getQrCode() { return qrCode; }
        public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    }
}