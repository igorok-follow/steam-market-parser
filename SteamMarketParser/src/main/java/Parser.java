import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

class Parser {

    private final String link;
    private String requestLink =
            "https://steamcommunity.com/market/priceoverview/?currency=3&appid=730&market_hash_name=";
    private static String userLink =
            "https://steamcommunity.com/market/listings/730/StatTrak%E2%84%A2%20P250%20%7C%20Steel%20Disruption%20%28Factory%20New%29";

    double eurCourse;

    Parser(String link) {
        this.link = link;
        writeJson(requestLink + splitLink(link));
        parseCourse();
    }

    private void parseCourse() {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        String month = "";
        if (String.valueOf(localDate.getMonth()).length() == 1) {
            month = "0" + localDate.getMonth();
        }
        String day = "";
        if (String.valueOf(localDate.getDayOfMonth()).length() == 1) {
            day = "0" + localDate.getMonth();
        }
        try {
            Document document = Jsoup.connect(
                    "http://cbr.ru/currency_base/daily/?date_req=" + day + "." + month + "." + year).get();
            Elements tds = document.getElementsByTag("td");
            eurCourse = Double.parseDouble(tds.get(59).text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getPrice() {
        int price = Integer.parseInt(new JsonReader().getPrice().split(" ", 2)[0]);
        return String.valueOf(price * eurCourse);
    }

    private void writeJson(String fileFill) {
        try {
            FileWriter fileWriter = new FileWriter("prices.json");
            fileWriter.write(getURLSource(fileFill));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String splitLink(String link) {
        return link.split("/", 7)[6];
    }

    public String getURLSource(String url) throws IOException
    {

        URL urlObject = new URL(url);
        URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        return toString(urlConnection.getInputStream());
    }

    private String toString(InputStream inputStream) throws IOException
    {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)))
        {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        }
    }

}
