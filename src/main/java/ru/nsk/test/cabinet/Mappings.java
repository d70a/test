package ru.nsk.test.cabinet;

/**
 *
 * @author me
 */
public class Mappings {

    public final static String UI = "app";
    
    public final static String SPRING_SECURE_LOGIN = "/j_spring_security_check";
    
    /**
     * Endpoints
     */
    // root
    public final static String ROOT = "/";
    // spring security endpoints
    public final static String AUTH = "/auth";
    public final static String LOGIN = "/login";
    public final static String REGISTER = "/register";
    public final static String AUTH_LOGIN = AUTH+LOGIN;
    public final static String AUTH_REGISTER = AUTH+REGISTER;
    public final static String AUTH_FIELD_LOGIN = "j_username";
    public final static String AUTH_FIELD_PASSWORD = "j_password";
    // user REST endpoints
    public final static String USER = "/user";
    public final static String IDCARD = "/idcard";
    public final static String PLACE = "/place";
    
    // recomended actions for client
    public final static String A_NEEDED_REG_STEP_1 = "NEEDED_REG_STEP_1";
    public final static String A_NEEDED_REG_STEP_2 = "NEEDED_REG_STEP_2";
    public final static String A_NEEDED_REG_STEP_3 = "NEEDED_REG_STEP_3";
    public final static String A_RELOGIN = "NEEDED_LOGIN";
    public final static String A_VALID = "USER_VALID";
    
    public final static String EMPTY = "";
}
