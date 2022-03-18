package com.example.restfulservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
@Slf4j
public class UserJPAController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {

        List<EntityModel<User>> result = new ArrayList<>(); // 전역 변수 선언
        List<User> users = userRepository.findAll();

        for (User user : users) { // user in users
            EntityModel entityModel = EntityModel.of(user); // EntityModel 변환
            entityModel.add(linkTo(methodOn(this.getClass()).retrieveOneUsers(user.getId())).withRel("retrieveOneUser {" + user.getId() + "}")); // 링크 추가
            result.add(entityModel); // List<entityModel> 에 값 추가하기 {User + links}
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "joinDate", "name", "password", "ssn", "posts");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(result);
        mapping.setFilters(filters);
//                "links": [
//        {
//            "rel": "retrieveOneUser {1}",
//                "href": "http://localhost:8080/jpa/users/1"
//        }
//        ]

//        [
//        {
//            "rel": "retrieveOneUser {2}",
//                "href": "http://localhost:8080/jpa/users/2"
//        }
//        ]

//        public ResponseEntity<CollectionModel<EntityModel<User>>> retrieveAllUsers()
//                                  ~~~ <!-- 필터가 없을 경우 이렇게 return -->
//        return ResponseEntity.ok(CollectionModel.of(result));
        return mapping;
    }

    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveOneUsers(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        EntityModel<Optional<User>> model = EntityModel.of(user);
        model.add(linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users"));

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "joinDate", "name", "password", "ssn", "posts");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(model);
        mapping.setFilters(filters);


        return mapping;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);

    }

    @PostMapping("/users")
    public ResponseEntity<EntityModel<User>> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }



    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s} not found]", id));
        }

        return user.get().getPosts();
    }
}
