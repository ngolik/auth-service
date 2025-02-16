package com.ngolik.authservice.service.cognito;

import java.util.List;

import com.ngolik.authservice.dto.UserDTO;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

public interface AuthService {

    void deleteUser(String userName);

    void updateUserInfo(UserDTO userDTO, String externalId);

    void enableUser(String username);

    void disableUser(String username);

    void updateRoles(String email, List<String> roles);

    UserType inviteUser(UserDTO dto);
}
