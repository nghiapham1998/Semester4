package com.example.projectclient;

import com.example.projectclient.Models.Role;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@SpringBootApplication
@RestController
public class ProjectClientApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

        SpringApplication.run(ProjectClientApplication.class, args);
    }

    private static final String BaseUrl = "https://jsonplaceholder.typicode.com/todos";
    @GetMapping("/ ")
    public void test() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept","application/json")
                .uri(URI.create(BaseUrl)).build();
       HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        List<Role> posts = mapper.readValue(response.body(), new TypeReference<List<Role>>() {
        });
        posts.forEach(System.out::println);
    }
}
