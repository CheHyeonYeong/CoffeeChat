package com.backend.coffeeChat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AlimtalkService {
    @Value("${solapi.api.key:YOUR_API_KEY}")
    private String apiKey;

    @Value("${solapi.api.secret:YOUR_API_SECRET}")
    private String apiSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendAlimtalk(String to, String templateCode, Map<String, String> variables) {
        // Construct request body for Solapi.
        Map<String, Object> body = new HashMap<>();
        body.put("to", to);
        body.put("templateId", templateCode);
        body.put("kakaoOptions", Map.of("variables", variables));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "HMAC " + apiKey + ":" + apiSecret);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Actual API endpoint may differ.
        String url = "https://api.solapi.com/messages/v4/send";
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
