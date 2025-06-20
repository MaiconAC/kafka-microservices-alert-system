package com.sistemasdistribuidos.alertsgenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class AlertMessageDTO implements Serializable {
    private String messageCode;
    private String sensorType;
    private String messageAlert;
    private String[] regions;
    private String dtCreation;

    public AlertMessageDTO() {}

    public AlertMessageDTO(String sensorType, String messageAlert, String[] regions) {
        this.messageCode = UUID.randomUUID().toString();
        this.sensorType = sensorType;
        this.messageAlert = messageAlert;
        this.regions = regions;
        this.dtCreation = LocalDateTime.now().toString();
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public String getSensorType() {
        return this.sensorType;
    }

    public String getMessageAlert() {
        return this.messageAlert;
    }

    public String[] getRegions() {
        return this.regions;
    }

    public String getDtCreation() {
        return this.dtCreation;
    }
}
