# **API de Contas a Pagar com Spring Boot e Docker**

## **Visão Geral**
Este projeto é uma API REST construída com **Java** e **Spring Boot**, com integração ao **PostgreSQL** e orquestração via **Docker**. A API gerencia contas a pagar, permitindo a criação, atualização e consulta de contas, além de funcionalidades de autenticação de usuários. A aplicação é completamente containerizada utilizando Docker e Docker Compose.

---

## **Tecnologias Usadas**
- **Java 17**.
- **Spring Boot**.
- **PostgreSQL**.
- **Docker** e **Docker Compose**.
- **JWT (JSON Web Token)** para autenticação.

---

## **Pré-requisitos**
Antes de rodar o projeto, você precisa garantir que as seguintes ferramentas estão instaladas na sua máquina:

- [Docker](https://www.docker.com/products/docker-desktop) e [Docker Compose](https://docs.docker.com/compose/)
- [Java 17](https://adoptopenjdk.net/)
- [Postman](https://www.postman.com/) para testar os endpoints da API

---

## **Como Executar a Aplicação**

### **1. Clonar o Repositório**
Clone o repositório para sua máquina local:

```
git clone https://github.com/izepon/contas-pagar-api.git

cd contas-pagar-api
```

### **2. Construir a Imagem Docker**
Este projeto já inclui um Dockerfile e um arquivo docker-compose.yml para facilitar a execução. Caso não tenha construído a imagem anteriormente, use o comando abaixo para construir e rodar os containers:

```
docker-compose up --build
```

O Docker Compose fará o seguinte:
- Criará um container para o banco de dados PostgreSQL.
- Criará um container para a aplicação Spring Boot.
- Aplicação estará na porta 8081.

### **3. Acessando a API**
Após a execução dos containers, a API estará acessível em http://localhost:8081. Você pode usar o Postman para testar os endpoints da API.
[Baixar Contas Pagar Apis.postman_collection.json](https://github.com/user-attachments/files/18742433/Contas.Pagar.Apis.postman_collection.json)

### **4. Parar os Containers**
Quando terminar de testar ou utilizar a aplicação, pode parar os containers com o seguinte comando:

```
docker-compose down
```

### **5 - Configuração do Banco de Dados**
O banco de dados PostgreSQL é configurado automaticamente via Docker Compose. 
