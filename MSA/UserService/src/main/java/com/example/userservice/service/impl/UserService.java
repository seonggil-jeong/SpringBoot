package com.example.userservice.service.impl;

import com.example.userservice.orderservice.OrderServiceClient;
import com.example.userservice.service.IUserService;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.entity.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("UserService")
@Slf4j
public class UserService implements IUserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private Environment env;
    private RestTemplate restTemplate;
    private ModelMapper modelMapper;
    private OrderServiceClient orderServiceClient;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Environment env,
                       RestTemplate restTemplate, ModelMapper modelMapper, OrderServiceClient orderServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username : " + username);
        UserEntity rEntity = userRepository.findByEmail(username);

        if (rEntity == null) { // 사용자가 없을 경우 오류
            throw new UsernameNotFoundException(username);
        }
        // 모두 통과 되었다면, email & pwd 를 저장하고, 권한 부여 (ture, ture, ture, true)
        return new User(rEntity.getEmail(), rEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>()); // 추가 권한 구현 시 + a
    }

    @Override
    public UserDTO createUser(UserDTO pDTO) {
        pDTO.setUserId(UUID.randomUUID().toString()); // random ID Set
        // DTO -> Entity

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // Matching 전략 ( 완전 == 만 허용 )
        UserEntity userEntity = mapper.map(pDTO, UserEntity.class); // pDTO -> userEntity
        userEntity.setEncryptedPwd(passwordEncoder.encode(pDTO.getPwd()));

        userRepository.save(userEntity);

        UserDTO rDTO = mapper.map(userEntity, UserDTO.class);
        return rDTO;
    }

    @Override
    public UserDTO getUserByUserID(String userId, String token) {

        UserEntity userEntity = userRepository.findByUserId(userId); // UserId 값으로 찾기 / make


        UserDTO rDTO = modelMapper.map(userEntity, UserDTO.class);

        if (rDTO == null) {
            throw new UsernameNotFoundException("User not found"); // 사용자 없을 경우 Exception
        }
            // RestTemplate Code
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("authorization", token);
//        HttpEntity headerEntity = new HttpEntity(headers);
//
//        String orderUrl = String.format(env.getProperty("order-service.url") + "/%s/orders", userId); // URL 설정
//        ResponseEntity<List<ResponseOrder>> restOrderList =
//                restTemplate.exchange(orderUrl, HttpMethod.GET, headerEntity, // url, Method, request, response
//                        new ParameterizedTypeReference<List<ResponseOrder>>() {
//                        });
//        log.info("restOrderList : " + restOrderList);
//        List<ResponseOrder> orderList = restOrderList.getBody();

        // FeignClient Code
        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);


        rDTO.setOrders(orderList);

        return rDTO;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO getUserDetailsByEmail(String username) {
        UserEntity rEntity = userRepository.findByEmail(username);

        if (rEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        UserDTO rDTO = new ModelMapper().map(rEntity, UserDTO.class);
        return rDTO;
    }
}
