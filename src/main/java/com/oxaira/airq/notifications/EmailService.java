package com.oxaira.airq.notifications;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendTechWelcomeEmail(String toEmail, String techName, String tempPassword) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreply.oxaira@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Bienvenido a AirQ - Credenciales de acceso");

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

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar el correo de bienvenida", e);
        }
    }
}
