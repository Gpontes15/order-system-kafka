package com.example.order_worker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Data // Lombok cria Getters e Setters
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    
    @Id
    private String orderId;   // O UUID que veio da API
    private String product;
    private BigDecimal price;
}