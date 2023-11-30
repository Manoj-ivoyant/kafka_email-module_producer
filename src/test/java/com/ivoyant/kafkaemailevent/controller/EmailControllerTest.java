package com.ivoyant.kafkaemailevent.controller;

import com.ivoyant.kafkaemailevent.dto.EmailDto;
import com.ivoyant.kafkaemailevent.service.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class EmailControllerTest {
    @Mock
    private EmailService emailService;
    @InjectMocks
    private EmailController emailController;

    @Test
    void testCreateEmailEventTest(){
        EmailDto emailDto=EmailDto.builder().fromEmail("manojgmail.com").toEmail("manojk@ivoyant.com").subject("Test")
                .body("hai here").build();
        ResponseEntity<String> stringResponseEntity=emailController.createEmailEvent(emailDto);
        String response= UUID.randomUUID().toString();
        Mockito.when(emailService.createEmailEvent(emailDto)).thenReturn(response);
        String s=emailService.createEmailEvent(emailDto);
        Assertions.assertEquals(response,s);
        Assertions.assertEquals(HttpStatus.OK,stringResponseEntity.getStatusCode());

    }
}
