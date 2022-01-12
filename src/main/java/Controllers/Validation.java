/**
 * Author: neko941
 * Created on: 2021-12-13
 *
 * This class provides methods to validate dara
 */

package Controllers;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class Validation
{
    //    public static final String USERNAME_CONSTRAINT = "[\\w^\\p{Blank}]+";
    public static final String PHONE_CONSTRAINT = "[\\p{Digit}]+";
    private static final String EMAIL_CONSTRAINT = "^([\\w_.\\-+])+@([\\w\\-]+\\.)+([\\w]{2,10})+$";
    public static Scanner in = new Scanner(System.in);

    public static boolean checkLength(String string, int min, int max) {return string.length() <= max && string.length() >= min;}
    public static boolean checkLength(String string, int min) {return checkLength(string, min, 100);}
    public static boolean checkNoSpace(String string) {return !string.contains(" ");}
    public static boolean checkDigit(String string) {return string.matches(".*[0-9].*");}
    public static boolean checkLower(String string) {return string.matches(".*[a-z].*");}
    public static boolean checkUpper(String string) {return string.matches(".*[A-Z].*");}

    public static boolean checkPunctuation(String string)
    {
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasSpecial = special.matcher(string);
        return hasSpecial.find();
    }

    public static boolean checkNoSpecialCharacter(String string)
    {
        return Pattern.compile ("^[a-zA-Z0-9\\s]*$").matcher(string).find();
    }

    public static boolean checkInputEmail(String string)
    {
        return Arrays   .asList(string.matches(EMAIL_CONSTRAINT),
                        checkLength(string, 1))
                .stream()
                .allMatch(val -> val == true);
    }
}