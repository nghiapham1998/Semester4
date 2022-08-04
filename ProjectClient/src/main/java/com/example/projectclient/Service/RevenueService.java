package com.example.projectclient.Service;

import com.example.projectclient.Config.JSONUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class RevenueService {
    private final static String BASE_URL = "http://localhost:8080/";
    //all revenue
    public String showAllRevenue(HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/android/getRevenue/"))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .header("Authorization","Bearer " + session.getAttribute("token"))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    //count user active
    public String countUsersActive(HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/android/getCountUsersActive"))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .header("Authorization","Bearer " + session.getAttribute("token"))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    //count user locked
    public String countUsersLocked(HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/android/getCountUsersLocked"))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .header("Authorization","Bearer " + session.getAttribute("token"))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    //count all order
    public String countAllProductOrder(HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/android/getCountProduct/"))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .header("Authorization","Bearer " + session.getAttribute("token"))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    //count order by status
    public List<Integer> countOrderByStatus(HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/android/countOrderStatus"))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .header("Authorization","Bearer " + session.getAttribute("token"))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONArray ob = new JSONArray(response.body());
        Integer[] countStatus = JSONUtils.convertToObject(Integer[].class,ob.toString());
        return List.of(countStatus);
    }
}
