package org.aldousdev.notificationservice.service;

import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class TelegramSender {
    private final String BOT_TOKEN = "YOUR_BOT_TOKEN";
    private final String CHAT_ID = "DEFAULT_CHAT_ID"; // можно передавать

    public boolean send(String chatId, String message) {
        try {
            String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + chatId + "&text=" + URLEncoder.encode(message, "UTF-8");
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}

