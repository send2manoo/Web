/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

/**
 *
 * @author Ramesh
 */
public class Constant {

    /**
     * Connection values
     */
    public static final String DATA_BASE_CONNECTION_URL = FieldSenseUtils.getPropertyValue("DATA_BASE_CONNECTION_URL");
    public static final String DATA_BASE_CONNECTION_URL_ONBOARD = FieldSenseUtils.getPropertyValue("DATA_BASE_CONNECTION_URL_FIELDSENSE");
    public static final String DEFAULT_COMMUNITY_DB_USERNAME = FieldSenseUtils.getPropertyValue("DEFAULT_COMMUNITY_DB_USERNAME");
    public static final String DEFAULT_COMMUNITY_DB_PASSWORD = FieldSenseUtils.getPropertyValue("DEFAULT_COMMUNITY_DB_PASSWORD");
//    public static final String DEFAULT_EJABBERD_DB_USERNAME = FieldSenseUtils.getPropertyValue("DEFAULT_EJABBERD_DB_USERNAME");
//    public static final String DEFAULT_EJABBERD_DB_PASSWORD = FieldSenseUtils.getPropertyValue("DEFAULT_EJABBERD_DB_PASSWORD");
    public static final int DEFAULT_COMMUNITY_DB_MIN_CONNECTION = Integer.parseInt(FieldSenseUtils.getPropertyValue("DEFAULT_COMMUNITY_DB_MIN_CONNECTION"));
    public static final int DEFAULT_COMMUNITY_DB_MAX_CONNECTION = Integer.parseInt(FieldSenseUtils.getPropertyValue("DEFAULT_COMMUNITY_DB_MAX_CONNECTION"));

    /**
     * C3P0 Properties
     */
    public static final int COMMUNITY_MAX_CONNECTION_AGE = Integer.parseInt(FieldSenseUtils.getPropertyValue("COMMUNITY_MAX_CONNECTION_AGE"));
    public static final int COMMUNITY_MAX_IDEAL_TIME_EXCESS_CONNECTIONS = Integer.parseInt(FieldSenseUtils.getPropertyValue("COMMUNITY_MAX_IDEAL_TIME_EXCESS_CONNECTIONS"));
    public static final int COMMUNITY_ACQUIRE_INCREMENT = Integer.parseInt(FieldSenseUtils.getPropertyValue("COMMUNITY_ACQUIRE_INCREMENT"));
    public static final int COMMUNITY_MAX_IDEAL_TIME = Integer.parseInt(FieldSenseUtils.getPropertyValue("COMMUNITY_MAX_IDEAL_TIME"));
    public static final int COMMUNITY_NUM_HELPER_THREADS = Integer.parseInt(FieldSenseUtils.getPropertyValue("COMMUNITY_NUM_HELPER_THREADS"));
    public static final int COMMUNITY_UNRETURNED_CONNECTION_TIMEOUT = Integer.parseInt(FieldSenseUtils.getPropertyValue("COMMUNITY_UNRETURNED_CONNECTION_TIMEOUT"));

    /**
     * Response Code
     */
    public static final String SUCCESS = "0000";
    public static final String FAILED = "1111";
    public static final String MIXRESULT = "3333"; // Added by jyoti, for displaying error msg and close the form
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String VALID = "Valid";
    public static final String ALREADY_EXIST = "ALREADY_EXIST"; // Added by jyoti
    public static final String INVALID_INPUT = "INVALID_INPUT"; // Added by Jyoti

    //public static final String USER_CREATED_SUCCESFULLY = "Users Added Successfully."; //Added by Siddhesh Pande on 25/10/2016
    public static final String USER_CREATED_SUCCESFULLY = "User created successfully ."; // added by vaibhav
    public static final String USERS_CREATED_SUCCESFULLY = "Users created successfully .";// added by vaibhav
    public static final String UNKNOWN = "3333"; // Added by jyoti, for displaying error msg and close the form
    public static final String STATUS_UNKNOWN = "UNKNOWN"; // Added by Jyoti
    public static final String ALREADY_EXIST_MOBILE = "ALREADY_EXIST_MOBILE";

