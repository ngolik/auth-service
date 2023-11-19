package com.ngolik.authservice.service;

import com.ngolik.authservice.entity.UserCredentials;

public interface UserService {

    public String generateToken(String userName);

    public String saveUser(UserCredentials userCredentials);

    public void  validateToken(String token);
}
