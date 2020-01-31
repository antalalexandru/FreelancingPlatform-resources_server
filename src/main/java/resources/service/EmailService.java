package resources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    @Value("${spring.mail.default-message}")
    private String defaultMessage;
    @Value("${spring.mail.default-subject}")
    private String defaultSubject;
    public JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendActivationEmail(String to, Long userId, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        String body = defaultMessage + " userID = " + userId + " token = " + token;
        message.setTo(to);
        message.setSubject(defaultSubject);
        message.setText(body);
        emailSender.send(message);

    }
}