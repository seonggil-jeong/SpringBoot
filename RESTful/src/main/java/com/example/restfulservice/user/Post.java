package com.example.restfulservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    // User ... ___ > Post (1 : N), parent : Child
    @ManyToOne(fetch = FetchType.LAZY) // <Many(POST) : One(USER)>      /       LAZY : 사용자 요청 시 항상 Post 를 함께 요청하지 않음
    @JsonIgnore // Return X
    private User user; // @ID 값과 매칭
}
