//package com.example.testproject.controllers;
//
//import com.example.testproject.services.EmailSenderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class TestingConrtroller {
//
//    private final EmailSenderService emailSenderService;
//
//    @GetMapping("/send")
//    public void sendMail(@RequestParam String email, @RequestParam String text, @RequestParam String subject){
//        emailSenderService.sendEmail(email, subject, text);
//    }
//
//}