    /**
     *Token
     */
    public static final String INVALID_TOKEN = "Invalid token . Please try again .";

    /**
     * create customer
     */
    public static final String CUSTOMER_CREATED = "Customer created successfully";
    public static final String CUSTOMER_UPDATED = "Customer updated successfully";
    public static final String CUSTOMER_CUSTOMER_CONTACT_UPDATED = "Customer and customer contact updated successfully";
    public static final String CUSTOMER_DELETED = "Customer deleted successfully";
    public static final String INVALID_CUSTOMER = "Invalid cutomerId, Please try again.";

    /**
     * create customer contact
     */
    public static final String CUSTOMER_CONTACT_CREATED = "Contact Person added successfully";
    public static final String CUSTOMER_CONTACT_UPDATED = "Customer contact updated successfully";
    public static final String CUSTOMER_CONTACT_DELETED = "Customer contact deleted successfully";
    public static final String INVALID_CUSTOMER_CONTACT = "Invalid cutomer contactId, Please try again.";

    /**
     *create userkey contact
     */
    public static final String USERKY_CREATED = "Userkey created successfully";
    public static final String USERKY_NOT_CREATED = "Userkey not created. Please try again";
    public static final String USERKEY_UPDATED = "Userkey updated successfully";
    public static final String USERKEY_DELETED = "Userkey deleted successfully";
    public static final String INVALID_USER = "Invalid userId or userKey. Please try again";

    //reset password
    public static final String RESET_PASSWORD = "Password updated successfully";
    
    //user travel logs
    /**
     *user travel logs
     */
    public static final String FORM_DELETED = "Form deleted successfully";
    public static final String INVALID_FORM = "Invalid form. Please try again";
    public static final String USER_TRAVEL_LOG_CREATED = "UserTravelLog created successfully";
    public static final String USER_TRAVEL_LOG_NOT_CREATED = "UserTravelLog not created. Please try again";

    /**
     *notes
     */
    public static final String NOTES_CREATED = "Notes Added Successfully";  //Added by Jyoti on 25th Oct 2016
    public static final String NOTES_NOT_CREATED = "Notes not created. Please try again";
    public static final String NOTES_UPDATED = "Notes updated successfully";
    public static final String NOTES_DELETED = "Notes deleted successfully";
    public static final String INVALID_CUSTOMERID = "Invalid customerId. Please try again";
    public static final String INVALID_ID = "Invalid id. Please try again";

    /**
     *Travels
     */
    public static final String TRAVELS_CREATED = "Travels created successfully";
    public static final String TRAVELS_NOT_CREATED = "Travels not created. Please try again";

    /**
     *imageUpload
     */
    public static final String IMAGE_UPLOAD_PATH = FieldSenseUtils.getPropertyValue("IMAGE_UPLOAD_PATH");//"/tmp/data/imageUpload";
    public static final String IMAGE_GET_PATH = FieldSenseUtils.getPropertyValue("IMAGE_GET_PATH");//"/tmp/data/imageGet";

    /**
     *customFormImageUpload
     */
    public static final String CUSTOM_FORM_IMAGE_UPLOAD_PATH = FieldSenseUtils.getPropertyValue("CUSTOM_FORM_IMAGE_UPLOAD_PATH");//"/tmp/data/CustomForm/imageUpload";

    /**
     *profilePicUpload
     */
    public static final String PROFILEPIC_UPLOAD_PATH = FieldSenseUtils.getPropertyValue("PROFILEPIC_UPLOAD_PATH");//"/tmp/data/profilePicUpload";

