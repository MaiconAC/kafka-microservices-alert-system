package com.sistemasdistribuidos.sensordatapublisher;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class SensorMessageDTO implements Serializable {
    private String sensorMessageCode;
    private int idSensor;
    private String message;
    private String dtCreation;

    public SensorMessageDTO(int idSensor, String message) {
        this.sensorMessageCode = UUID.randomUUID().toString();
        this.idSensor = idSensor;
        this.message = message;
        this.dtCreation = LocalDateTime.now().toString();
    }

    public String getSensorMessageCode() {
        return this.sensorMessageCode;
    }


    public int getIdSensor() {
        return this.idSensor;
    }


    public String getMessage() {
        return this.message;
    }


    public String getDtCreation() {
        return this.dtCreation;
    }
}

