package com.ngolik.authservice.service.cognito;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.ngolik.authservice.dto.UserDTO;
import com.ngolik.authservice.service.exception.CognitoConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDisableUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminEnableUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeliveryMediumType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import static com.ngolik.authservice.config.Constants.CUSTOM_ROLE;
import static com.ngolik.authservice.config.Constants.EMAIL;
import static com.ngolik.authservice.config.Constants.EMAIL_VERIFIED;
import static com.ngolik.authservice.config.Constants.FIRST_NAME;
import static com.ngolik.authservice.config.Constants.LAST_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CognitoAuthServiceImpl implements AuthService {

    private final CognitoIdentityProviderClient cognitoClient;

    @Value("${aws.cognito.user-pool-id}")
    private String userPoolId;

    @Override
    public UserType inviteUser(UserDTO dto) {

        List<AttributeType> userAttributes = List.of(
            AttributeType.builder().name(EMAIL).value(dto.getEmail()).build(),
            AttributeType.builder().name(FIRST_NAME).value(dto.getFirstName()).build(),
            AttributeType.builder().name(LAST_NAME).value(dto.getLastName()).build(),
            AttributeType.builder().name(EMAIL_VERIFIED).value(Boolean.TRUE.toString()).build());

        AdminCreateUserRequest.Builder createUserRequestBuilder = AdminCreateUserRequest.builder()
            .userPoolId(userPoolId)
            .username(dto.getEmail())
            .userAttributes(userAttributes)
            .desiredDeliveryMediums(DeliveryMediumType.EMAIL)
            .forceAliasCreation(true);

        AdminCreateUserRequest createUserRequest = createUserRequestBuilder.build();
        try {
            AdminCreateUserResponse adminCreateUserResponse = cognitoClient.adminCreateUser(createUserRequest);
            log.info("User created successfully: {}", adminCreateUserResponse.user().username());
            return adminCreateUserResponse.user();
        } catch (CognitoIdentityProviderException e) {
            if (e.getMessage() != null && e.getMessage().contains("User account already exists")) {
                throw new CognitoConflictException("User account already exists");
            }
            log.error("Error creating user in Cognito: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to create user in Cognito", e);
        }
    }

    @Override
    public void updateUserInfo(UserDTO userDTO, String externalId) {
        updateUserAttributes(externalId, Map.of(
            FIRST_NAME, userDTO.getFirstName(),
            LAST_NAME, userDTO.getLastName()));
    }

    @Override
    public void deleteUser(String userName) {
        AdminDeleteUserRequest deleteUserRequest = AdminDeleteUserRequest.builder()
            .userPoolId(userPoolId)
            .username(userName)
            .build();
        try {
            cognitoClient.adminDeleteUser(deleteUserRequest);
            log.info("User {} successfully deleted from cognito", userName);
        } catch (Exception ex) {
            log.error("User {} not found in cognito, but we are not throwing exception", userName);
        }
    }

    @Override
    public void enableUser(String username) {
        cognitoClient.adminEnableUser(AdminEnableUserRequest.builder()
            .userPoolId(userPoolId)
            .username(username)
            .build());
    }

    @Override
    public void disableUser(String username) {
        cognitoClient.adminDisableUser(AdminDisableUserRequest.builder()
            .userPoolId(userPoolId)
            .username(username)
            .build());
    }

    private void updateUserAttributes(String username, Map<String, String> attributes) {
        AdminUpdateUserAttributesRequest updateUserRequest = AdminUpdateUserAttributesRequest.builder()
            .userAttributes(
                attributes.entrySet().stream().
                    map(entry -> AttributeType.builder().name(entry.getKey()).value(entry.getValue()).build()).toList()
            )
            .username(username)
            .userPoolId(userPoolId)
            .build();
        try {
            cognitoClient.adminUpdateUserAttributes(updateUserRequest);
        } catch (CognitoIdentityProviderException e) {
            log.error("Error updating user in Cognito: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to update user in Cognito", e);
        }
    }

    @Override
    public void updateRoles(String email, List<String> roles) {
        cognitoClient.adminUpdateUserAttributes(AdminUpdateUserAttributesRequest.builder()
            .userAttributes(
                AttributeType.builder().name(CUSTOM_ROLE).value(String.join(" ", new HashSet<>(roles))).build()
            )
            .username(email)
            .userPoolId(userPoolId)
            .build());
    }
}
