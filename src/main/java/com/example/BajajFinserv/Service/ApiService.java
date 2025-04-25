package com.example.BajajFinserv.Service;

import com.example.BajajFinserv.Util.SolutionProcessor;
import com.example.BajajFinserv.model.webHookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    private final String regNo = "REG12347";
    private final String name = "John Doe";
    private final String email = "john@example.com";

    public void execute() {

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("regNo", regNo);
        requestBody.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<webHookResponse> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(
                    "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook",
                    entity,
                    webHookResponse.class
            );
        } catch (Exception e) {
            System.err.println("Failed to call generateWebhook: " + e.getMessage());
            return;
        }

        webHookResponse response = responseEntity.getBody();
        if (response == null) {
            System.err.println("Empty response received");
            return;
        }

        List<List<Integer>> outcome;

        try {
            String digits = regNo.replaceAll("[^0-9]", "");
            int lastTwoDigits = Integer.parseInt(digits.substring(digits.length() - 2));

            if (lastTwoDigits % 2 == 1) {
                outcome = SolutionProcessor.solve(response);
            } else {
                outcome = Collections.singletonList(SolutionProcessor.solveNthLevel(response.getData()));
            }
        } catch (Exception e) {
            System.err.println("Error processing logic: " + e.getMessage());
            return;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("regNo", regNo);
        result.put("outcome", outcome);

        int maxRetries = 4;
        int attempts = 0;
        boolean success = false;

        while (attempts < maxRetries && !success) {
            try {
                HttpHeaders webhookHeaders = new HttpHeaders();
                webhookHeaders.set("Authorization", response.getAccessToken());
                webhookHeaders.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, Object>> webhookEntity = new HttpEntity<>(result, webhookHeaders);

                restTemplate.postForEntity(response.getWebhook(), webhookEntity, String.class);
                System.out.println("✅ Successfully posted to webhook on attempt " + (attempts + 1));
                success = true;  // Mark as successful
            } catch (Exception e) {
                attempts++;
                System.err.println("Retry " + attempts + " failed: " + e.getMessage());
                if (attempts >= maxRetries) {
                    System.err.println("All retry attempts failed.");
                }
            }
        }
    }
}
