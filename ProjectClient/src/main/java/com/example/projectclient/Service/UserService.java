package com.example.projectclient.Service;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.UpdateProfileUser;
import com.example.projectclient.Models.User;
import okhttp3.*;
import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final static String BASE_URL = "http://localhost:8080";
    public User userProfile(HttpSession session,String sessions) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/auth/profile/" + session.getAttribute("usernamesClient")))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + sessions)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject ob = new JSONObject(response.body());
        if (response.statusCode() == 401){
            System.out.println(ob.getString("message"));
        }

        User user = JSONUtils.convertToObject(User.class,ob.toString());
        System.out.println(user);
        return user;
    }

    public void changeImageUser(HttpSession session, File file) throws URISyntaxException, IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Path currentRelativePath = Paths.get("D:/ProjectClient/user-photos");
        String path = currentRelativePath.toAbsolutePath().toString();
        System.out.println(path);
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", (String) session.getAttribute("usernamesClient"))
                .addFormDataPart("image", path+"/"+file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(path+"/"+file.getName())))
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/auth/profile/changeImageProfile")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
    }

    public HttpResponse<String> updateProfile(String json, HttpSession session) throws IOException, InterruptedException {


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/auth/profile/updateProfile/"+ session.getAttribute("id")))
                .headers("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response;
    }


    public HttpResponse<String> changePasswordAdmin(String json ) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/auth/changeEmail"))
                .headers("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }


    public HttpResponse<String> updatePassword(String json ) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/auth/updatePassword"))
                .headers("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response;
    }


    public HttpResponse<String> FindAllUserAdmin(int rolesId,String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/User/findAllUserAdmin/" + rolesId))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> FindUserBand(HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/User/findUserBand"))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }
    public HttpResponse<String> deleteUserAdmin(int id,HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/User/delete/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }


    public HttpResponse<String> unlocked(int id,HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/User/unlocked/" + id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }
    public HttpResponse<String> CreateAccount(String json, HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/auth/signup"))
                .headers("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response;
    }

    public Boolean checkUsername(String username,HttpSession session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/User/checkUsername/" + username))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session.getAttribute("token"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200){
            return true;
        }
        return false;
    }

    public Boolean isLogined(HttpSession sessions,String session) throws IOException, InterruptedException {
        User user = userProfile(sessions,session);
        if(sessions.getAttribute("token").toString().equals(user.getToken())){
            return true;
        }
        return false;
    }
}
