package cat.copernic.mbotana.entrebicis_backend.config;

public class DataFormat {

    //Data Format - User
    public static final int MAX_USR_NAME_LENGTH = 10;
    public static final int MAX_USR_SURNAME_LENGTH = 25;
    public static final int MIN_USR_PASS_LENGTH = 8;
    public static final int MAX_USR_PASS_LENGTH = 16;
    public static final int USR_MOBILE_LENGTH = 9;

    //Password Format - User
    public static final String SPECIAL_CHAR = "@#$%^&+=";

    //Data Format - Reward
    public static final int MAX_REW_NAME_LENGTH = 50;
    public static final int MAX_REW_DESC_LENGTH = 300;
    public static final int MAX_REW_OBS_LENGTH = 150;

    /*--------------------------------------------------------------------------------------*/

    //Password - REGEX
    private static final String regexMinMax = ".{" + MIN_USR_PASS_LENGTH + "," + MAX_USR_PASS_LENGTH + "}";
    private static final String regexNum = "(?=.*[0-9])";
    private static final String regexLowercase = "(?=.*[a-z])";
    private static final String regexUppercase = "(?=.*[A-Z])";
    private static final String regexSpecial = "(?=.*[" + SPECIAL_CHAR + "])";
    private static final String regexNoWhitespace = "(?=\\S+$)";
    
    private static final String regexMinMaxMsg = " - Entre " + MIN_USR_PASS_LENGTH + " i " + MAX_USR_PASS_LENGTH + " caràcters! <br> ";
    private static final String regexNumMsg = " - Almenys un número! <br> ";
    private static final String regexLowercaseMsg = " - Almenys una lletra minúscula! <br> ";
    private static final String regexUppercaseMsg = " - Almenys una lletra majúscula! <br> ";
    private static final String regexSpecialMsg = " - Almenys un caràcter especial! (" + SPECIAL_CHAR + ") <br> ";
    private static final String regexNoWhitespaceMsg = " - Sense espais en blanc! <br> ";

    public static final String USR_PASS_REGEX = "^" +
        regexMinMax +
        regexNum + 
        regexLowercase + 
        regexUppercase + 
        regexSpecial + 
        regexNoWhitespace +      
        "$";

    public static final String PASS_FORM_ERROR = 
        regexMinMaxMsg + 
        regexNumMsg + 
        regexLowercaseMsg + 
        regexUppercaseMsg + 
        regexSpecialMsg + 
        regexNoWhitespaceMsg;

}
