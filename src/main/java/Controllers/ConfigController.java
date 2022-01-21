package Controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ConfigController {

    public static String getDataFromConfig(String text)
    {
        JSONParser parser = new JSONParser();

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/config.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return (String) jsonObject.get(text);
    }
}
