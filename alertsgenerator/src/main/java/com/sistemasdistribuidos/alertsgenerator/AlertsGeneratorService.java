package com.sistemasdistribuidos.alertsgenerator;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class AlertsGeneratorService {
    private ArrayList<SensorMessageDTO> recentRainMessages = new ArrayList<>();
    private ArrayList<SensorMessageDTO> recentRiverMessages = new ArrayList<>();

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    public void storeSensorDataByType(SensorMessageDTO sensorMessage) {
        switch (sensorMessage.getSensorType()) {
            case "CHUVA":
                validateRainData(sensorMessage);
                break;
            case "NIVEL_RIO":
                validateRiverData(sensorMessage);
                break;
            default:
                System.out.println("Tipo de aviso não identificado");

        }
    }

    private void validateRainData(SensorMessageDTO sensorMessage) {
        recentRainMessages.add(sensorMessage);

        if (sensorMessage.getValue() > 25f && sensorMessage.getValue() <= 50f) {
            String alert = "Alerta de chuva forte!";
            sendAlert(alert, sensorMessage);

        } else if (sensorMessage.getValue() > 50f) {
            String alert = "Alerta de chuva torrencial! Favor buscar abrigo.";
            sendAlert(alert, sensorMessage);
        }
    }

    private void validateRiverData(SensorMessageDTO sensorMessage) {
        recentRiverMessages.add(sensorMessage);

        if (sensorMessage.getValue() >= 4f && sensorMessage.getValue() < 6f) {
            String alert = "Atenção: Nível do rio em " + sensorMessage.getValue();
            sendAlert(alert, sensorMessage);

        } else if (sensorMessage.getValue() >= 6f && sensorMessage.getValue() < 8f) {
            String alert = "Alerta: Nível do rio em " + sensorMessage.getValue();
            sendAlert(alert, sensorMessage);

        } else if (sensorMessage.getValue() >= 8f) {
            String alert = "ALERTA MÁXIMO: Nível do rio em " + sensorMessage.getValue();
            sendAlert(alert, sensorMessage);
        }
    }

    private void sendAlert(String alert, SensorMessageDTO sensorMessage) {
        try {
            kafkaMessageProducer.sendMessage(new AlertMessageDTO(
                sensorMessage.getSensorType(),
                alert,
                sensorMessage.getRegions()
            ));

        } catch (JsonProcessingException e) {
            String errorMessage = "Ocorreu um erro no processamento do JSON: " + e.getMessage();
            System.out.println(errorMessage);
            return;

        } catch (Exception e) {
            String errorMessage = "Ocorreu um erro ao enviar o alerta: " + e.getMessage();
            System.out.println(errorMessage);
            return;
        }
    }
}
