/**
 * @author neko941
 * Created on: 2022-01-21
 */
package Controllers;

import javafx.scene.control.Label;

import static Controllers.ConfigController.getDataFromConfig;

public class ColorController {
    static String errorColor = getDataFromConfig("color", "errorColor");
    static String validColor = getDataFromConfig("color", "validColor");
    static String defaultColor = getDataFromConfig("color", "defaultColor");

    /**
     * Change color of the Label
     *
     * @param check boolean value to change color for the text
     * @param empty true if label is empty
     * @param label the Label that needs to be changed color
     * @return boolean value of the check variable
     */
    public static boolean changeLabelColor(boolean check, boolean empty, Label label)
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

    /**
     * Set text and change color for Label
     *
     * @param check boolean value to change color for the text
     * @param empty <code>true</code> if label is empty
     * @param label the Label that needs to be set text/ changed color
     * @param trueString the string to set to the Label if "check" is <code>true</code>
     * @param falseString the string to set to the Label if "check" is <code>false</code>
     * @return boolean value of the check variable
     */
    public static boolean changeLabelText(boolean check, boolean empty, Label label, String trueString, String falseString)
    {
        if(empty)
        {
            label.setText("");
        }
        else
        {
            if (check)
            {
                label.setText(trueString);
                label.setStyle("-fx-text-fill:" + validColor);
            }
            else
            {
                label.setText(falseString);
                label.setStyle("-fx-text-fill:" + errorColor);
            }
        }
        return check;
    }
}
