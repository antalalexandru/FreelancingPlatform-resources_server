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
    private String defaultActivationMessage;
    @Value("${spring.mail.default-subject}")
    private String defaultActivationSubject;
    @Value("${reset.default-message}")
    private String defaultResetMessage;
    @Value("${reset.default-subject}")
    private String defaultResetSubject;

    public JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendActivationEmail(String to, Long userId, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        String body = defaultActivationMessage + " http://localhost:8080/activate_account?userId=" + userId + "&token=" + token;
        message.setTo(to);
        message.setSubject(defaultActivationSubject);
        message.setText(body);
        emailSender.send(message);
    }

    @Async
    public void sendResetEmail(String to, Long userId, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        String body = defaultResetMessage + " http://localhost:8080/reset_account?userId=" + userId + "&token=" + token;
        message.setTo(to);
        message.setSubject(defaultResetSubject);
        message.setText(body);
        emailSender.send(message);
    }
}