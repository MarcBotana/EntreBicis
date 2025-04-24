package cat.copernic.mbotana.entrebicis_backend.config;

public class ErrorMessage {

    //Exception Messages BDD
    public static final String DATA_ACCESS_EXCEPTION = "Error amb la BDD! ";
    public static final String SQL_EXCEPTION = "Error SQL! ";
    public static final String GENERAL_EXCEPTION = "Error General! ";

    //Error Messages Validation
    public static final String NOT_BLANK = "El camp no pot estar buit!";

    //Error Messages Validation - Login
    public static final String AUTH_ERROR = "Credencials incorrectes!";
    public static final String DENIED_ERROR = "Accés denegat!";
    
    //Error Messages Validation - User
    public static final String USR_NAME_LENGTH = "Màxim " + DataFormat.MAX_USR_NAME_LENGTH + " caràcters!";
    public static final String USR_SURNAME_LENGTH = "Màxim " + DataFormat.MAX_USR_SURNAME_LENGTH + "caràcters!";
    public static final String USR_OBSERVATION_LENGTH = "Màxim " + DataFormat.MAX_OBSERVATION_LENGTH + "caràcters!";
    public static final String USR_MOBILE_LENGTH = "El mòbil no es vàlid! (" + DataFormat.USR_MOBILE_LENGTH + " digits)";
    public static final String EMAIL_FORMAT = "El format de no és correcte! (exemple@exemple.com)";
    public static final String EMAIL_EXIST = "El correu introduït ja existeix!";
    public static final String IMAGE_TYPE = "El format de l'imatge no es vàlid! (Només: .jpeg)";

                                              
    //Error Messages Validation - Reward
    public static final String REW_NAME_LENGTH = "Màxim " + DataFormat.MAX_REW_NAME_LENGTH + " caràcters!";
    public static final String REW_DESC_LENGTH = "Màxim " + DataFormat.MAX_REW_DESC_LENGTH + " caràcters!";
    public static final String REW_OBS_LENGTH = "Màxim " + DataFormat.MAX_REW_OBS_LENGTH + " caràcters!";

    //Error Messages Validation - System Params
    public static final String SYS_VEL_LENGTH = "El valor no es vàlid! (" + DataFormat.MIN_SYS_VEL + " - " + DataFormat.MAX_SYS_VEL + ")";
    public static final String SYS_POINT_LENGTH = "El valor no es vàlid! (" + DataFormat.MIN_SYS_POINT + " - " + DataFormat.MAX_SYS_POINT + ")";

    //Error Messages Validation - Update Password
    public static final String TOKEN_NOT_FOUND = "No s'ha trobat el Codi, torna a enviar el correu!";
    public static final String TOKEN_EXPIRED = "El Codi s'ha expirat, torna a enviar el correu!";
    public static final String PASS_NOT_MATCH = "Les contrasenyes han de coincidir!";
    public static final String EMAIL_NOT_EXIST = "El correu introduït no existeix!";

    






}   
