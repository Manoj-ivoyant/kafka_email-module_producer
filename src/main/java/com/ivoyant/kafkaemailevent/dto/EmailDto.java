package com.ivoyant.kafkaemailevent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EmailDto {
    @NotBlank
    @Email(message = "invalid email")
    private String fromEmail;
    @Email(message = "invalid email")
    private String toEmail;
    private String subject;
    private String body;
}
