package com.example.userservice.jpa.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "USER_INFO")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto set 전략
    private int userSeq;

    @Column(nullable = false, length = 50, unique = true) // unique = 중복 X
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String encryptedPwd;
}
