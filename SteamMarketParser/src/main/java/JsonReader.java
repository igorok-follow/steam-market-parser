import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class JsonReader {

    private File file = new File("prices.json");

    String getPrice() {
        JSONObject jsonObject = null;
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.nextLine() != null) {
                JSONParser jsonParser = new JSONParser();
                try {
                    jsonObject = (JSONObject) jsonParser.parse(new FileReader(file));
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Json file is empty");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        assert jsonObject != null;
        return String.valueOf(jsonObject.get("lowest_price"));
    }

}
