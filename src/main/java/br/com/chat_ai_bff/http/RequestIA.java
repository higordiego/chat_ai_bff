package br.com.chat_ai_bff.http;

import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestIA {

    private String url = "http://localhost:4000/question";
    public String requestQuestion(String search) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        JsonObject json = new JsonObject();
        json.put("question", search);

        System.out.println(json.toString());

        var request = HttpRequest.newBuilder(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .header("Content-Type", "application/json")
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body().toString();
    }
}
