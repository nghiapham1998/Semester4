package com.example.projectclient.Controllers.Admin;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.*;
import com.example.projectclient.Service.OrderService;
import com.example.projectclient.Service.ProductService;
import com.example.projectclient.Service.ReviewService;
import com.example.projectclient.Service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/Admin")
public class IndexControllers {
    @Autowired
    ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    OrderService orderService;

    @Autowired
    ReviewService reviewService;

    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    @GetMapping("/")
    public String index(HttpSession sessions,Model model, RedirectAttributes attributes ,
                        @RequestParam(value = "month",defaultValue = "",required = false) Long month ,
                        @RequestParam(value = "year",defaultValue = "",required = false) String year,
                        @RequestParam(value = "chart",defaultValue = "",required = false) Long categoryId)
                        throws IOException, InterruptedException {
        String role = (String) sessions.getAttribute("roles");
        String session = (String) sessions.getAttribute("tokenAdmin");
        System.out.println(session);
        if (sessions.getAttribute("tokenAdmin") == null  ){

            return "Admin/Login";
        }
        if (role.contains("[\"ROLE_USER\"]")){
            return "redirect:/Admin/login";
        }
        Map<String, Integer> data = new HashMap<>();
        SortedMap<String, Integer> dataYear = new TreeMap<>();
        if (categoryId != null){
            var response = orderService.getChartsCategory(session,categoryId);
            JSONArray ob = new JSONArray(response.body());
            System.out.println(response.body());
            Chart[] charts = JSONUtils.convertToObject(Chart[].class,ob.toString());


            assert charts != null;
            for (Chart c : charts){
                data.put(c.getMonth()+ "  / " + c.getYear(),c.getPrice());

            }

            System.out.println(data);
            Map<String, Integer> sortedMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int lengthDifference = o1.length() - o2.length();
                    if (lengthDifference != 0) return lengthDifference;
                    return o1.compareTo(o2);
                }
            });

            sortedMap.putAll(data);

            sortedMap.entrySet().forEach(System.out::println);
            model.addAttribute("keySet",sortedMap.keySet());
            model.addAttribute("values",sortedMap.values());
        } else if (month!= null) {
            var responseMonth = orderService.getChartsMonth(session,month);
            JSONArray obMonth = new JSONArray(responseMonth.body());
            System.out.println(responseMonth.body());
            Chart[] charts = JSONUtils.convertToObject(Chart[].class,obMonth.toString());
            System.out.println(charts);
            assert charts != null;
            for (Chart c : charts){
                data.put("Day "  + c.getDay(),c.getPrice());
            }

       data.entrySet().forEach(System.out::println);
            Map<String, Integer> sortedMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int lengthDifference = o1.length() - o2.length();
                    if (lengthDifference != 0) return lengthDifference;
                    return o1.compareTo(o2);
                }
            });

            sortedMap.putAll(data);

            sortedMap.entrySet().forEach(System.out::println);
            model.addAttribute("infor",charts[1].getMonth() +"/"+ charts[1].getYear());
            model.addAttribute("keySet",sortedMap.keySet());
            model.addAttribute("values",sortedMap.values());
        } else if (year != null) {

            var response = orderService.getChartYear(session,year);
            JSONArray ob = new JSONArray(response.body());
            System.out.println(response.body());
            Chart[] charts = JSONUtils.convertToObject(Chart[].class,ob.toString());


            assert charts != null;
            for (Chart c : charts){
                dataYear.put( c.getMonth()  + " /  " + c.getYear(),c.getPrice());

            }
            Map<String, Integer> sortedMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int lengthDifference = o1.length() - o2.length();
                    if (lengthDifference != 0) return lengthDifference;
                    return o1.compareTo(o2);
                }
            });

            sortedMap.putAll(dataYear);

            sortedMap.entrySet().forEach(System.out::println);
            model.addAttribute("keySet",sortedMap.keySet());
            model.addAttribute("values",sortedMap.values());
        } else if(year == null && categoryId == null && month == null){
            var response = orderService.getCharts(session);
            JSONArray ob = new JSONArray(response.body());
            System.out.println(response.body());
            Chart[] charts = JSONUtils.convertToObject(Chart[].class,ob.toString());


            assert charts != null;
            for (Chart c : charts){
                data.put(  c.getMonth()+ "/   " + c.getYear(),c.getPrice());

            }
            Map<String, Integer> sortedMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int lengthDifference = o1.length() - o2.length();
                    if (lengthDifference != 0) return lengthDifference;
                    return o1.compareTo(o2);
                }
            });

            sortedMap.putAll(data);

            sortedMap.entrySet().forEach(System.out::println);
            model.addAttribute("keySet",sortedMap.keySet());
            model.addAttribute("values",sortedMap.values());
        }

        var response = orderService.ShowAll(session);
        JSONArray ob = new JSONArray(response.body());
        Orders[] orders = JSONUtils.convertToObject(Orders[].class,ob.toString());
        assert orders != null;
        List<Orders> or = List.of(orders);
        int size = 0;
        int countConfrim = 0;
        int countCreate = 0;
        int countSuccess = 0;
        for (Orders o : or){
            if (o.getOrder_process().getId() == 1){
                countCreate +=1;
            }
            if (o.getOrder_process().getId() == 2){
                countConfrim +=1;
            }
            if (o.getOrder_process().getId() == 3){
                countSuccess +=1;
            }
            size = countCreate + countConfrim + countSuccess;
        }

        var responseUser = userService.FindAllUserAdmin(1,session);
        JSONArray obUser = new JSONArray(responseUser.body());
        User[] user = JSONUtils.convertToObject(User[].class,obUser.toString());
        assert user != null;
        List<User> userCount = List.of(user);
        int countUser = userCount.size();


        var responseProduct = productService.ShowAll(session);
        JSONArray obProduct = new JSONArray(responseProduct.body());
        Product[] products = JSONUtils.convertToObject(Product[].class,obProduct.toString());
        assert products != null;
        List<Product> productList = List.of(products);
        int productCount = productList.size();


        var responseTopProduct = productService.GetTopProduct(session);
        JSONArray obTopProduct = new JSONArray(responseTopProduct.body());
        Product[] Topproducts = JSONUtils.convertToObject(Product[].class,obTopProduct.toString());
        assert Topproducts != null;



        model.addAttribute("productCount",productCount);
        model.addAttribute("countUser",countUser);
        model.addAttribute("countConfrim",countConfrim);
        model.addAttribute("countSuccess",countSuccess);
        model.addAttribute("countCreate",countCreate);
        model.addAttribute("SizeOrder",size);

        model.addAttribute("TopProduct",List.of(Topproducts));


        return "Admin/index";
    }



    @GetMapping("/settings")
    public String setting(ModelMap model,HttpSession session, RedirectAttributes attributes){

        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null  ){

            return "redirect:/Admin/login";
        }
        if (role.contains("[\"ROLE_USER\"]")){
            return "redirect:/Admin/login";
        }

        model.addAttribute("changeEmailResetPassword",new changePasswordReset());
        return "Admin/settings";
    }
    @GetMapping("/resetPassword")
    public String resetPassword(HttpSession session,ModelMap modelMap){
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("token") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        if (session.getAttribute("tokenSendMailChangePassword") ==null){
            return "redirect:/Admin/";
        }
        modelMap.addAttribute("changePasswordResetForm",new changePasswordReset());
        return "Admin/Users/ResetPassword";
    }

    @GetMapping(value = "/Users")
    public String  IndexUser(@RequestParam(value = "roles",defaultValue = "1",required = false) Integer roles, HttpSession sessions, ModelMap model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) sessions.getAttribute("roles");
        if (sessions.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        String session = (String) sessions.getAttribute("tokenAdmin");
            var response = userService.FindAllUserAdmin(roles,session);
        System.out.println(response);
            if (response.statusCode() != 200){
                return "Admin/403";
            }
        JSONArray ob = new JSONArray(response.body());

        User[] user = JSONUtils.convertToObject(User[].class,ob.toString());


        assert user != null;
        model.addAttribute("listUser",List.of(user));

        return "Admin/Users/index";
    }



    @GetMapping("/reviews")
    public String reviews(HttpSession session,Model model) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var response = reviewService.FindForbiddenword(session);
        var responseReview = reviewService.FindReview(session);
        System.out.println(response.body());
        System.out.println(responseReview.body());
        JSONArray ob = new JSONArray(response.body());
        JSONArray obReview = new JSONArray(responseReview.body());
        ForbiddenReview[] forbiddenReviews = JSONUtils.convertToObject(ForbiddenReview[].class,ob.toString());
        Review[] review = JSONUtils.convertToObject(Review[].class,obReview.toString());


        assert forbiddenReviews != null;
        assert review != null;
        model.addAttribute("listForbiddenword",List.of(forbiddenReviews));
        model.addAttribute("forbiddenword",new ForbiddenReview());
        model.addAttribute("Review",List.of(review));
        return "Admin/Review/index";
    }



}
