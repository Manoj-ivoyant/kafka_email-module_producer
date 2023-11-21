package com.ivoyant.kafkaemailevent.entity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.time.Instant;

@Data
@Builder
@Table
public class Email {
    @PrimaryKey
    private String emailId;
    @Indexed
    private String fromEmail;
    @Indexed
    private String toEmail;
    private String subject;
    private String body;
    private Instant createdAt;
}