    /**
     *message
     */
    public static final String INVALID_MESSAGE_SENDER_ID = " Invalid sender . please send propper sender id .";
    public static final String INVALID_MESSAGE_RECIVER_ID = "Invalid Reciver . please send propper reciver id .";
    public static final String INVALID_MESSAGE_MESSAGE = "Invalid message . please send propper message";

    /**
     *forgot password
     */
    public static final String INVALID_EMAIL = "Invalid emailId . please send proper emailId";
    public static final String INVALID_DOMAIN = "Invalid email domain . please send proper emailId";
    public static final String INVALID_CONFIRM_PASSWORD = "Confirm password must match with new password.";
    public static final String SUCCESS_PASSWORD = "Password reset successfully.";
    public static final String FAIL_PASSWORD = "Password reset failed.Please try again";
    public static final String WEBSITE_PATH = FieldSenseUtils.getPropertyValue("WEBSITE_PATH");
    public static final String EMAIL_NOT_REGISTERD = "Sorry, we couldn’t find an account with that email address. Please contact your account administrator.";

    /**
     *activity purpose
     */
    public static final String ACTIVITY_PURPOSE_CREATED = "Activity purpose added successfully";
    public static final String ACTIVITY_PURPOSE_UPDATED = "Activity purpose updated successfully";
    public static final String ACTIVITY_PURPOSE_DELETE = "Activity purpose deleted successfully";
    public static final String ACTIVITY_PURPOSE_NOT_DELETE = "Activity purpose not deleted, please try again.";
    public static final String ACTIVITY_PURPOSE_INVALID = "Invalid activity purpose";

    /**
     *email template
     */
    public static final String EMAIL_TEMPLATE_PATH = FieldSenseUtils.getPropertyValue("EMAIL_TEMPLATE_PATH");
    public static final String IMPORT_ERROR_FILES = FieldSenseUtils.getPropertyValue("IMPORT_ERROR_FILES");
    public static final String GOOGLE_PUSH_NOTIFICATION_SERVER_KEY = FieldSenseUtils.getPropertyValue("GOOGLE_PUSH_NOTIFICATION_SERVER_KEY");

    /**
     *expense Category
     */
    public static final String EXPENSE_CATEGORY_CREATED = "Expense Category added successfully";
    public static final String EXPENSE_CATEGORY_UPDATED = "Expense Category updated successfully";
    public static final String EXPENSE_CATEGORY_DELETE = "Expense Category deleted successfully";
    public static final String EXPENSE_CATEGORY_NOT_DELETE = "Expense Category not deleted, please try again.";
    public static final String EXPENSE_CATEGORY_INVALID = "Invalid Expense Category";

    /**
     *industry categories
     */
    public static final String INDUSTRY_CATEGORY_CREATED = "Industry Category added successfully";
    public static final String INDUSTRY_CATEGORY_UPDATED = "Industry Category updated successfully";
    public static final String INDUSTRY_CATEGORY_DELETE = "Industry Category deleted successfully";
    public static final String INDUSTRY_CATEGORY_NOT_DELETE = "Industry Category not deleted, please try again.";
    public static final String INDUSTRY_CATEGORY_INVALID = "Invalid Industry Category"; 

    /**
     *territory categories
     */
    public static final String TERRITORY_CATEGORY_CREATED = "Territory Category added successfully";
    public static final String TERRITORY_CATEGORY_UPDATED = "Territory Category updated successfully";
    public static final String TERRITORY_CATEGORY_DELETE = "Territory Category deleted successfully";
    public static final String TERRITORY_CATEGORY_NOT_DELETE = "Territory Categorynot not deleted, please try again.";
    public static final String TERRITORY_CATEGORY_INVALID = "Invalid Territory Category";
   
    /**
     *appointment messages
     */
    public static final String APPOINTMENT_CREATE_SUCCESS = "Visit created successfully";
    public static final String APPOINTMENT_DELETE_SUCCESS = "Visit Deleted Successfully";
    public static final String APPOINTMENT_UPDATE_SUCCESS = "Visit Updated successfully";

