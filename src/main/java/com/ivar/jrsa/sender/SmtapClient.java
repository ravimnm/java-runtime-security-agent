package com.ivar.jrsa.sender;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivar.jrsa.event.SecurityEvent;

public class SmtapClient {

    private static final String ENDPOINT =
            "http://localhost:8080/security/events";

    private static final String TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGUiOiJBRE1JTiIsInRlbmFudElkIjoidGVuYW50XzEiLCJpYXQiOjE3ODIyOTQ4MjUsImV4cCI6MTc4MjM4MTIyNX0.-5wwg1xPT4nImEXt7RjdCzWF7XAReVU9ECfX1ZZPlUg";

    private static final ObjectMapper mapper =
            new ObjectMapper();

    public static void send(SecurityEvent event) {

        try {

            URL url = URI.create(ENDPOINT).toURL();

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty(
                    "Content-Type",
                    "application/json");

            connection.setRequestProperty(
                    "Authorization",
                    "Bearer " + TOKEN);

            connection.setDoOutput(true);

            String json =
                    mapper.writeValueAsString(event);

            try (OutputStream os =
                         connection.getOutputStream()) {

                os.write(json.getBytes());
            }

            System.out.println(
                    "SMTAP response: "
                    + connection.getResponseCode());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}