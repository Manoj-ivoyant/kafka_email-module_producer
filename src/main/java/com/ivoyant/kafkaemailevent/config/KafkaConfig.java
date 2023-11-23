package com.ivoyant.kafkaemailevent.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topics.dynamic-topic1}")
    private String dynamicTopic1;

    @Value("${spring.kafka.topics.dynamic-topic2}")
    private String dynamicTopic2;

    @Value("${spring.kafka.topics.email-event}")
    private int partitions;
    @Bean
    public NewTopic createTopic() {
        return new NewTopic(dynamicTopic1, partitions, (short) 1);
    }

    @Bean
    public NewTopic createTopic2(){
        return new NewTopic(dynamicTopic2,partitions, (short) 1);
    }

}
