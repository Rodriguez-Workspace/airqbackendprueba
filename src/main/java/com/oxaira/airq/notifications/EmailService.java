package com.oxaira.airq.notifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmailService {

    @Value("${spring.brevo.api.url:https://api.brevo.com/v3/smtp/email}")
    private String apiUrl;

    @Value("${spring.brevo.api.key}")
    private String apiKey;

    @Value("${spring.brevo.api.sender:b01efb001@smtp-brevo.com}")
    private String senderEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void sendTechWelcomeEmail(String toEmail, String techName, String tempPassword) {
        try {
            String htmlContent = """
                    <div style=\"font-family: Arial, sans-serif; background-color: #F8FAFC; padding: 24px;\">
                      <div style=\"max-width: 620px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 8px 24px rgba(15, 23, 42, 0.08);\">
                        <div style=\"background: linear-gradient(90deg, #2563EB 0%%, #1D4ED8 100%%); padding: 24px 32px; color: #ffffff;\">
                          <h2 style=\"margin: 0; font-size: 24px;\">Bienvenido a AirQ</h2>
                          <p style=\"margin: 8px 0 0; opacity: 0.95;\">Tu cuenta de técnico ha sido creada correctamente.</p>
                        </div>
                        <div style=\"padding: 32px; color: #0F172A;\">
                          <p style=\"margin: 0 0 12px; font-size: 16px;\">Hola <strong>%s</strong>,</p>
                          <p style=\"margin: 0 0 16px; font-size: 15px;\">Te damos la bienvenida a la plataforma AirQ. Para acceder, usa las siguientes credenciales:</p>
                          <div style=\"background: #EFF6FF; border: 1px solid #BFDBFE; border-radius: 10px; padding: 16px; margin: 16px 0;\">
                            <p style=\"margin: 0 0 6px; font-size: 13px; color: #1D4ED8; text-transform: uppercase; letter-spacing: 0.04em;\">Correo electrónico</p>
                            <p style=\"margin: 0 0 12px; font-size: 16px; font-weight: 700; color: #1E3A8A;\">%s</p>
                            <p style=\"margin: 0 0 6px; font-size: 13px; color: #1D4ED8; text-transform: uppercase; letter-spacing: 0.04em;\">Contraseña temporal</p>
                            <p style=\"margin: 0; font-size: 18px; font-weight: 700; color: #111827;\">%s</p>
                          </div>
                          <p style=\"margin: 0; font-size: 14px; color: #475569;\">Te recomendamos cambiar esta contraseña en el primer acceso.</p>
                        </div>
                      </div>
                    </div>
                    """.formatted(techName, toEmail, tempPassword);

            Map<String, Object> requestBody = Map.of(
                    "sender", Map.of("name", "AirQ System", "email", senderEmail),
                    "replyTo", Map.of("name", "AirQ System", "email", senderEmail),
                    "to", List.of(Map.of("email", toEmail, "name", techName)),
                    "subject", "Bienvenido a AirQ - Credenciales de acceso",
                    "htmlContent", htmlContent
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", apiKey.trim());
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "application/json");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            log.info("Email enviado exitosamente a {} vía Brevo API. Respuesta: {}", toEmail, response.getBody());

        } catch (Exception e) {
            log.error("Error al enviar el correo de bienvenida a {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo de bienvenida", e);
        }
    }
}
