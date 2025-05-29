package com.mycompany.renovai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class Servidor {
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public String conversa(String mensagem, listaMensagens listaMensagem) throws Exception {
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject()
            .put("role", "system")
            .put("content",
                "Você é um chatbot especialista em sustentabilidade, meio ambiente e energias renováveis. " +
                "Responda somente perguntas relacionadas a esses temas. " +
                "Se o usuário perguntar qualquer outra coisa, responda: “Desculpe, posso ajudar somente com dúvidas sobre sustentabilidade e energias renováveis.”"
            )
        );
        No atual = listaMensagem.inicio;
        while (atual != null) {
            messages.put(new JSONObject()
                .put("role", "user")
                .put("content", atual.mensagem)
            );
            atual = atual.proximo;
        }
        JSONObject payload = new JSONObject();
        payload.put("model", "gpt-4o");
        payload.put("messages", messages);
        payload.put("temperature", 0.0);
        payload.put("max_tokens", 500);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
            .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("OpenAI API error: " + response.statusCode());
        }

        JSONObject json = new JSONObject(response.body());
        return json.getJSONArray("choices")
                   .getJSONObject(0)
                   .getJSONObject("message")
                   .getString("content")
                   .trim();
    }
}
