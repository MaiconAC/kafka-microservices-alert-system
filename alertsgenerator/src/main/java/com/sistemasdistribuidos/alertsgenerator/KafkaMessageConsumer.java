package com.sistemasdistribuidos.alertsgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaMessageConsumer {

    private static final String TOPIC = "sensores";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AlertsGeneratorService alertsGeneratorService;

    @KafkaListener(topics = TOPIC)
    public void listen(String message) {
        try {
            System.out.println("Mensagem recebida: " + message);
            SensorMessageDTO sensorMessage = objectMapper.readValue(message, SensorMessageDTO.class);
            alertsGeneratorService.storeSensorDataByType(sensorMessage);
        } catch (Exception e) {
            System.out.println("Erro escutando as mensagens: " + e.getMessage());
        }
    }
}

