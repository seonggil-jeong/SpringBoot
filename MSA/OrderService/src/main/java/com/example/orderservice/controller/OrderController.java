package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.jpa.entity.OrderEntity;
import com.example.orderservice.service.IOrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    @Resource(name = "OrderService")
    private IOrderService orderService;
    Environment env;

    @Autowired
    public OrderController(Environment env) {
        this.env = env;
    }
    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder orderDetails, @PathVariable("userId") String userId){
        ModelMapper mapper = new ModelMapper();
        OrderDTO pDTO = mapper.map(orderDetails, OrderDTO.class);
        pDTO.setUserId(userId);

        OrderDTO rDTO = orderService.createOrder(pDTO);

        ResponseOrder responseOrder = mapper.map(rDTO, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId){
        Iterable<OrderEntity> rIterable = orderService.getOrdersByUserId(userId);
        ModelMapper mapper = new ModelMapper();

        List<ResponseOrder> rList = new ArrayList<>();
        rIterable.forEach(v -> {
            rList.add(mapper.map(v, ResponseOrder.class));
        });


        return ResponseEntity.status(HttpStatus.OK).body(rList);

    }

}
