package com.sistemasdistribuidos.sensordatapublisher;

import java.time.Duration;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class SensorService {
    @Value("${sensor.idSensor}")
    private int idSensor;

    @Value("${sensor.tipo}")
    private String sensorType;

    @Value("${sensor.regioes}")
    private String[] sensorRegions;

    @Autowired
    private SensorRequestPublisher sensorRequestPublisher;

    private final Random randGenerator = new Random();

    // Funcao que vai entrar em loop e enviar dados de sensores para o kafka
    public void collectData() {
        try {
            while (true) {
                sensorRequestPublisher.sendMessage(simulateSensorData());
                Thread.sleep(10 * 1000);
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

    // Gera o dado de sensor de acordo com o tipo escolhido
    private SensorMessageDTO simulateSensorData() {
        switch(sensorType) {
            case "CHUVA":
                return generateRainData();
            case "NIVEL_RIO":
                return generateRiverData();
            case "TEMPERATURA":
                return generateTemperatureData();
            default:
                return new SensorMessageDTO(idSensor, 0, "CHUVA", new String[]{"CENTRO"});
        }
    }

    // Dados de chuva forte ~ muito forte, retirados do site AlertaBlu
    private SensorMessageDTO generateRainData() {
        float minValue = 10f;
        float maxValue = 60f;

        float value = randGenerator.nextFloat(maxValue - minValue) + minValue;

        return new SensorMessageDTO(idSensor, value, sensorType, sensorRegions);
    }


    // Dados de chuva forte ~ muito forte, retirados do site AlertaBlu
    private SensorMessageDTO generateRiverData() {
        float minValue = 0.5f;
        float maxValue = 15f;

        float value = randGenerator.nextFloat(maxValue - minValue) + minValue;

        return new SensorMessageDTO(idSensor, value, sensorType, sensorRegions);
    }


    private SensorMessageDTO generateTemperatureData() {
        int minValue = 2;
        int maxValue = 45;

        float value = (float) randGenerator.nextInt(maxValue - minValue) + minValue;

        return new SensorMessageDTO(idSensor, value, sensorType, sensorRegions);
    }
}
