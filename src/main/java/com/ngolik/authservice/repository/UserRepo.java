package com.ngolik.authservice.repository;

import com.ngolik.authservice.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserCredentials, Long> {

    Optional<UserCredentials> findByName(String username);
}
