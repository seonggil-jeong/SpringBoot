package com.example.restfulservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn; // static 으로 import 하여 사용 / WebMvcLinkBuilder.methodOn (X), methodOn (O)

@RestController
@Slf4j
public class UserController {
    // 의존성 주입 / 생성자 사용
    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {

        List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "join_date");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }


    @GetMapping("/users/{id}") // int 로 선언하여 int 값으로 매핑
    public MappingJacksonValue retrieveOneUsers(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id)); // 오류 반환
        }

        // HATEOAS / 반환 값으로 사용되는 정보에 현재 상태(권한 등을 고려해서)에서 사용할 수 있는 또다른 링크의 주소를 같이 얻을 수 있음
        EntityModel<User> model = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers()); // retrieveAllUser link 가져오기
        model.add(linkTo.withRel("all-users")); // all-users : {url} 로 저장
//        {
//            "id": 1,
//                "name": "Jeong",
//                "_links": {
//                      "all-users": {
//                      "href": "http://localhost:8080/users"
//                            }
//                      }
//       }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(model);
        mapping.setFilters(filters);

        return mapping;
    }

    // Headers 에 Content-Type : application/json 추가
    @PostMapping(value = "/users")      // Valid를 추가하여 Validation 조건 사용
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) { // HttpEntity 클래스를 상속받아 구현한 클래스가 RequestEntity, ResponseEntity 클래스
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
