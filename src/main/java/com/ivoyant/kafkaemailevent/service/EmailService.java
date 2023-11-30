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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final JmsTemplate pubJmsTemplate;
    private final EmailAttachRepository emailAttachRepository;
    private final KafkaTemplate<String, Object> template;
    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    @Value("${spring.kafka.topics.dynamic-topic1}")
    private String dynamicTopic1;
    @Value("${spring.kafka.topics.dynamic-topic2}")
    private String dynamicTopic2;
    @Value("${spring.jms.queue-name}")
    private String queueName;

    @Autowired
    public EmailService(EmailRepository emailRepository, EmailAttachRepository emailAttachRepository,
                        KafkaTemplate<String, Object> template, JmsTemplate pubJmsTemplate) {
        this.emailRepository = emailRepository;
        this.emailAttachRepository = emailAttachRepository;
        this.pubJmsTemplate = pubJmsTemplate;
        this.template = template;
    }

    public String createEmailEvent(EmailDto emailDto) {
        Email email = Email.builder().emailId(UUID.randomUUID().toString()).fromEmail(emailDto.getFromEmail())
                .toEmail(emailDto.getToEmail())
                .subject(emailDto.getSubject()).body(emailDto.getBody()).createdAt(Instant.now()).build();
        LOGGER.info("email created and saved to cassandra cluster {}", email.getEmailId());
        emailRepository.save(email);
        CompletableFuture<SendResult<String, Object>> future = template.send(dynamicTopic1, emailDto);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                LOGGER.info("Sent message=[ {} ] with offset=[ {} ]", emailDto,
                        result.getRecordMetadata().offset());
            } else {
                LOGGER.error("Unable to send message=[ {} ] due to :  {}", emailDto, ex.getMessage());
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
        CompletableFuture<SendResult<String, Object>> future = template.send(dynamicTopic2, emailAttachDto);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                LOGGER.info("Sent message=[ {} ] with offset=[ {} ]", emailAttachDto,
                        result.getRecordMetadata().offset());
            } else {
                LOGGER.error("Unable to send message=[ {} ] due to :  {}", emailAttachDto, ex.getMessage());
            }
        });
        return emailAttach.getEmailId();
    }

    public void sendMessage(String message) {
        LOGGER.info("message has been added to queue {}", message);
        pubJmsTemplate.convertAndSend(queueName, message);
    }

}

