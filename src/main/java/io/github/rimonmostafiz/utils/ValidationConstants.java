package io.github.rimonmostafiz.utils;

/**
 * @author Rimon Mostafiz
 */
public class ValidationConstants {

    public final static String ALPHANUMERIC_UNDERSCORE_DOT = "^[a-zA-Z0-9](?:[a-zA-Z0-9_.]*[a-zA-Z0-9])?$";
    public final static String ALPHABET_SPACE_HYPHEN_DOT = "^[a-zA-Z](?:[a-zA-Z\\s-.]*[a-zA-Z])?$";
    public final static String ALPHANUMERIC_SPACE_HYPHEN_DOT = "^[a-zA-Z0-9](?:[a-zA-Z0-9\\s-.]*[a-zA-Z0-9])?$";
    public final static String ALPHANUMERIC_SPACE_HYPHEN_UNDERSCORE_DOT = "^[a-zA-Z0-9](?:[a-zA-Z0-9\\s-_.]*[a-zA-Z0-9])?$";
    public final static String ALPHANUMERIC_AND_SPECIAL_CHAR = "^[a-zA-Z0-9](?:[a-zA-Z0-9\\s-._#,]*[a-zA-Z0-9])?$";
    public final static String DOMAIN_NAME_PATTERN = "^[a-z.](?:[a-z\\s.]*[a-z])?$";
    public final static String NUMERIC="^[0-9]*$";

    public final static int MAX_USERNAME_SIZE = 32;
    public final static int MAX_EMAIL_SIZE = 64;
    public final static int MIN_PASSWORD_SIZE = 6;
    public final static int MAX_PASSWORD_SIZE = 32;
    public final static int MAX_FIRST_NAME_SIZE = 64;
    public final static int MAX_LAST_NAME_SIZE = 64;
}
