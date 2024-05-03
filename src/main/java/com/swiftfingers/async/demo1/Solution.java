package com.swiftfingers.async.demo1;

import org.apache.tomcat.util.json.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Solution {
    public static void main(String[] args) {
        //Question 1
//        int k = 3;
//        List<Integer> scores = Arrays.asList(100, 50, 50, 25);
//
//        int result = numPlayers(k, scores);
//        System.out.println("Number of players who can level up: " + result);


        //Question 2
        String statusQuery = "MALFUNCTIONING";
        int threshold = 50;
        String dateStr = "03-2022";

        int res = numDevices(statusQuery, threshold, dateStr);
        System.out.println("Number of matching devices: " + res);
    }


    public static int numDevices(String statusQuery, int threshold, String dateStr) {
        try {
            // Convert date string to UTC milliseconds
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-YYYY");
            Date date = dateFormat.parse(dateStr);
            long utcTimestamp = date.getTime();

            // Make HTTP request
            int totalDevices = 0;
            int currentPage = 1;
            int totalPages = Integer.MAX_VALUE;

            while (currentPage <= totalPages) {
                String apiUrl = "https://jsonmock.hackerrank.com/api/iot_devices/search"
                        + statusQuery + "&page=" + currentPage;


                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }


                    // Parse JSON response using JSON Simple
                    org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
                    JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
                    totalPages = ((Long) jsonResponse.get("total_pages")).intValue();

                    // Process data array
                    JSONArray dataArray = (JSONArray) jsonResponse.get("data");
                    for (Object deviceObj : dataArray) {
                        JSONObject device = (JSONObject) deviceObj;

                        // Check if device was added during the given month and year
                        long deviceTimestamp = (Long) device.get("timestamp");
                        if (deviceTimestamp == utcTimestamp) {

                            // Check if root threshold is greater than the given threshold
                            JSONObject operatingParams = (JSONObject) device.get("operatingParams");
                            long rootThreshold = (Long) operatingParams.get("rootThreshold");

                            if (rootThreshold > threshold) {
                                totalDevices++;
                            }
                        }
                    }

                    reader.close();
                }

                connection.disconnect();
                currentPage++;
            }

            return totalDevices;

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate an error
        }
    }

    public static int numPlayers(int k, List<Integer> scores) {
        int numPlayers = 0; //to keep track of the number of players who can level up

        // Convert the list to an array for easier sorting
        Integer[] scoresArray = new Integer[scores.size()];
        scores.toArray(scoresArray);

        // Sort the array in descending order using lambda expression
        Arrays.sort(scoresArray, (a, b) -> b - a);

        // Iterate through the sorted array to determine the rank
        int currentRank = 1;
        for (int i = 0; i < scoresArray.length; i++) {

            if (i > 0 && !scoresArray[i].equals(scoresArray[i - 1])) {
                currentRank = i + 1;
            }

            // Check if the player qualifies for leveling up
            if (currentRank <= k && scoresArray[i] > 0) {
                numPlayers++;
            }

            System.out.println("[current rank = " + currentRank + "] " +
                    "[i = " + i + "] [Num players = " + numPlayers + "]");
        }

        return numPlayers;
    }
}
