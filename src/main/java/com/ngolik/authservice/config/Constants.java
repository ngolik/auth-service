package com.ngolik.authservice.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";

    // Cognito constants
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "given_name";
    public static final String LAST_NAME = "family_name";
    public static final String EMAIL_VERIFIED = "email_verified";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String CUSTOM_ROLE = "custom:rl_role";
    public static final String CUSTOM_GROUP = "custom:rl_group";
    public static final String CUSTOM_GROUP_ROLE = "custom:rl_group_role";
    public static final String CUSTOM_USER_STATUS = "custom:rl_user_status";
    public static final String CUSTOM_USERNAME = "custom:rl_username";

    private Constants() {}
}
