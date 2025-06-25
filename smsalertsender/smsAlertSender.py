import time
import json
import requests
import phonenumbers
from urllib.parse import quote

from confluent_kafka import Consumer, KafkaException

'''Lista fixa de moradores'''
users = [
    {
        "nome": "Gabriel",
        "endereco": {"rua": "XV de novembro, 100", "bairro": "CENTRO"},
        "telefone": "(47) 997870021"
    },
    {
        "nome": "Luiza",
        "endereco": {"rua": "Humberto de campos, 300", "bairro": "VELHA"},
        "telefone": "(47) 98800-7869"
    },
    {
        "nome": "Maicon",
        "endereco": {"rua": "25 de Agosto, 50", "bairro": "FORTALEZA"},
        "telefone": "(47) 989289306"
    },
    {
        "nome": "Camile",
        "endereco": {"rua": "7 de setembro, 200", "bairro": "CENTRO"},
        "telefone": "(47) 999202941"
    }
]

conf = {
    'bootstrap.servers': 'kafka:29092',  # endereço do broker
    'group.id': '2',
    'auto.offset.reset': 'earliest',  # ou 'latest'
}

def enviar_sms_para_moradores(alerta):
    bairros_validos = [b.upper() for b in alerta["regions"]]
    mensagem = alerta["messageAlert"]

    if mensagem == None:
        return

    #textmessage_service = TextMessageService(API_KEY)

    for pessoa in users:
        bairro = pessoa["endereco"]["bairro"].upper()
        if bairro in bairros_validos:
            try:
                numero = phonenumbers.parse(pessoa["telefone"], "BR")

                if phonenumbers.is_valid_number(numero):
                    numero_formatado = phonenumbers.format_number(numero, phonenumbers.PhoneNumberFormat.E164)

                    numero_internacional = numero_formatado.replace("+", "").replace("9", "", 1) #PQ O CALLME BOT N RECEBE O +55 NA FRENTE DO TELEFONE

                    #https://api.callmebot.com/whatsapp.php?phone=554788007869&text=This+is+a+test&apikey=9416911 <---- formato da mensagem por url que o bot me enviou
                    url = f"https://api.callmebot.com/whatsapp.php?phone={numero_internacional}&text={quote(mensagem)}&apikey=8088332"

                    response = requests.get(url)
                    if "Message queued" in response.text:
                        print(f"mensagem está na fila para {pessoa['nome']} ({bairro}): {numero_internacional}")
                    else:
                        print(f"erro {pessoa['nome']} ({numero_internacional}): {response.text}")
                else:
                    print(f"Telefone inválido: {pessoa['telefone']} de {pessoa['nome']}")
            except phonenumbers.NumberParseException as e:
                print(f"Erro ao validar número de {pessoa['nome']}: {e}")


def connectKafkaConsumer():
    consumer = None
    for i in range(10):
        try:
            print(f"Iniciando conexão com servidor Kafka, tentativa {i+1}")

            # Cria consumidor e se inscreve no topico
            consumer = Consumer(conf)
            consumer.subscribe(['alertas'])

            # Testa a conexão
            consumer.list_topics(timeout=5)

            # Se nao deu erro ao testar a conexao, sai do loop
            print("Conectado no Kafka, aguardando mensagens...")
            return consumer

        except KafkaException as err:
            print("Erro de coneção no servidor do Kafka")
            time.sleep(5)

        except:
            print("Não foi possível conectar com o Kafka")
            time.sleep(5)



if __name__ == "__main__":
    consumer = connectKafkaConsumer()

    try:
        while True:
            msg = consumer.poll(1.0)  # espera até 1 segundo por nova mensagem
            if msg is None:
                continue
            if msg.error():
                print(f"Erro: {msg.error()}")
            else:
                decodedMsg = msg.value().decode('utf-8')
                print(f"Mensagem recebida: {decodedMsg}")
                alerta = json.loads(decodedMsg)
                enviar_sms_para_moradores(alerta)

    except Exception as err:
        print(f"Erro ao receber/processar mensagem do Kafka: {err}")
        exit(1)

    finally:
        consumer.close()
