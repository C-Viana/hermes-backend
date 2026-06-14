![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

# hermes-backend
Um Notification Hub para estudo e apresentação. 
Este projeto disponibiliza um serviço que simula o envio de notificações para diferentes plataformas.
O intento de sua criação foi para consolidação de conhecimentos no desenvolvimento backend com recursos e padrões comumente utilizados, como Spring Boot, RabbitMQ, Redis, etc.


## Tecnologias Utilizadas
- Java 25
- Spring Boot 4 com Hibernate e Migrations
- RabbitMQ
- Redis 8
- PostgreSQL 18
- Testcontainers
- Docker e Docker Compose


## Padrões Aplicados
- **Strategy Pattern/OCP**: diferentes tipos de notificação (SMS, E-mail, push) são definidos automaticamente pela requisição do usuário;
- **Assincronismo**: o envio das notificações são realizados por um serviço consumidor de uma fila de requisições independente;
- **Idempotency Consumer**: um cache armazena por tempo determinado o hash das requisições para evitar que uma mesma notificação seja enviada múltiplas vezes;
- **Resiliência com Resilience4j**: o serviço de envio está preparado para realizar mais de uma tentativa de envio antes de falhar, sem interromper o serviço.


## Instruções para execução
1. Instale o Java (JRE ou JDK) na versão 25 ou superior e certifique-se de adicioná-lo às variáveis de ambiente
2. Instale e execute o Docker com docker compose no sistema
3. Executar o arquivo 'docker-compose.yaml' pelo comando ```docker compose up -d```
4. Aguarde os serviços do Postgres, Redis e RabbitMQ ficarem disponíveis
5. Na pasta raíz desse projeto execute o comando ```mvn spring-boot:run```
6. Com uma aplicação de sua preferência (Console, Postman, Insomnia, etc), faça uma requisição como nesse cURL de exemplo:
    ```
    curl --location 'http://localhost:8080/api/v1/notifications' \
    --form 'type="EMAIL"' \
    --form 'addressee="joaosilva@gmail.com, mariaferreira@yahoo.com.br"' \
    --form 'message="Mensagem de teste"' \
    --form 'dateSchedule="07-06-2026 20:30:00"'
    ```


## Autoria
    Feito com ☕ e persistência por Carlos Eduardo de Souza Viana
    [LinkedIn](https://www.linkedin.com/in/carlos-eds-viana)