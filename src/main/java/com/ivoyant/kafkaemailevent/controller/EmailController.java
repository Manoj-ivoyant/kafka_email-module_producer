package com.ivoyant.kafkaemailevent.controller;

import com.ivoyant.kafkaemailevent.dto.EmailAttachDto;
import com.ivoyant.kafkaemailevent.dto.EmailDto;
import com.ivoyant.kafkaemailevent.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> createEmailEvent(@Valid @RequestBody EmailDto emailDto) {
        return ResponseEntity.ok(emailService.createEmailEvent(emailDto));
    }

    @PostMapping("/attachment")
    public ResponseEntity<String> createEmailAttachmentEvent(@Valid @RequestBody EmailAttachDto emailAttachDto){
        return ResponseEntity.ok(emailService.createEmailAttachmentEvent(emailAttachDto));
    }

    @GetMapping("/message")
    public void sendMessage(@RequestParam String message){
        emailService.sendMessage(message);
    }


}
