package com.sistemasdistribuidos.alertsgenerator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class AlertsGeneratorService {
    // Listas dos ultimos alertas enviados de cada sensor
    private Map<Integer, SensorMessageDTO> lastRainAlertSentBySensorIdMap = new HashMap<>();
    private Map<Integer, SensorMessageDTO> lastTemperatureAlertSentBySensorIdMap = new HashMap<>();
    private Map<Integer, SensorMessageDTO> lastRiverAlertSentBySensorIdMap = new HashMap<>();

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    public void generateWeatherAlertByType(SensorMessageDTO sensorMessage) {
        int alertLevel = 0;
        String alert = null;
        SensorMessageDTO lastSentAlert = null;

        switch (sensorMessage.getSensorType()) {
            case "CHUVA":
                alertLevel = validateRainAlertLevel(sensorMessage.getValue());

                alert = getRainAlertMessage(alertLevel, sensorMessage.getValue());
                lastSentAlert = lastRainAlertSentBySensorIdMap.get(sensorMessage.getIdSensor());

                // Se precisar enviar o alerta, salva a mensagem para comparacoes futuras
                // e chama a funcao de envio
                if (shouldSendAlert(sensorMessage, lastSentAlert, alertLevel)) {
                    lastRainAlertSentBySensorIdMap.put(sensorMessage.getIdSensor(), sensorMessage);
                    sendAlert(alert, sensorMessage);
                }

                break;

            case "TEMPERATURA":
                alertLevel = validateTemperatureAlertLevel(sensorMessage.getValue());

                alert = getTemperatureAlertMessage(alertLevel, sensorMessage.getValue());
                lastSentAlert = lastTemperatureAlertSentBySensorIdMap.get(sensorMessage.getIdSensor());

                if (shouldSendAlert(sensorMessage, lastSentAlert, alertLevel)) {
                    lastTemperatureAlertSentBySensorIdMap.put(sensorMessage.getIdSensor(), sensorMessage);
                    sendAlert(alert, sensorMessage);
                }

                break;

            case "NIVEL_RIO":
                alertLevel = validateRiverAlertLevel(sensorMessage.getValue());

                alert = getRiverAlertMessage(alertLevel, sensorMessage.getValue());
                lastSentAlert = lastRiverAlertSentBySensorIdMap.get(sensorMessage.getIdSensor());

                if (shouldSendAlert(sensorMessage, lastSentAlert, alertLevel)) {
                    lastRiverAlertSentBySensorIdMap.put(sensorMessage.getIdSensor(), sensorMessage);
                    sendAlert(alert, sensorMessage);
                }

                break;

            default:
                System.out.println("Tipo de mensagem do sensor não identificado");
        }
    }

    // Realiza as validacoes para saber se precisa gerar o alerta
    private boolean shouldSendAlert(SensorMessageDTO sensorMessage, SensorMessageDTO lastSentAlert, int alertLevel) {
        // Valida se eh o primeiro alerta daquele sensor, precisa ser validado
        // primeiro pois se nao tiver alerta passado, as proxs validacoes nao
        // funcionam
        if (lastSentAlert == null) {
            return true;
        }

        // Valida se fazem 6 ou mais horas desde o ultimo alerta do sensor
        boolean isSixHoursAfterLastAlert = isSixHoursAfter(
            lastSentAlert.getDtCreation(),
            sensorMessage.getDtCreation()
        );

        // Valida se a gravidade do alerta eh pior
        boolean alertIsWorse = alertLevel > validateRainAlertLevel(lastSentAlert.getValue());

        if (isSixHoursAfterLastAlert || alertIsWorse) {
            return true;
        }

        return false;
    }

    // Valida o nivel da gravidade dos alertas de chuva
    private int validateRainAlertLevel(float value) {
        if (value > 25f && value <= 50f) {
            return 1;
        } else if (value > 50f) {
            return 2;
        } else {
            return 0;
        }
    }

    // Retorna a mensagem de alerta de acordo com o nivel do evento de chuva
    private String getRainAlertMessage(int alertLevel, float value) {
        switch (alertLevel) {
            case 1:
                return "Atenção: Chuva forte na sua região: " + value + "mm/h";
            case 2:
                return "Atenção: Chuva torrencial na sua região: " + value + "mm/h";
            default:
                return null;
        }
    }

    // Valida o nivel da gravidade dos alertas de temperatua
    private int validateTemperatureAlertLevel(float value) {
        if (value >= 38f) {
            return 1;
        } else if (value <= 5f) {
            return 2;
        } else {
            return 0;
        }
    }

    // Retorna a mensagem de alerta de acordo com o nivel do evento de temperatura
    private String getTemperatureAlertMessage(int alertLevel, float value) {
        switch (alertLevel) {
            case 1:
                return "Alerta de onda de calor: " + value + " °C";
            case 2:
                return "Alerta de frente fria: " + value + " °C";
            default:
                return null;
        }
    }

    // Valida o nivel da gravidade dos alertas do nivel do rio
    private int validateRiverAlertLevel(float value) {
        if (value >= 4f && value < 6f) {
            return 1;
        } else if (value >= 6f && value < 8f) {
            return 2;
        } else if (value >= 8f) {
            return 3;
        } else {
            return 0;
        }
    }

    // Retorna a mensagem de alerta de acordo com o nivel do evento de chuva
    private String getRiverAlertMessage(int alertLevel, float value) {
        switch (alertLevel) {
            case 1:
                return "Atenção: Nível do rio em " + value + "mm/h";
            case 2:
                return "Alerta: Nível do rio em " + value + "mm/h";
            case 3:
                return "ALERTA MÁXIMO: Nível do rio em " + value + "mm/h";
            default:
                return null;
        }
    }

    // Chama o producer do kafka para envar o alerta gerado
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

    // Valida se os datetimes tem seis horas de diferenca
    private boolean isSixHoursAfter(String oldDate, String newDate) {
        int newHours = LocalDateTime.parse(newDate).getHour();
        int oldHours = LocalDateTime.parse(oldDate).getHour();
        return (newHours - oldHours) >= 6;
    }
}
