package com.ngolik.authservice.service;

import com.ngolik.authservice.entity.UserCredentials;
import com.ngolik.authservice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public String saveUser(UserCredentials userCredentials) {
        userCredentials.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
        userRepo.save(userCredentials);
        //TODO
        return "User added to system";
    }

    @Override
    public String generateToken(String userName) {
        return jwtService.generateToken(userName);
    }

    @Override
    public void  validateToken(String token) {
        jwtService.validateToken(token);
    }
}
