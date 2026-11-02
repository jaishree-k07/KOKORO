
package com.kokoro.ai;

import com.kokoro.model.ChatMessage;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LlmClient {

    private final String apiKey;
    private final String url;
    private final String model;

    private final RestClient http = RestClient.create();

    public LlmClient(
            @Value("${llm.api-key}") String apiKey,
            @Value("${llm.url}") String url,
            @Value("${llm.model}") String model) {

        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.url = url.trim();
        this.model = model.trim();

        System.out.println("AIMLAPI configured");
        System.out.println("Model: " + this.model);
        System.out.println("API URL: " + this.url);
    }

    public String complete(
            String systemPrompt,
            List<ChatMessage> history,
            String userMessage) {

        if (apiKey.isBlank()) {
            return "I'm here with you 💜 Please configure the AIMLAPI API key.";
        }

        try {

            List<Map<String, String>> msgs = new ArrayList<>();

            // Companion personality
            msgs.add(Map.of(
                    "role", "system",
                    "content", systemPrompt
            ));

            // Include only the latest 12 conversation messages
            if (history != null && !history.isEmpty()) {

                int start = Math.max(0, history.size() - 12);

                for (int i = start; i < history.size(); i++) {

                    ChatMessage m = history.get(i);

                    msgs.add(Map.of(
                            "role", m.getRole(),
                            "content", m.getMessage()
                    ));
                }
            }

            // Current user message
            msgs.add(Map.of(
                    "role", "user",
                    "content", userMessage
            ));

            Map<String, Object> request = Map.of(
                    "model", model,
                    "messages", msgs,
                    "max_completion_tokens", 400
            );

            JsonNode response = http.post()
                    .uri(url)
                    .header(
                            "Authorization",
                            "Bearer " + apiKey
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null ||
                    !response.path("choices").isArray() ||
                    response.path("choices").isEmpty()) {

                System.err.println("AIMLAPI returned an empty response.");

                return "I'm here with you 💜 Could you tell me that again?";
            }

            String reply = response
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText("");

            if (reply.isBlank()) {
                return "I'm here with you 💜 Could you tell me that again?";
            }

            return reply.trim();

        } catch (RestClientResponseException e) {

            System.err.println(
                    "AIMLAPI HTTP error: " + e.getStatusCode()
            );

            System.err.println(
                    "AIMLAPI response: " + e.getResponseBodyAsString()
            );

            return "I'm having trouble connecting right now. Please try again in a moment. 💜";

        } catch (Exception e) {

            System.err.println(
                    "AIMLAPI error: " + e.getMessage()
            );

            return "I'm having trouble connecting right now. Please try again in a moment. 💜";
        }
    }
}
