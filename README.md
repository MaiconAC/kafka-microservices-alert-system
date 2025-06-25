# Sistema de Alertas Kafka
Sistema de alertas para Defesa Civil construído utilizando arquitetura de microsserviços e comunicação por mensageria Kafka.

## Como inicializar:
Requisitos:
- Docker
- Docker-compose
- JDK 24
- Projeto clonado/instalado

### Gerando as imagens
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
