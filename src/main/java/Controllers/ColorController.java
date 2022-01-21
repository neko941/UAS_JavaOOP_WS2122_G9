/**
 * Author: neko941
 * Created on: 2022-01-21
 */
package Controllers;

import javafx.scene.control.Label;

import static Controllers.ConfigController.getDataFromConfig;

public class ColorController {
    static String errorColor = getDataFromConfig("errorColor");
    static String validColor = getDataFromConfig("validColor");
    static String defaultColor = getDataFromConfig("defaultColor");

    public static boolean changeTextFieldColor(boolean check, boolean empty, Label label)
    {
        if(empty)
        {
            label.setStyle("-fx-text-fill:" + defaultColor);
        }
        else {
            if (check)
            {
                label.setStyle("-fx-text-fill:" + validColor);
            }
            else
            {
                label.setStyle("-fx-text-fill:" + errorColor);
            }
        }
        return check;
    }
}
