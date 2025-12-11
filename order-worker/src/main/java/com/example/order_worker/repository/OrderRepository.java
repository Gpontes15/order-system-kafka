package com.example.order_worker.repository;

import com.example.order_worker.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    // Só de estender JpaRepository, você ganha o método .save() de graça!
}