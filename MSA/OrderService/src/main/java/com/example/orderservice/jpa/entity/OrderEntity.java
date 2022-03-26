package com.example.orderservice.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ORDER_INFO")
public class OrderEntity implements Serializable { // 직렬화 : 객체를 전송 시 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto set 전략
    private int orderSeq;

    @Column(nullable = false, length = 120, unique = true) // unique = 중복 X
    private String productId;

    @Column(nullable = false)
    private String qty;

    @Column(nullable = false)
    private String unitPrice;

    @Column(nullable = false)
    private String totalPrice;

    @Column(nullable = false)
    private String userId;
    @Column(nullable = false, unique = true)
    private String orderId;

}
