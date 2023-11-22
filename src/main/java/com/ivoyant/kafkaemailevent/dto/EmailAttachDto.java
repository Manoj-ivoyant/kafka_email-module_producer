package com.ivoyant.kafkaemailevent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailAttachDto {
    @NotBlank
    @Email(message = "invalid email")
    private String fromEmail;
    @Email(message = "invalid email")
    private String toEmail;
    private String subject;
    private String body;
    private String attachment;
}
