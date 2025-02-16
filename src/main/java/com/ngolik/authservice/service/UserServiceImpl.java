package com.ngolik.authservice.service;

import java.util.List;

import com.ngolik.authservice.dto.UserDTO;
import com.ngolik.authservice.entity.User;
import com.ngolik.authservice.repository.UserRepository;
import com.ngolik.authservice.service.cognito.AuthService;
import com.ngolik.authservice.service.exception.ResourceNotFoundException;
import com.ngolik.authservice.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(userMapper::toDto)
            .toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        authService.inviteUser(userDTO);
        User user = userMapper.toEntity(userDTO);
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userMapper.partialUpdate(user, userDTO);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(user);
    }
}
