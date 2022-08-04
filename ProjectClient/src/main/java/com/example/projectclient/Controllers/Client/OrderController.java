package com.example.projectclient.Controllers.Client;


import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.Category;
import com.example.projectclient.Models.CreateOrderRequest;
import com.example.projectclient.Models.Orders;
import com.example.projectclient.Models.User;
import com.example.projectclient.Service.CategoryService;
import com.example.projectclient.Service.Client.OrderServiceClient;
import com.example.projectclient.Service.PdfService;
import com.example.projectclient.Service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    OrderServiceClient orderServiceClient;

    @Autowired
    CategoryService categoryService;

    @Autowired
    PdfService pdfService;

    @Autowired
    UserService userService;

    @GetMapping("MoveToOrder")
    public String getOrder(ModelMap model,HttpSession sessions) throws IOException, InterruptedException {
        if (sessions.getAttribute("token") == null){
            return "redirect:/Login";
        }

        String session =(String) sessions.getAttribute("token");
        model.addAttribute("isLogined",userService.isLogined(sessions,session));

        var response = categoryService.ShowAll(session);
        JSONArray ob = new JSONArray(response.body());
        Category[] categories = JSONUtils.convertToObject(Category[].class,ob.toString());
        assert categories != null;
        model.addAttribute("categoriOrder", List.of(categories));
        model.addAttribute("CreateOrder", new CreateOrderRequest());
        return "Client/Order";
    }

    @PostMapping("/CheckOut")
    public String Checkout(@ModelAttribute("") CreateOrderRequest request, HttpSession session) throws IOException, InterruptedException {
        if (session.getAttribute("token") == null){
            return "redirect:/Login";
        }

        request.setUsername((String) session.getAttribute("usernamesClient"));
        String json = JSONUtils.convertToJSON(request);
        System.out.println(json);
        var response  = orderServiceClient.orderCheckOut(json,session);
        if (response.statusCode() == 200){
            JSONObject ob = new JSONObject(response.body());
            Orders orders = JSONUtils.convertToObject(Orders.class,ob.toString());
//            //create pdf file
//            pdfService.Create(orders.getId(),orders.getFullname(), orders.getProduct().getImageUrlcode());
            return "redirect:/Client";
        }
        return "redirect:/Order-" + request.getCategory_id();
    }
}
