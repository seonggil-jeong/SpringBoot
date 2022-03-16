package com.example.restfulservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminUserController { // 관리자를 위한 Controller
    // 의존성 주입 / 생성자 사용
    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {
        List<User> users = service.findAll();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "password", "ssn");

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

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "ssn", "password"); // id, name, joinDate, ssn  값을 Out

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter); // UserInfo 에 filter 적용
        MappingJacksonValue mapping = new MappingJacksonValue(user); // Return Type (user)
        mapping.setFilters(filters); // filters 에 적용 된 filter 값을 mapping (return) 에 setting

        return mapping;
    }

}
