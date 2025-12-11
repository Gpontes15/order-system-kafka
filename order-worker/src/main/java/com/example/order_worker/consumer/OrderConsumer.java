package com.example.order_worker.consumer;

import com.example.order_worker.model.OrderEntity;
import com.example.order_worker.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class OrderConsumer {

    private final OrderRepository orderRepository;

    public OrderConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "order-topic", groupId = "order-worker-group")
    public void consume(String message) {
        log.info("Recebi do Kafka: {}", message);

        // A mensagem vem como texto: "ID,Produto,Preco"
        String[] parts = message.split(",");
        
        String orderId = parts[0];
        String product = parts[1];
        String priceStr = parts[2];

        // Cria o objeto para salvar no banco
        OrderEntity order = new OrderEntity();
        order.setOrderId(orderId);
        order.setProduct(product);
        order.setPrice(new BigDecimal(priceStr));

        // Salva no Postgres
        orderRepository.save(order);
        
        log.info("Pedido salvo no banco com sucesso! ID: {}", orderId);
    }
}