package com.example.projectclient.Service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class OrderService {
    private final static String BASE_URL = "http://localhost:8080/";
    public HttpResponse<String> ShowAll(String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/order/list"))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> getCharts(String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/order/getCharts/0"))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }
    public HttpResponse<String> getChartsCategory(String session,Long categoryId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/order/getCharts/" + categoryId))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> getChartsMonth(String session,Long month) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/order/MonthOrder/" + month))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> getChartYear(String session,String year) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/order/YearOrder/?d2=" + year))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }
    public HttpResponse<String> confirm(int id,HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/order/nextProcess/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> cancel(int id,HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/order/cancelOrder/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> details(Long id,HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/order/details/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
}
