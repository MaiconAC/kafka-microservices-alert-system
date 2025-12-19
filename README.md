# Kafka Alert System
Alert system for Civil Defense built using microservices architecture and Kafka messaging communication.

The project is divided into 4 parts:
- Kafka: Queue manager and event streaming, responsible for organized communication between services.
- sensor-data-publisher: Service that simulates sensor data capture, generates a random value within a configured limit and type, packages it, and sends it to a queue.
- alerts-generator: Captures climate event data sent by the sensor and analyzes its value. If it falls into any risk category, it packages an alert message and sends it to the next service.
- sms-alert-sender: Python script that captures alert messages from Kafka and sends them to the Callmebot API, which sends an SMS to the selected user.

## How to Initialize:
Requirements:
- Docker
- Docker-compose
- JDK 24
- Project cloned/installed

### Generating Images
To run all services within docker-compose, you need to generate an image for each service.

This example will be done for the `sensordatapublisher` service, but should be repeated for the others as well.

First, we should verify that an executable was created for the service in the target folder. It should look like `sensordatapublisher-0.0.1-SNAPSHOT.jar`. The executable is normally created when running the program, but can also be created by Maven with mvn clean package. The executable is necessary because the Dockerfile creates the image based on it.

Once you have the executable, still within the service folder, we can create the Docker image with the command `docker build -t sistemasdistribuidos/sensor-data-publisher:latest ..`

After creating the images for all services, confirm that docker-compose is not running and execute `docker-compose build --no-cache` to update with the new images.

In the project directory, run the command `docker-compose up -d` to start the Kafka container.

To follow the logs of a program: `docker logs alerts-generator`.

## Optional
Kafka should create the topic automatically, but if you need to configure it manually, run this command inside the Kafka container's bash in Docker:
`/usr/bin/kafka-topics --create --topic sensores --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1`

---------------------------------------------------------------------------------------------------------------------------

# Sistema de Alertas Kafka
Sistema de alertas para Defesa Civil construído utilizando arquitetura de microsserviços e comunicação por mensageria Kafka.

O projeto é dividido em 4 partes:
- Kafka: Gerenciador de filas e streaming de eventos, responsável pela comunicação organizada entre os serviços.
- sensor-data-publisher: serviço que simula a captura de dados de um sensor, gera um valor aleatório dentro de um limite configurado e de tipo configurado, empacota e envia numa fila.
- alerts-generator: captura os dados de eventos climáticos enviados pelo sensor e analisa seu valor, caso cair em algum caso de risco, empacota uma mensagem de alerta e envia para o proximo serviço.
- sms-alert-sender: script python que captura as mensagens de alerta do kafka e envia na API do Callmebot, que envia um SMS para o usuário selecionado

## Como inicializar:
Requisitos:
- Docker
- Docker-compose
- JDK 24
- Projeto clonado/instalado

## Gerando as imagens
Para conseguir rodar todos os serviços dentro do docker-compose, é preciso gerar uma imagem para cada serviço.

Esse exemplo vai ser feito pro serviço `sensordatapublisher`, mas deve ser repetido com os outros também.

Primeiro, devemos verificar se foi criado um executável do serviço na pasta target, ele deve se parecer com `sensordatapublisher-0.0.1-SNAPSHOT.jar`. O executável normalmente é criado ao executar o programa, mas atmbém pode ser criado pelo Maven com `mvn clean packages`. O executável é necessário pois o Dockerfile cria a imagem em cima dele.

Tendo o executável, ainda dentro da pasta do serviço podemos criar a imagem do docker com o comando `docker build -t sistemasdistribuidos/sensor-data-publisher:latest .`.

Após criar as imagens de todos os serviços, confirme que o docker-compose não está executando e rode `docker-compose build --no-cache` para atualizar com as novas imagens.

No diretório do projeto, rodar o comando `docker-compose up -d` para subir o container do Kafka.

Para acompanhar os logs de um programa, `docker logs alerts-generator`.

## Opcional
O Kafka deverá criar o tópico automaticamente, mas caso precisar configurar manualmente, rodando esse comando dentro do bash do container do Kafka, no Docker

`/usr/bin/kafka-topics --create --topic sensores --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1`
