package com.example.restfulservice.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    // 의존성 주입 / 생성자 사용
    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }


    @GetMapping("/users/{id}") // int 로 선언하여 int 값으로 매핑
    public User retrieveOneUsers(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id)); // 오류 반환
        }
        return user;
    }

    // ------------   POST   -----------------------------------------------------

    /*
    *
    * Headers 에 Content-Type : application/json 추가
    *
    * */

    @PostMapping(value = "/users")
    public ResponseEntity<User> createUser(@RequestBody User user) { // HttpEntity 클래스를 상속받아 구현한 클래스가 RequestEntity, ResponseEntity 클래스
        User savedUser = service.save(user);

        URI location= ServletUriComponentsBuilder.fromCurrentRequest() // 요청 값
                .path("/{id}") // URI location 값 요청값 ( 요청 URL )+= /{id}
                .buildAndExpand(savedUser.getId()) // 만든 가변 변수 {id} 에 넣을 값 지정
                .toUri(); // toString


        return ResponseEntity.created(location).build();

    }

    @DeleteMapping("/users/{id}") // GetMapping = 사용자 조회
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteUser(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id)); // Exception 으로 넘기기
        }

    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User pUser) {
        User rUser = service.updateUser(pUser);
        if (rUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", pUser.getId()));
        }

        URI location= ServletUriComponentsBuilder.fromCurrentRequest() // 요청 값
                .path("/{id}") // URI location 값 요청값 ( 요청 URL )+= /{id}
                .buildAndExpand(pUser.getId()) // 만든 가변 변수 {id} 에 넣을 값 지정
                .toUri(); // toString


        return ResponseEntity.created(location).build();
    }


}
