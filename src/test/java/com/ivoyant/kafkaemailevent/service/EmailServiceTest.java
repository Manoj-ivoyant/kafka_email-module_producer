package com.ivoyant.kafkaemailevent.service;

import com.ivoyant.kafkaemailevent.dto.EmailAttachDto;
import com.ivoyant.kafkaemailevent.dto.EmailDto;
import com.ivoyant.kafkaemailevent.entity.Email;
import com.ivoyant.kafkaemailevent.entity.EmailAttach;
import com.ivoyant.kafkaemailevent.repository.EmailAttachRepository;
import com.ivoyant.kafkaemailevent.repository.EmailRepository;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
 class EmailServiceTest {


    @InjectMocks
    private EmailService emailService;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private EmailAttachRepository emailAttachRepository;

    @Mock
    private KafkaTemplate<String, Object> template;

    @Value("${spring.kafka.topics.dynamic-topic1}")
    private String dynamicTopic1;

    @Value("${spring.kafka.topics.dynamic-topic2}")
    private String dynamicTopic2;

    @Test
     void testCreateEmailEventSuccess() {
        // given
        EmailDto emailDto = new EmailDto("from@test.com", "to@test.com", "subject", "body");
        String id=UUID.randomUUID().toString();
        Email email = Email.builder().emailId(id).fromEmail(emailDto.getFromEmail())
                .toEmail(emailDto.getToEmail())
                .subject(emailDto.getSubject()).body(emailDto.getBody()).createdAt(Instant.now()).build();
        SendResult<String, Object> sendResult = mock(SendResult.class);
        RecordMetadata recordMetadata = mock(RecordMetadata.class);
        when(recordMetadata.offset()).thenReturn(1L);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
        when(template.send(dynamicTopic1, emailDto)).thenReturn(future);
        when(emailRepository.save(email)).thenReturn(email);
      //  when(emailService.createEmailEvent(emailDto)).thenReturn(any());
        //emailService.createEmailEvent(emailDto);

        // then
       // assertEquals(email.getEmailId(), emailService.createEmailEvent(emailDto));
       Mockito.verify(emailRepository, times(1)).save(any());
        verify(template, times(1)).send(dynamicTopic1, emailDto);
    }

    @Test
     void testCreateEmailEventFailure() {
        // given
        EmailDto emailDto = new EmailDto("from@test.com", "to@test.com", "subject", "body");
        Email email = Email.builder().emailId(UUID.randomUUID().toString()).fromEmail(emailDto.getFromEmail())
                .toEmail(emailDto.getToEmail())
                .subject(emailDto.getSubject()).body(emailDto.getBody()).createdAt(Instant.now()).build();
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Some error"));
        when(template.send(dynamicTopic1, emailDto)).thenReturn(future);
        when(emailRepository.save(any())).thenReturn(email);
        // when
        String emailId = emailService.createEmailEvent(emailDto);

        // then
        assertEquals(email.getEmailId(), emailId);
        verify(emailRepository, times(1)).save(any());
        verify(template, times(1)).send(dynamicTopic1, emailDto);
    }

    @Test
    void testCreateEmailAttachmentEventSuccess() {
        // given
        EmailAttachDto emailAttachDto = new EmailAttachDto("from@test.com", "to@test.com", "subject", "body", "attachment");
        EmailAttach emailAttach = EmailAttach.builder().emailId(UUID.randomUUID().toString())
                .fromEmail(emailAttachDto.getFromEmail()).toEmail(emailAttachDto.getToEmail())
                .subject(emailAttachDto.getSubject()).body(emailAttachDto.getBody()).createdAt(Instant.now())
                .attachment(emailAttachDto.getAttachment()).build();
        SendResult<String, Object> sendResult = mock(SendResult.class);
        RecordMetadata recordMetadata = mock(RecordMetadata.class);
        when(recordMetadata.offset()).thenReturn(1L);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
        when(template.send(dynamicTopic2, emailAttachDto)).thenReturn(future);
        when(emailAttachRepository.save(any())).thenReturn(emailAttach);

        // when
        String emailId = emailService.createEmailAttachmentEvent(emailAttachDto);

        // then
        assertEquals(emailAttach.getEmailId(), emailId);
        verify(emailAttachRepository, times(1)).save(any());
        verify(template, times(1)).send(dynamicTopic2, emailAttachDto);
    }

    @Test
     void testCreateEmailAttachmentEventFailure() {
        // given
        EmailAttachDto emailAttachDto = new EmailAttachDto("from@test.com", "to@test.com", "subject", "body", "attachment");
        EmailAttach emailAttach = EmailAttach.builder().emailId(UUID.randomUUID().toString())
                .fromEmail(emailAttachDto.getFromEmail()).toEmail(emailAttachDto.getToEmail())
                .subject(emailAttachDto.getSubject()).body(emailAttachDto.getBody()).createdAt(Instant.now())
                .attachment(emailAttachDto.getAttachment()).build();
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Some error"));
        when(template.send(dynamicTopic2, emailAttachDto)).thenReturn(future);
        when(emailAttachRepository.save(any())).thenReturn(emailAttach);

        // when
        String emailId = emailService.createEmailAttachmentEvent(emailAttachDto);

        // then
        assertEquals(emailAttach.getEmailId(), emailId);
        verify(emailAttachRepository, times(1)).save(any());
        verify(template, times(1)).send(dynamicTopic2, emailAttachDto);
    }
}
