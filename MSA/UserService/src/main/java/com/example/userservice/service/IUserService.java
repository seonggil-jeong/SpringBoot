package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    UserDTO createUser(UserDTO pDTO);

    UserDTO getUserByUserID(String user_id, String token);

    Iterable<UserEntity> getUserByAll();

    UserDTO getUserDetailsByEmail(String username);

}
