package com.example.projectclient.Service.Client;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class OrderServiceClient {

    private final static String BASE_URL = "http://localhost:8080";

    public HttpResponse<String> orderCheckOut(String json, HttpSession session) throws IOException, InterruptedException {


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/order/add"))
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("token"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response;
    }
}
