package com.example.restfulservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service // bean 용도 설정
public class UserDaoService {
    private static List<User> users = new ArrayList<>();        // Static 으로 선언하여 DB 에서 가져오는 것과 같이 메모리 값 사용하기

    private static int usersCount = 3;

    static {
        users.add(new User(1, "Jeong", new Date()));
        users.add(new User(2, "Seong", new Date()));
        users.add(new User(3, "Gil", new Date()));
    }
    // SELECT ID, NAME, JOIN_DATE FROM USER
    public List<User> findAll() {
        return users;
    }
    // INSERT INTO (ID, NAME, JOIN_DATE) VALUES (id, name, join_date)
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++usersCount);
        }

        users.add(user);
        return user;
    }
    // SELECT ID, NAME, JOIN_DATE FROM USER WHERE ID = 'id'
    public User findOne(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        //  id에 매칭되는 값이 없을 경우
        return null;
    }

}
