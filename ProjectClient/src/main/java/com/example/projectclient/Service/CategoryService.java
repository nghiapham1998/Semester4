package com.example.projectclient.Service;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.Category;
import com.example.projectclient.Models.User;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CategoryService {
    private final static String BASE_URL = "http://localhost:8080";

    public HttpResponse<String> ShowAll(String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/category/list"))
                .GET()
                .headers("Content-Type","application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> delete(int id,String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/category/delete/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> add(String json, String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/category/add"))
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public Category details(Long id, String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/category/details/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject ob = new JSONObject(response.body());
        Category category = JSONUtils.convertToObject(Category.class,ob.toString());
        return category;
    }

    public HttpResponse<String> edit(String json, String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/category/edit"))
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> AddQuantity(Long id,int number, String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/category/addQuantity/" + id + "/"+ number))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }



}
