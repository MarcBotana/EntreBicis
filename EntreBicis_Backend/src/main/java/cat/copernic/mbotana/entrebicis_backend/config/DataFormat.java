package cat.copernic.mbotana.entrebicis_backend.config;

public class DataFormat {

    

    //Data Format - User
    public static final int MAX_USR_NAME_LENGTH = 10;
    public static final int MAX_USR_SURNAME_LENGTH = 25;
    public static final int MAX_OBSERVATION_LENGTH = 500;
    public static final int MIN_USR_PASS_LENGTH = 8;
    public static final int MAX_USR_PASS_LENGTH = 16;
    public static final int USR_MOBILE_LENGTH = 9;

    //Password Format - User
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 12;

    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";
    public static final String SPECIAL_CHAR = "!@#$%^&*()-_+=<>?";

    //Data Format - Reward
    public static final int MAX_REW_NAME_LENGTH = 50;
    public static final int MAX_REW_DESC_LENGTH = 300;
    public static final int MAX_REW_OBS_LENGTH = 150;

    //Data Format - Token
    public static final int MAX_TOKEN_LENGTH = 8;

    //Data Format - System Params

    public static final int MIN_SYS_VEL = 1;
    public static final int MAX_SYS_VEL = 120;
    public static final int MIN_SYS_POINT = 1;
    public static final int MAX_SYS_POINT = 99;




}
