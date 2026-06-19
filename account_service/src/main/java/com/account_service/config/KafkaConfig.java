package com.account_service.config;

import com.account_service.utils.KafkaTopic;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic accountCreatedTopic(){
          return  TopicBuilder
                  .name(KafkaTopic.ACCOUNT_CREATED)
                  .partitions(1)
                  .replicas(1)
                  .build();
    }
}

