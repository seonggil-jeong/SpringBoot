package com.example.orderservice.jpa;

import com.example.orderservice.jpa.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("OrderRepository")
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    OrderEntity findByOrderId(String orderId);

    Iterable<OrderEntity> findByUserId(String userId);
}
