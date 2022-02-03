/**
 * @author neko941
 * Created on: 2022-01-21
 */
package Controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ConfigController {
    /**
     *
     * @param key key of the dictionary in the config.txt
     * @param item item of the key of the dictionary in the config.txt
     * @return data in the config.txt file
     */
    public static String getDataFromConfig(String key, String item)
    {
        JSONParser parser = new JSONParser();

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/config.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        assert jsonObject != null;
        JSONObject jsonObject1 = (JSONObject) jsonObject.get(key);
        return (String) jsonObject1.get(item);
    }
}


