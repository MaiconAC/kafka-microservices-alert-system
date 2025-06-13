package com.sistemasdistribuidos.sensordatapublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SensorRequestPublisher {
    private static final String TOPIC = "sensores";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(SensorMessageDTO sensorMessage) throws JsonProcessingException {
        String orderAsMessage = objectMapper.writeValueAsString(sensorMessage);
        kafkaTemplate.send(TOPIC, orderAsMessage);
        return "Dados do sensor enviados";
    }
}
