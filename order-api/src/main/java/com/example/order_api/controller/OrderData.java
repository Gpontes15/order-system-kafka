package com.example.order_api.controller;

import java.math.BigDecimal;

// O record já cria getters, equals, hashCode e toString automaticamente.
public record OrderData(String product, BigDecimal price) {
}