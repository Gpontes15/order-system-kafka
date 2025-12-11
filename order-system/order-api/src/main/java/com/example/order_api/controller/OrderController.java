package com.example.order_api.controller;

import com.example.order_api.controller.OrderData; // Importação explicita para forçar o reconhecimento
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public String createOrder(@RequestBody OrderData data) {
        var orderId = UUID.randomUUID().toString();
        // A concatenação abaixo funciona automaticamente convertendo BigDecimal para String
        var message = orderId + "," + data.product() + "," + data.price();

        kafkaTemplate.send("order-topic", orderId, message);

        log.info("Evento enviado para o Kafka: {}", message);

        return "Pedido recebido! ID de rastreamento: " + orderId;
    }
}