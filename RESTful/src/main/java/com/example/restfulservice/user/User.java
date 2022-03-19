package com.example.restfulservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
// @JsonIgnoreProperties(value = {"password", "ssn"}) // 해당하는 값 무시
@JsonFilter("UserInfo")
@Entity // table 자동 생성
public class User {

    @Id // PK
    @GeneratedValue // Auto Increment
    private Integer id;

    @Size(min=4, message = "Name은 2글자 이상 입력해 주세요") // 최소 길이 / Bad Request 발생 시 Error message 출력
    private String name;

    @Past // 과거 값만 가능
    private Date join_date;

    // Filtering 을 진행 후 전송해야하는 값들

    // @JsonIgnore / return 무시
    private String password;

    private String ssn;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    public User(Integer id, String name, Date join_date, String password, String ssn) {
        this.id = id;
        this.name = name;
        this.join_date = join_date;
        this.password = password;
        this.ssn = ssn;
    }
}
