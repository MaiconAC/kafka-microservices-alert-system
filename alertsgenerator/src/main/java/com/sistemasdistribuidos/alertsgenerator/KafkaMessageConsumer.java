package com.sistemasdistribuidos.alertsgenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    private static final String TOPIC = "sensores";

    @KafkaListener(topics = TOPIC)
    public void listen(String message) {
        System.out.println("Mensagem recebida: " + message);
    }
}

