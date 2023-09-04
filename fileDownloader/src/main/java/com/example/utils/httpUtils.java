package com.example.utils;

import java.net.HttpURLConnection;
import java.net.URL;


public class httpUtils {

    public void getHttpInfo(String urlStr){
        try {
            URL url = new URL(urlStr);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Handle the response
                System.out.println("Response: " + connection.getResponseMessage());
                System.out.println("Headers Field: " +connection.getHeaderFields());
                System.out.println("Content type: " +connection.getContentType());

            } else {
                System.out.println("Error: " + responseCode);
            }

            // Close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getContentLength(String urlStr){
        try {
            URL url = new URL(urlStr);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Handle the response
                System.out.println("Response: " + connection.getHeaderFields());

                return connection.getHeaderField("Content-Length");

            } else {
                System.out.println("Error: " + responseCode);
            }

            // Close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
