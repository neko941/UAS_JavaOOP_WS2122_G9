/**
 * @author neko941
 * Created on: 2021-12-13
 *
 * This class provides methods to validate data
 */

package Controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Validation
{
//    public static final String USERNAME_CONSTRAINT = "[\\w^\\p{Blank}]+";
//    public static final String PHONE_CONSTRAINT = "[\\p{Digit}]+";
    private static final String EMAIL_CONSTRAINT = "^([\\w_.\\-+])+@([\\w\\-]+\\.)+([\\w]{2,10})+$";

    /**
     *
     * @param string the string needs to be checked
     * @param min minimum length
     * @param max maximum length
     * @return <code>true</code> if the length of the string is between min and max
     *         <code>false</code> otherwise
     */
    public static boolean checkLength(String string, int min, int max)
    {
        return string.length() <= max && string.length() >= min;
    }

    /**
     *
     * @param string the string needs to be checked
     * @param min minimum length
     * @return <code>true</code> if the length of the string is between min and infinity
     *         <code>false</code> otherwise
     */
    public static boolean checkLength(String string, int min)
    {
        return checkLength(string, min, 100);
    }

    /**
     *
     * @param string the string needs to be checked
     * @return <code>true</code> if the string has no space
     *         <code>false</code> otherwise
     */
    public static boolean checkNoSpace(String string)
    {
        return !string.contains(" ");
    }

    /**
     *
     * @param string the string needs to be checked
     * @return <code>true</code> if the string contains at least a digit
     *         <code>false</code> otherwise
     */
    public static boolean checkDigit(String string)
    {
        return string.matches(".*[0-9].*");
    }

    /**
     *
     * @param string the string needs to be checked
     * @return <code>true</code> if the string contains at least a lowercase character
     *         <code>false</code> otherwise
     */
    public static boolean checkLower(String string)
    {
        return string.matches(".*[a-z].*");
    }

    /**
     *
     * @param string the string needs to be checked
     * @return <code>true</code> if the string contains at least an uppercase character
     *         <code>false</code> otherwise
     */
    public static boolean checkUpper(String string)
    {
        return string.matches(".*[A-Z].*");
    }

    /**
     *
     * @param string the string needs to be checked
     * @return <code>true</code> if the string contains at least a punctuation
     *         <code>false</code> otherwise
     */
    public static boolean checkPunctuation(String string)
    {
        Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasSpecial = special.matcher(string);
        return hasSpecial.find();
    }

    /**
     *
     * @param string the string needs to be checked
     * @return <code>true</code> if the string just contains digits and alphabet characters
     *         <code>false</code> otherwise
     */
    public static boolean checkNoSpecialCharacter(String string)
    {
        return Pattern.compile ("^[a-zA-Z0-9\\s]*$").matcher(string).find();
    }

    /**
     *
     * @param string the string needs to be checked
     * @return <code>true</code> if the string is in the valid email form
     *         <code>false</code> otherwise
     */
    public static boolean checkInputEmail(String string)
    {
        return Stream.of(string.matches(EMAIL_CONSTRAINT),
                        checkLength(string, 1))
                .allMatch(val -> val);
    }
}