package com.ngolik.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class CognitoConfig {

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        AwsCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("private-acc-profile");

        return CognitoIdentityProviderClient.builder()
            .credentialsProvider(credentialsProvider)
            .region(Region.of(awsRegion))
            .build();
    }
}
