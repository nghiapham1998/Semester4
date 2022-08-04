package com.example.projectclient.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

@Service
public class LoginService {

    private final static String BASE_URL = "http://localhost:8080";


     public HttpResponse<String> login(String username, String password) throws IOException, InterruptedException {
         var values = new HashMap<String, String>() {{
             put("username", username);
             put ("password", password);
         }};

         var objectMapper = new ObjectMapper();
         String requestBody = objectMapper
                 .writeValueAsString(values);

         HttpClient client = HttpClient.newHttpClient();
         HttpRequest request = HttpRequest.newBuilder()
                 .uri(URI.create(BASE_URL + "/api/auth/signin"))
                 .headers("Content-Type","application/json")
                 .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                 .build();

         HttpResponse<String> response = client.send(request,
                 HttpResponse.BodyHandlers.ofString());

         System.out.println(response.body());

         return response;
     }
}
