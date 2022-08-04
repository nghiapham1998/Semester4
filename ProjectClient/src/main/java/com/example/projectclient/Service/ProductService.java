package com.example.projectclient.Service;

import com.cloudinary.Url;
import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.*;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    UserService userService;
    private final static String BASE_URL = "http://localhost:8080/";
    public HttpResponse<String> ShowAll(String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/product/list/"))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .header("Authorization","Bearer " + session)
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public Product GetProduct(HttpSession session,String sessions) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/product/getProduct/" + session.getAttribute("usernamesClient")))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + sessions)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200){
            JSONObject ob = new JSONObject(response.body());
            Product myProduct = JSONUtils.convertToObject(Product.class,ob.toString());
            return myProduct;
        }
        return null;
    }

    public HttpResponse<String> GetTopProduct(String session) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/product/topProductCount"))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public Product Display(String username) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/display/" + username))
                .GET()
                .headers("Content-Type","application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200){
            JSONObject ob = new JSONObject(response.body());
            Product myProduct = JSONUtils.convertToObject(Product.class,ob.toString());
            return myProduct;
        }
        return null;
    }

    public List<UrlProduct> ShowAllUrl(String username) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/urlProduct/list/" +username))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JSONArray ob = new JSONArray(response.body());
        UrlProduct[] urlProducts = JSONUtils.convertToObject(UrlProduct[].class,ob.toString());
        return List.of(urlProducts);
    }

    public List<LinkType> ShowAllLinkType(String session)throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/urlProduct/listLinkType"))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .header("Authorization","Bearer " + session)
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JSONArray ob = new JSONArray(response.body());
        LinkType[] linkTypes = JSONUtils.convertToObject(LinkType[].class,ob.toString());
        return List.of(linkTypes);
    }

    public HttpResponse<String> addUrl(UrlProduct urlProduct,Long linkTypeId, String session) throws IOException, InterruptedException {
        List<LinkType> typeList = ShowAllLinkType(session);
        for(LinkType item:typeList){
            if(item.getId() == linkTypeId){
                urlProduct.setLinkType(item); ;
                break;
            }
        }

        String json = JSONUtils.convertToJSON(urlProduct);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "api/urlProduct/add"))
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> edit(String json, String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "api/product/edit"))
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public UrlProduct editUrl(String json, String session) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "api/urlProduct/edit"))
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject ob = new JSONObject(response.body());
        UrlProduct url = JSONUtils.convertToObject(UrlProduct.class,ob.toString());
        return url;
    }

    public Boolean deleteUrl(Long id, String session)throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/urlProduct/delete/"+ id))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .header("Authorization","Bearer " + session)
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if(response.statusCode()==200){
            return  true;
        }
        return  false;
    }

    public Boolean ChangeStatus(Long id, HttpSession session)throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( BASE_URL + "api/product/changeStatus/" + id))
                .GET()
                .headers("Content-Type"
                        ,"application/json")
                .header("Authorization","Bearer " + session.getAttribute("tokenAdmin"))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() ==200){
            return true;
        }
        return false;
    }

    public HttpResponse<String> getChartsMonthUserProduct(String session,int month,Long product_id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/display/chartProductAccess/" + product_id + "/" + month))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }

    public HttpResponse<String> getChartsYearUserProduct(String session,int year,Long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/display/YearProductAccess/"+id))
                .GET()
                .headers("Content-Type","application/json")
                .header("Authorization","Bearer " + session)
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }

}
