package com.ripplereach.ripplereach.constants;

public class Messages {
    // Error Messages
    public static final String USER_NOT_FOUND = "User with this userId doesn't exist";
    public static final String COMPANY_UNIVERSITY_PRESENT = "Either university or company must be present, but not both.";
    public static final String UPDATE_PROFESSION_WITH_UNIVERSITY = "Unable to update profession along with university.";
    public static final String ERROR_UPDATING_USER = "Error while updating user!";
    public static final String VALIDATION_FAILED = "Validation failed!";
    public static final String UNPROCESSABLE_ENTITY = "Unprocessable entity.";
    public static final String ENTITY_NOT_FOUND = "Entity not found!";
    public static final String FORBIDDEN_ACCESS_DENIED = "Forbidden, Access denied!";
    public static final String METHOD_NOT_ALLOWED = "Method not allowed.";
    public static final String ENTITY_ALREADY_EXISTS = "Entity already exists!";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred.";

    // Informational Messages
    public static final String LOGGED_OUT_SUCCESSFULLY = "Logged out successfully!!";
    public static final String USER_CREATED_SUCCESSFULLY = "User created successfully.";
    public static final String USER_UPDATED_SUCCESSFULLY = "User updated successfully.";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully.";
    public static final String CATEGORY_DELETED_SUCCESSFULLY = "Category deleted successfully.";

    // Validation Messages
    public static final String ID_TOKEN_REQUIRED = "ID token is required.";
    public static final String PHONE_REQUIRED = "Phone number is required.";
    public static final String INVALID_PHONE = "Invalid phone number.";
    public static final String VALID_PHONE = "Phone number must be digits only";
    public static final String USERNAME_REQUIRED = "Username is required.";
    public static final String REFRESH_TOKEN_REQUIRED = "Refresh token is required.";
    public static final String DEVICE_TOKEN_REQUIRED = "Device token is required.";
    public static final String TITLE_REQUIRED = "Title is required.";
    public static final String BODY_REQUIRED = "Body is required.";
    public static final String CATEGORY_NAME_REQUIRED = "Category name is required.";
    public static final String COMMUNITY_NAME_REQUIRED = "Community name is required.";
    public static final String COMMUNITY_ID_REQUIRED = "Community id is required.";
    public static final String COMMUNITY_IMAGE_REQUIRED = "Community image is required and should be of max size 5MB.";
    public static final String COMMUNITY_DESC_REQUIRED = "Community description is required.";
    public static final String COMMUNITY_NAME_SIZE = "Community size must be between 5 to 100 characters.";
    public static final String COMMUNITY_DESC_SIZE = "Community description length must be between 10 to 500 characters.";
    public static final String PROFESSION_REQUIRED = "Profession must be provided if company is present.";
    public static final String COMPANY_OR_UNIVERSITY_REQUIRED = "Either company or university must be provided.";
    public static final String CATEGORY_DESC_REQUIRED = "Category description is required.";
    public static final String CATEGORY_DESC_SIZE = "Category desc must be between 10 to 500 characters.";
    public static final String USERNAME_SIZE = "Username must be between 3 to 25 characters.";
    public static final String UNIVERSITY_SIZE = "University name must be between 3 to 50 characters.";
    public static final String COMPANY_SIZE = "Company name must be between 3 to 50 characters.";
    public static final String PROFESSION_SIZE = "Profession name must be between 3 to 50 characters.";
    public static final String CATEGORY_NAME_SIZE = "Category name must be between 3 to 25 characters.";

    // Other Messages
    public static final String WELCOME_MESSAGE = "Welcome to Ripple Reach!";
    public static final String PLATFORM_DESCRIPTION = "Conveying the idea of creating waves of influence and connection that extend far and wide.";
}
