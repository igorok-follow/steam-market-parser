import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

class JsonReader {

    private ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    private File file = new File(Objects.requireNonNull(classLoader.getResource("prices.json")).getFile());

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
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        assert jsonObject != null;
        return String.valueOf(jsonObject.get("lowest_price"));
    }

}
