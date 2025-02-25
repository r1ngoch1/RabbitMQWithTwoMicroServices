package com.royal.receiverService.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.sql.Timestamp;

/**
 * DTO-класс, представляющий сообщение с его основными аттрибутами.
 * Этот класс используется для передачи данных сообщения в различных слоях приложения. Он включает в себя поля:
 * идентификатор, имя, цену и временную метку.
 */

public class Message {
    @NotBlank(message = "ID не может быть пустым")
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotNull(message = "Цена не может быть null")
    @Positive(message = "Цена должна быть положительным числом")
    private Double price;

    private Timestamp timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}


