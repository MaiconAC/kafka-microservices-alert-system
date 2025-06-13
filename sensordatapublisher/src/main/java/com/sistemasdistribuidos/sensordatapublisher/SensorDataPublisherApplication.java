package com.sistemasdistribuidos.sensordatapublisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
    public class SensorDataPublisherApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SensorDataPublisherApplication.class, args);

        System.out.println("Começando a enviar mensagens");

        SensorService sensorService = context.getBean(SensorService.class);

        sensorService.collectData();
	}

}
