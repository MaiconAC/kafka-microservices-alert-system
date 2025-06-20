package com.sistemasdistribuidos.sensordatapublisher;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class SensorMessageDTO implements Serializable {
    private String sensorMessageCode;
    private int idSensor;
    private float value;
    private String sensorType;
    private String[] regions;
    private String dtCreation;

    public SensorMessageDTO(int idSensor, float value, String sensorType, String[] regions) {
        this.sensorMessageCode = UUID.randomUUID().toString();
        this.idSensor = idSensor;
        this.value = value;
        this.sensorType = sensorType;
        this.regions = regions;
        this.dtCreation = LocalDateTime.now().toString();
    }

    public String getSensorMessageCode() {
        return this.sensorMessageCode;
    }


    public int getIdSensor() {
        return this.idSensor;
    }


    public float getValue() {
        return this.value;
    }


    public String getSensorType() {
        return this.sensorType;
    }

    public String[] getRegions() {
        return this.regions;
    }

    public String getDtCreation() {
        return this.dtCreation;
    }
}

