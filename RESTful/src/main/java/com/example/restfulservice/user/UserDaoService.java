package com.example.restfulservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

    public User deleteUser(int id) {
        Iterator<User> iterator = users.iterator(); // 변환

        while (iterator.hasNext()) { // 다음 값이 있으면 true
            User user = iterator.next(); // 다음 값

            if (user.getId() == id) {
                iterator.remove();

                return user;
            }

        }
            return null;
    }

    public User updateUser(User pUser) {
        for (User user : users) {
            if (user.getId() == pUser.getId()) {
                user.setId(pUser.getId());
                user.setName(pUser.getName());
                user.setJoin_date(new Date());
                return user;
            }
        }
        return null;
        }
    }

