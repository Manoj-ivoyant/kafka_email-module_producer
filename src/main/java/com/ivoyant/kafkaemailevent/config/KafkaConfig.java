package com.ivoyant.kafkaemailevent.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic createTopic() {
        return new NewTopic("email-event", 3, (short) 1);
    }

    @Bean
    public NewTopic createTopic2(){
        return new NewTopic("email-attach-event",3, (short) 1);
    }

}
