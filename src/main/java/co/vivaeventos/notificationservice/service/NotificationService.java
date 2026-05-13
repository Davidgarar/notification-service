package co.vivaeventos.notificationservice.service;

import co.vivaeventos.notificationservice.model.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPaymentConfirmation(NotificationRequest request) {
        log.info("========================================");
        log.info("📧 ENVIANDO NOTIFICACIÓN DE PAGO REAL");
        log.info("========================================");
        log.info("Para: {}", request.getTo());
        log.info("Asunto: {}", request.getSubject());
        log.info("Orden: {}", request.getOrderNumber());
        log.info("Estado: {}", request.getStatus());
        
        try {
            // Crear el correo
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(buildEmailBody(request));
            
            // Enviar correo
            mailSender.send(message);
            
            log.info("✅ CORREO REAL ENVIADO EXITOSAMENTE a: {}", request.getTo());
            log.info("========================================");
            
        } catch (Exception e) {
            log.error("❌ Error al enviar correo: {}", e.getMessage());
            throw new RuntimeException("No se pudo enviar la notificación: " + e.getMessage());
        }
    }
    
    private String buildEmailBody(NotificationRequest request) {
        return String.format("""
            ====================================
            ¡PAGO APROBADO!
            ====================================
            
            Número de orden: %s
            Estado: %s
            Mensaje: %s
            
            Gracias por tu compra.
            Tu boleta digital será enviada próximamente.
            
            ====================================
            VivaEventos - Tu plataforma de eventos
            ====================================
            """, 
            request.getOrderNumber(), 
            request.getStatus(), 
            request.getBody());
    }
    
    public void sendTicketGenerated(String email, String orderNumber, String qrCode) {
        log.info("========================================");
        log.info("🎫 ENVIANDO BOLETA DIGITAL");
        log.info("========================================");
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Tu boleta digital - Orden: " + orderNumber);
            message.setText(String.format("""
                ====================================
                ¡TU BOLETA DIGITAL!
                ====================================
                
                Orden: %s
                Código QR: %s
                
                Presenta este código en la entrada del evento.
                
                ¡Disfruta del evento!
                
                ====================================
                VivaEventos
                ====================================
                """, orderNumber, qrCode));
            
            mailSender.send(message);
            
            log.info("✅ BOLETA DIGITAL ENVIADA a: {}", email);
            log.info("========================================");
            
        } catch (Exception e) {
            log.error("❌ Error al enviar boleta: {}", e.getMessage());
        }
    }
}