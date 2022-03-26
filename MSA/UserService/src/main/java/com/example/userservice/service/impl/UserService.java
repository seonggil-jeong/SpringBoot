package com.example.userservice.service.impl;

import com.example.userservice.service.IUserService;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.entity.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO pDTO) {
        pDTO.setUserId(UUID.randomUUID().toString()); // random ID Set
        // DTO -> Entity

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // Matching 전략 ( 완전 == 만 허용 )
        UserEntity userEntity = mapper.map(pDTO, UserEntity.class); // pDTO -> userEntity
        userEntity.setEncryptedPwd("exPwd");

        userRepository.save(userEntity);

        UserDTO rDTO = mapper.map(userEntity, UserDTO.class);
        return rDTO;
    }

    @Override
    public UserDTO getUserByUserID(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId); // UserId 값으로 찾기 / make

        ModelMapper mapper = new ModelMapper();
        UserDTO rDTO = mapper.map(userEntity, UserDTO.class);

        List<ResponseOrder> orders = new ArrayList<>();
        rDTO.setOrders(orders); // 사용자 주문 정보 Setting

        if (userEntity == null) {
            log.info("User Not found");
        }
        return rDTO;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }
}
