package com.example.projectclient.Service;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.Review;
import com.example.projectclient.Models.User;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ReviewService {
    private final static String BASE_URL = "http://localhost:8080";
    public HttpResponse<String> FindForbiddenword(HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/forbiddenword/getAll"))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }


    public HttpResponse<String> addWord(String json, HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/forbiddenword/add"))
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> deleteWord(Long id,HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/forbiddenword/delete/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> deleteComment(Long id,HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/reviews/delete/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }


    public HttpResponse<String> FindReview(HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/reviews/showAll"))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }
    public HttpResponse<String> addReview(String json, HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/reviews/add"))
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("token"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> FindReviewCategory(Long id ,String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/reviews/showAll/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());


        return response;
    }


    public Review getDetails(HttpSession session, Long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/reviews/details/" +  id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject ob = new JSONObject(response.body());
        if (response.statusCode() == 401){
            System.out.println(ob.getString("message"));
        }

        Review review = JSONUtils.convertToObject(Review.class,ob.toString());

        return review;


    }
}
