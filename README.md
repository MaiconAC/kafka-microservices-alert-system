# Sistema de Alertas Kafka
Sistema de alertas para Defesa Civil construído utilizando arquitetura de microsserviços e comunicação por mensageria Kafka.

## Como inicializar:
Requisitos:
- Docker
- Docker-compose
- JDK 24
- Projeto clonado/instalado

No diretório do projeto, rodar o comando `docker-compose up -d` para subir o container do Kafka.
OBS: ter certeza que a porta 9092 não esteja sendo utilizada

Numa IDE, inicializar o projeto SensorDataPublisher para começar a enviar mensagens para o Kafka. Para utilizar mais de um sensor, atualizar o **idSensor** no arquivo **application.properties**

Para configurar o Kafka, apenas é necessário criar o tópico, rodando esse comando dentro do bash do container do Kafka, no Docker

`/usr/bin/kafka-topics --create --topic sensores --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1`
