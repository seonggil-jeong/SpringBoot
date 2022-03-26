package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.jpa.OrderRepository;
import com.example.orderservice.jpa.entity.OrderEntity;
import com.example.orderservice.service.IOrderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Data
@Slf4j
@Service("OrderService")
public class OrderService implements IOrderService {

    @Resource(name = "OrderRepository")
    private OrderRepository orderRepository;


    @Override
    public OrderDTO createOrder(OrderDTO pDTO) {
        pDTO.setOrderId(UUID.randomUUID().toString());
        pDTO.setTotalPrice(pDTO.getUnitPrice() * pDTO.getQty()); // 수량 X 단가

        ModelMapper mapper = new ModelMapper();
        OrderEntity pEntity = mapper.map(pDTO, OrderEntity.class);

        orderRepository.save(pEntity);

        OrderDTO rDTO = mapper.map(pEntity, OrderDTO.class);


        return rDTO;
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public OrderDTO getOrderByOrderId(String orderId) {
        OrderEntity rEntity = orderRepository.findByOrderId(orderId);

        ModelMapper mapper = new ModelMapper();
        OrderDTO rDTO = mapper.map(rEntity, OrderDTO.class);
        return rDTO;
    }
}
