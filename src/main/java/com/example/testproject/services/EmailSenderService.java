package com.example.testproject.services;

import com.example.testproject.models.entities.User;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    private void sendEmail(String toEmail,
                          String subject,
                          String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("zotov.bogdan2019@yandex.ru");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("Mail send succesfully");
    }

    public void sendVerifyCode(User user, String token){
        String subject = "Confirm email";
        String body = "http://localhost:8070/verify?token=" + token;
        this.sendEmail(user.getEmail(), subject, body);
    }
}
