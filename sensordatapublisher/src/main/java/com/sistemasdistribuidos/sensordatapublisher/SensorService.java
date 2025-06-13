package com.sistemasdistribuidos.sensordatapublisher;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class SensorService {
    @Value("${sensor.idSensor}")
    private int idSensor;

    @Autowired
    private SensorRequestPublisher sensorRequestPublisher;

    public void collectData() {
        try {
            while (true) {
                String message = "Mensagem de teste kafka";
                sensorRequestPublisher.sendMessage(
                    new SensorMessageDTO(idSensor, message)
                );

                Thread.sleep(5 * 1000);
            }
        } catch (JsonProcessingException e) {
            String errorMessage = "Ocorreu um erro na request: " + e.getMessage();
            System.out.println(errorMessage);
            return;
        } catch (InterruptedException e) {
            String errorMessage = "Ocorreu um erro na espera do sensor: " + e.getMessage();
            System.out.println(errorMessage);
            return;
        } catch (Exception e) {
            String errorMessage = "Ocorreu um erro ao coletar os dados do sensor: " + e.getMessage();
            System.out.println(errorMessage);
            return;
        }
    }
}
