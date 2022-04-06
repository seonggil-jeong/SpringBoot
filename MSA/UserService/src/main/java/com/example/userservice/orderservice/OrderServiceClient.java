package com.example.userservice.orderservice;

import com.example.userservice.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "order-service") // Eureka Server 에 등록된 이름 사용
public interface OrderServiceClient {
    @GetMapping("/order-service/{user_id}/orders")
    List<ResponseOrder> getOrders(@PathVariable String user_id); // Order Service 의 반환 값으로 설정 List<ResponseOrder>
}
