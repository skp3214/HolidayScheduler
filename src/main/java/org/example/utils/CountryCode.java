package org.example.utils;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CountryCode {
    private static final String API_URL = "https://api.first.org/data/v1/countries";

    public static Map<String, String> getCountryCodeMap() throws IOException {
        Map<String, String> countryCodeMap = new HashMap<>();

        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = inputReader.readLine()) != null) {
                response.append(inputLine);
            }
            inputReader.close();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonObject dataObject = jsonObject.getAsJsonObject("data");

            for (Map.Entry<String, JsonElement> entry : dataObject.entrySet()) {
                String countryCode = entry.getKey();
                JsonObject countryObject = entry.getValue().getAsJsonObject();
                String countryName = countryObject.get("country").getAsString();
                countryCodeMap.put(countryName, countryCode);
            }
        } else {
            System.out.println("HTTP Response Code: " + responseCode);
        }


        return countryCodeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static void main(String[] args) {
        try {
            Map<String, String> countryCodeMap = getCountryCodeMap();
            System.out.println(countryCodeMap);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
