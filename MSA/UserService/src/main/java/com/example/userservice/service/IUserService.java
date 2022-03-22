package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.entity.UserEntity;

public interface IUserService {
    UserDTO createUser(UserDTO pDTO);

    UserDTO getUserByUserID(String user_id);

    Iterable<UserEntity> getUserByAll();

}