    /**
     *BCC email address to know the account creation.
     */
    public static final String BCC_EMAIL_ADDRESS1 = FieldSenseUtils.getPropertyValue("BCC_EMAIL_ADDRESS1");
    public static final String BCC_EMAIL_ADDRESS2 = FieldSenseUtils.getPropertyValue("BCC_EMAIL_ADDRESS2");
    public static final String CUSTOM_FORM_XML_UPLOAD_PATH = FieldSenseUtils.getPropertyValue("CUSTOM_FORM_XML_UPLOAD_PATH");
    public static final String API_GOOGLE_GEOCODING_KEY =FieldSenseUtils.getPropertyValue("API_GOOGLE_GEOCODING_KEY");
    public static final String CACHE_LOCATION_PRECISION =FieldSenseUtils.getPropertyValue("CACHE_LOCATION_PRECISION");
    public static final String MOBILE_APP_DEBUG_UPLOAD_PATH =FieldSenseUtils.getPropertyValue("MOBILE_APP_DEBUG_UPLOAD_PATH");
    public static final String MOBILE_TRAVEL_LOG_UPLOAD_PATH =FieldSenseUtils.getPropertyValue("MOBILE_TRAVEL_LOG_UPLOAD_PATH");
    public static final String APN_CERTIFICATE =FieldSenseUtils.getPropertyValue("APN_CERTIFICATE");
    public static final String APN_PASSWORD =FieldSenseUtils.getPropertyValue("APN_PASSWORD");

    /**
     * 1 for IOS_ENVIRONMENT_MODE, 0 for android
     */
    public static final boolean IOS_ENVIRONMENT_MODE =FieldSenseUtils.getPropertyValue("IOS_ENVIRONMENT_MODE").equals("1")?true:false; // o means sandbox , 1 means production
    public static final String VERIFY_LINK = FieldSenseUtils.getPropertyValue("VERIFY_LINK"); // Added by Jyoti
    /**
     * Mail values
     */
    public static final String MAIL_PROTOCOL = FieldSenseUtils.getPropertyValue("MAIL_PROTOCOL");
    public static final String MAIL_HOST_IP = FieldSenseUtils.getPropertyValue("MAIL_HOST_IP");
    public static final String MAIL_SENDER_ID = FieldSenseUtils.getPropertyValue("MAIL_SENDER_ID");
    public static final String MAIL_AUTH_ID = FieldSenseUtils.getPropertyValue("MAIL_AUTH_ID");
    public static final String MAIL_AUTH_PASSWORD = FieldSenseUtils.getPropertyValue("MAIL_AUTH_PASSWORD");
    public static final String MAIL_CC_ID = FieldSenseUtils.getPropertyValue("MAIL_CC_ID");
    
    public static final String APP_VERSION_CHECK = FieldSenseUtils.getPropertyValue("APP_VERSION_CHECK");
    
    /**
     * @purpose google reCaptcha parameters
     * @Added by jyoti, 18-02-2018
     * @Parameters DO NOT CHANGE THE STRING NAME
     */
    public static final String GOOGLE_RECAPTCHA_DOMAIN_SECRET_KEYNAME = "secret";
    public static final String GOOGLE_RECAPTCHA_USER_RESPONSE_KEYNAME = "response";
    
    /**
     * @Added by Jyoti, 18-02-2018
     */
    public static final String GOOGLE_RECAPTCHA_DOMAIN_SECRET_VALUE = FieldSenseUtils.getPropertyValue("GOOGLE_RECAPTCHA_DOMAIN_SECRET_VALUE");
    public static final String GOOGLE_RECAPTCHA_URL = FieldSenseUtils.getPropertyValue("GOOGLE_RECAPTCHA_URL");
    
    /**
     * @Added by manohar mar 23 2018
     */
    
}
