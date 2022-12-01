package fr.esgi.cleancode.validators;

public class SocialSecurityNumberValidator {
    private static final String SOCIAL_SECURITY_NUMBER_PATTERN = "^[0-9]{15}$";
    public static boolean isValid(String socialSecurityNumberToValidate){
        if(socialSecurityNumberToValidate == null){
            return false;
        }
        return socialSecurityNumberToValidate.matches(SOCIAL_SECURITY_NUMBER_PATTERN);
    }
}
