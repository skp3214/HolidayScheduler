package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AllHolidays {
    private final String apiUrl;

    public AllHolidays(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public List<Holiday> fetchAllHolidays() {
        List<Holiday> holidays = new ArrayList<>(); // Use ArrayList for the list

        try {
            InputStream inputStream = new URL(apiUrl).openStream();
            Scanner scanner = new Scanner(inputStream);
            String responseBody = scanner.useDelimiter("\\A").next();
            scanner.close();

            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray holidaysArray = jsonObject.getAsJsonObject("response").getAsJsonArray("holidays");

            for (JsonElement holidayElement : holidaysArray) {
                JsonObject holidayObject = holidayElement.getAsJsonObject();
                String name = holidayObject.get("name").getAsString();
                String dateIso = holidayObject.getAsJsonObject("date").get("iso").getAsString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(dateIso);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String dayOfWeek = new SimpleDateFormat("EEEE").format(calendar.getTime());

                Holiday holiday = new Holiday(name, dayOfWeek, dateIso);
                holidays.add(holiday);
            }

        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }

        return holidays;
    }

}
