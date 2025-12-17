package com.ai.studyplanner.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenAiClient {

    private static final String OPENAI_URL =
            "https://api.openai.com/v1/responses";


    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    public String chat(String systemPrompt, String userPrompt) throws IOException {

        ObjectNode requestJson = mapper.createObjectNode();
        requestJson.put("model", model);

        ObjectNode inputObj = mapper.createObjectNode();
        inputObj.put("role", "user");
        inputObj.put("content", systemPrompt + "\n\n" + userPrompt);

        requestJson.set("input", mapper.createArrayNode().add(inputObj));

        Request request = new Request.Builder()
                .url(OPENAI_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        requestJson.toString(),
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = client.newCall(request).execute()) {

            String body = response.body().string();

            if (!response.isSuccessful()) {
                throw new IOException("OpenAI API error: " + body);
            }

            JsonNode root = mapper.readTree(body);

            JsonNode output = root.path("output");
            JsonNode content = output.get(0).path("content");
            JsonNode textNode = content.get(0).path("text");

            return textNode.asText();
        }
    }
}
