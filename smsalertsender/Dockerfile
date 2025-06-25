FROM python:3.11-slim

WORKDIR /app

RUN pip install confluent-kafka requests phonenumbers

COPY smsAlertSender.py .

CMD ["python", "-u", "smsAlertSender.py"]

