package com.sistemasdistribuidos.alertsgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaMessageProducer {
    private static final String TOPIC = "alertas";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(AlertMessageDTO alertMessage) throws JsonProcessingException {
        String orderAsMessage = objectMapper.writeValueAsString(alertMessage);
        kafkaTemplate.send(TOPIC, orderAsMessage);
        return "Alerta enviado";
    }
}

