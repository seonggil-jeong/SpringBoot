package com.example.userservice.controller;

import com.example.userservice.service.impl.UserService;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user-service")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 사용자 추가
    @PostMapping("/users")

    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        log.info(user.getEmail());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDTO pDTO = mapper.map(user, UserDTO.class);
        userService.createUser(pDTO);

        ResponseUser responseUser = mapper.map(pDTO, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser); // CREATED (201) Return

    }

}
