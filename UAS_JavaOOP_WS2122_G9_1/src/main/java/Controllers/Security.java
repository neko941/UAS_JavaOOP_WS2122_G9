/**
 * Author: neko941
 * Created on: 2021-12-13
 *
 * This class provides methods to hash and generate random string
 */

package Controllers;

import org.apache.commons.lang3.RandomStringUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class Security {
    public static String generateRandomString()
    {
        return RandomStringUtils.random(
                15,
                0,
                93,
                false,
                false,
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?".toCharArray(),
                new SecureRandom()
        );
    }

    public static String generateRandomNumber()
    {
        java.util.Random rng = new java.util.Random();
        return String.format("%06d-%06d", rng.nextInt(999999), rng.nextInt(999999));
    }

    public static String sha512(String password) {
        try {
            // Static getInstance method is called with hashing SHA
            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            //return array of byte of the password
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert array of byte digest into hex value
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash)
            {
                // Convert message digest into hex value
                String hex = Integer.toHexString(0xff & b);
                // Pad with leading zeros
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // return hashed text
            return hexString.toString();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}