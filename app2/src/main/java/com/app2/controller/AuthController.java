package com.app2.controller;

import com.app2.entity.User;
import com.app2.payload.JWTTokenDto;
import com.app2.payload.LoginDto;
import com.app2.repository.UserRepository;
import com.app2.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private UserRepository UserRepository;
    private UserService userService;

    public AuthController(com.app2.repository.UserRepository userRepository, UserService userService) {
        UserRepository = userRepository;

        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody User user){
        Optional<User> opUsername=UserRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("user exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User>opEmail=UserRepository.findByEmail(user.getEmail());
        if(opEmail.isPresent()){
            return new ResponseEntity<>("email id exists",HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        String encodedPassword =passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);

        String hashpw =BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10));
        user.setPassword(hashpw);
        UserRepository.save(user);
        return new ResponseEntity<>("created",HttpStatus.CREATED);
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> userSignIn( @RequestBody LoginDto dto){
       String jwtToken = userService.verifyLogin(dto);
        if(jwtToken!=null) {
            JWTTokenDto tokenDto= new JWTTokenDto();
            tokenDto.setToken(jwtToken);
            tokenDto.setTokenType("JWT");
            return new ResponseEntity<>(tokenDto,HttpStatus.CREATED);

        }
        return new ResponseEntity<>("invalid token",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/message")
    public String getMessage(){
        return "hello";
    }
}
