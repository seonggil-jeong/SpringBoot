package com.example.orderservice.service;


import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.jpa.entity.OrderEntity;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

public interface IOrderService {
    OrderDTO createOrder(OrderDTO pDTO); // 주문 하기

    OrderDTO getOrderByOrderId(String orderId); // 주문 상세 확인

    Iterable<OrderEntity> getOrdersByUserId(String userId); // 각 사용자 주문 All 조회


}
