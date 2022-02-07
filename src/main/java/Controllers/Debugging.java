/**
 * @author neko941
 */
package Controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Debugging
{
    /**
     * Print text in console for debugging
     *
     * @param text text that need to be printed
     */
    public static void printNotificationInConsole(String text)
    {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.format("%s\t\t%s\n", time, text);
    }
}

