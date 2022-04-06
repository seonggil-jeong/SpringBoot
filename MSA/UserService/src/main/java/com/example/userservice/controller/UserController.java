package com.example.userservice.controller;

import com.example.userservice.jpa.entity.UserEntity;
import com.example.userservice.service.impl.UserService;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.util.TokenUtil;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Slf4j
public class UserController {

    private UserService userService;
    private Environment env;

    public UserController(UserService userService, Environment env) {
        this.userService = userService;
        this.env = env;
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
    @GetMapping(value = "users/{user_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<ResponseUser>> getUsersByUserId(@PathVariable String user_id,
                                                                      @RequestHeader HttpHeaders headers) throws Exception {
        // Token 인증 처리 구현
        String token = headers.get("Authorization").get(0);
        UserDTO rDTO = userService.getUserByUserID(user_id, token);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ResponseUser responseUser = mapper.map(rDTO, ResponseUser.class);


        // Hateoas
        EntityModel<ResponseUser> model = EntityModel.of(responseUser);
//        model.add(linkTo(methodOn(this.getClass()).getUsers()).withRel("getAllUser"));

        return ResponseEntity.status(HttpStatus.OK).body(model);
    }


    // 전체 사용자 조회
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(@RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + "getUsers Start!");
        log.info("header : " + headers);
        // Token 에서 UserId 가져오기
        String userId = TokenUtil.getUserIdFromToken(headers.get("Authorization").get(0), env.getProperty("token.secret"));
        log.info("userId : " + userId);


        Iterable<UserEntity> userList = userService.getUserByAll();

        ModelMapper mapper = new ModelMapper();
        List<ResponseUser> result = new ArrayList<>(); // 바꾼 값을 받을 List

        userList.forEach(v -> {    // userList 값을 RequestUser 로 변경 and Add
            result.add(mapper.map(v, ResponseUser.class));
        });

        log.info(this.getClass().getName() + ".getUsers End!");

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
