package com.example.userservice.service.impl;

import com.example.userservice.service.IUserService;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.entity.UserEntity;
import com.example.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO pDTO) {
        pDTO.setUser_id(UUID.randomUUID().toString()); // random ID Set
        // DTO -> Entity

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // Matching 전략 ( 완전 == 만 허용 )
        UserEntity userEntity = mapper.map(pDTO, UserEntity.class); // pDTO -> userEntity
        userEntity.setEncryptedPwd("exPwd");

        userRepository.save(userEntity);

        UserDTO rDTO = mapper.map(userEntity, UserDTO.class);
        return rDTO;
    }
}
