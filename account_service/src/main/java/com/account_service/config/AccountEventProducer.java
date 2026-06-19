package com.account_service.config;


import com.account_service.domain.accounts.kafkaDtos.AccountCreatedEvent;
import com.account_service.utils.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventProducer {

    private  final KafkaTemplate<String, Object> kafkaTemplate;

    public  void sendAccountCreated(AccountCreatedEvent event){
           kafkaTemplate.send(
                   KafkaTopic.ACCOUNT_CREATED,
                   event.getAccountId(),
                   event
           );
    }

}
