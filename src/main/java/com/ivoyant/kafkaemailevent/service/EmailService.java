package com.ivoyant.kafkaemailevent.service;

import com.ivoyant.kafkaemailevent.dto.EmailAttachDto;
import com.ivoyant.kafkaemailevent.dto.EmailDto;
import com.ivoyant.kafkaemailevent.entity.Email;
import com.ivoyant.kafkaemailevent.entity.EmailAttach;
import com.ivoyant.kafkaemailevent.repository.EmailAttachRepository;
import com.ivoyant.kafkaemailevent.repository.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    private final EmailRepository emailRepository;

    private final EmailAttachRepository emailAttachRepository;
    private final KafkaTemplate<String, Object> template;

    private Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public EmailService(EmailRepository emailRepository, EmailAttachRepository emailAttachRepository, KafkaTemplate<String, Object> template) {
        this.emailRepository = emailRepository;
        this.emailAttachRepository = emailAttachRepository;
        this.template = template;
    }

    public String createEmailEvent(EmailDto emailDto) {
        Email email = Email.builder().emailId(UUID.randomUUID().toString()).fromEmail(emailDto.getFromEmail())
                .toEmail(emailDto.getToEmail())
                .subject(emailDto.getSubject()).body(emailDto.getBody()).createdAt(Instant.now()).build();
        LOGGER.info("email created and saved to cassandra cluster {}", email.getEmailId());
        emailRepository.save(email);
        CompletableFuture<SendResult<String, Object>> future = template.send("email-event", emailDto);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                LOGGER.info("Sent message=[" + emailDto.toString() +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                LOGGER.error("Unable to send message=[" +
                        emailDto.toString() + "] due to : " + ex.getMessage());
            }
        });
        return email.getEmailId();
    }

    public String createEmailAttachmentEvent(EmailAttachDto emailAttachDto) {
        EmailAttach emailAttach = EmailAttach.builder().emailId(UUID.randomUUID().toString())
                .fromEmail(emailAttachDto.getFromEmail()).toEmail(emailAttachDto.getToEmail())
                .subject(emailAttachDto.getSubject()).body(emailAttachDto.getBody()).createdAt(Instant.now())
                .attachment(emailAttachDto.getAttachment()).build();
        LOGGER.info("email with attachment created and saved to cassandra cluster {}", emailAttach.getEmailId());
        emailAttachRepository.save(emailAttach);
        CompletableFuture<SendResult<String, Object>> future = template.send("email-attach-event", emailAttachDto);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                LOGGER.info("Sent message=[" + emailAttachDto.toString() +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                LOGGER.error("Unable to send message=[" +
                        emailAttachDto.toString() + "] due to : " + ex.getMessage());
            }
        });
        return emailAttach.getEmailId();
    }
}
