package com.example.userservice.controller;

import com.example.userservice.jpa.entity.UserEntity;
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

import java.util.ArrayList;
import java.util.List;

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

    // 사용자 조회
    @GetMapping("users/{user_id}")
    public ResponseEntity<ResponseUser> getUsersByUserId(@PathVariable String user_id) throws Exception {

        UserDTO rDTO = userService.getUserByUserID(user_id);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ResponseUser responseUser = mapper.map(rDTO, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }


    // 전체 사용자 조화
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() throws Exception {
        log.info(this.getClass().getName() + "getUsers Start!");

        Iterable<UserEntity> userList = userService.getUserByAll();

        ModelMapper mapper = new ModelMapper();
        List<ResponseUser> result = new ArrayList<>(); // 바꾼 값을 받을 List

        userList.forEach(v -> {    // userList 값을 RequestUser 로 변경 and Add
            result.add(mapper.map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
