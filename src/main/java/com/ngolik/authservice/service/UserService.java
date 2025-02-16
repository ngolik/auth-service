package com.ngolik.authservice.service;

import java.util.List;

import com.ngolik.authservice.dto.UserDTO;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    public void deleteUser(Long id);
}
