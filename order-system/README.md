# 🚀 Order System - Event-Driven Architecture

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)](https://spring.io/projects/spring-boot)
[![Kafka](https://img.shields.io/badge/Apache_Kafka-Event_Streaming-black)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)](https://www.postgresql.org/)

Um sistema de processamento de pedidos robusto e resiliente, projetado para suportar alta volumetria de requisições utilizando uma arquitetura assíncrona baseada em eventos.

---

## 🧠 O Problema e a Solução

Em arquiteturas tradicionais (síncronas), quando um cliente faz um pedido, a API precisa salvar no banco, processar pagamento e enviar e-mail antes de responder. Se o banco ficar lento ou cair (comum em eventos como Black Friday), a API cai junto e o cliente perde a compra.

**A Solução deste projeto:**
Desacoplar o recebimento do pedido do seu processamento.
1.  A **API** recebe o pedido e apenas publica um evento no **Kafka** (resposta em milissegundos).
2.  Um **Worker** dedicado consome esse evento e processa a gravação no **PostgreSQL** em background.

Isso garante que a API de vendas permaneça sempre disponível (Non-blocking), independente da carga no banco de dados.

---

## 🏗️ Arquitetura

```mermaid
sequenceDiagram
    participant Cliente
    participant API as Order API (Producer)
    participant Kafka as Apache Kafka
    participant Worker as Order Worker (Consumer)
    participant DB as PostgreSQL

    Cliente->>API: POST /orders (JSON)
    Note over API: Valida dados e gera ID
    API->>Kafka: Publica evento (Topic: order-topic)
    Kafka-->>API: Ack
    API-->>Cliente: 202 Accepted (Pedido Recebido)
    
    Note over Worker: Processamento Assíncrono
    Worker->>Kafka: Consome mensagem
    Worker->>DB: Salva pedido (INSERT)
    Note over Worker: Log de confirmação