
# RabbitMQ With Two Microservices

## Описание
Два микросервиса, которые взаимодействуют с помощью RabbitMQ, один является отправителем, второй получателем.

## Инструкция по запуску

1. **Склонируйте репозиторий** на компьютер с помощью команды:
    ```bash
    git clone https://github.com/r1ngoch1/RabbitMQWithTwoMicroServices.git
    ```

2. **Создайте в проекте файл `.env`**.

3. **Откройте файл `.env-example`**, скопируйте все в `.env`, и измените значения от пользователя БД на ваши данные. Данные от RabbitMQ обычно такие же, как в `.env-example`, но если у вас другие данные, то измените их.

4. Перейдите на каждый из сервисов по очереди и соберите их. Если вы находитесь в главной папке, пропишите следующие команды для сборки:
    ```bash
    cd .\ReceiverService
    mvn clean package -DskipTests
    cd ..
    cd .\SenderService
    mvn clean package -DskipTests
    cd ..
    ```

5. Дальше запустите **docker compose**. Для этого пропишите следующую команду:
    ```bash
    docker compose up --build
    ```

6. Если хотите завершить работу, используйте команду:
    ```bash
    docker compose down
    ```

## Endpoints:

- **http://localhost:8080/api/v1/publish**  
  Отправляет json сообщение в другой сервис.  
  Пример входных данных:
  ```json
  {
      "id": 100,
      "name": "tomato",
      "price": 5
  }
  ```

- **http://localhost:8081/api/v1/messages**  
  Получает все json сообщения из базы данных.

- **http://localhost:8081/api/v1/dead-letters**  
  Получает все ошибочные запросы к эндпоинту `/publish` из базы данных.
