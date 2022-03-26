package com.example.userservice.service.impl;

import com.example.userservice.service.IUserService;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.entity.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("UserService")
@Slf4j
public class UserService implements IUserService {
    private UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username : " + username);
        UserEntity rEntity = userRepository.findByEmail(username);

        if (rEntity == null) { // 사용자가 없을 경우 오류
            throw new UsernameNotFoundException(username);
        }
        // 모두 통과 되었다면
        return new User(rEntity.getEmail(), rEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>()); // 추가 권한 구현 시 + a
    }

    @Override
    public UserDTO createUser(UserDTO pDTO) {
        pDTO.setUserId(UUID.randomUUID().toString()); // random ID Set
        // DTO -> Entity

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // Matching 전략 ( 완전 == 만 허용 )
        UserEntity userEntity = mapper.map(pDTO, UserEntity.class); // pDTO -> userEntity
        userEntity.setEncryptedPwd(passwordEncoder.encode(pDTO.getPwd()));

        userRepository.save(userEntity);

        UserDTO rDTO = mapper.map(userEntity, UserDTO.class);
        return rDTO;
    }

    @Override
    public UserDTO getUserByUserID(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId); // UserId 값으로 찾기 / make

        ModelMapper mapper = new ModelMapper();
        UserDTO rDTO = mapper.map(userEntity, UserDTO.class);

        List<ResponseOrder> orders = new ArrayList<>();
        rDTO.setOrders(orders); // 사용자 주문 정보 Setting

        if (userEntity == null) {
            log.info("User Not found");
        }
        return rDTO;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }
}
