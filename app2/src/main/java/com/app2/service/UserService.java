package com.app2.service;

import com.app2.entity.User;
import com.app2.payload.LoginDto;
import com.app2.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private JWTService jWTService;
    public UserService(UserRepository userRepository, JWTService jWTService) {
        this.userRepository = userRepository;
        this.jWTService = jWTService;
    }
    public String verifyLogin(LoginDto dto){
        Optional<User>opUser =userRepository.findByUsername(dto.getUsername());
        if(opUser.isPresent()){
            User user=opUser.get();
            if(BCrypt.checkpw(dto.getPassword(),user.getPassword())){
                return jWTService.generateToken(user.getUsername());
            }
        }
        return null;
    }

}
