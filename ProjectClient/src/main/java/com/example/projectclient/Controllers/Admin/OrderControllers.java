package com.example.projectclient.Controllers.Admin;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.Category;
import com.example.projectclient.Models.Orders;
import com.example.projectclient.Service.OrderService;
import com.example.projectclient.Service.PdfService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@MultipartConfig
@Validated
public class OrderControllers {
    @Autowired
    OrderService orderService;

    @Autowired
    PdfService pdfService;


    public OrderControllers(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("Admin/Order")
    public String ShowAll(HttpSession sessions, Model model) throws IOException, InterruptedException {
        String role = (String) sessions.getAttribute("roles");
        if (sessions.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var session = (String) sessions.getAttribute("tokenAdmin");
        var response = orderService.ShowAll(session);
        JSONArray ob = new JSONArray(response.body());
        Orders[] orders = JSONUtils.convertToObject(Orders[].class,ob.toString());
        assert orders != null;
        model.addAttribute("orders", List.of(orders));
        return "Admin/Orders/index";
    }

    @GetMapping("Admin/Order/Confirm/{id}")
    public String Confirm(@PathVariable int id, HttpSession session,  RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var response = orderService.confirm(id,session);
        if (response.statusCode() != 200){
            redirectAttributes.addFlashAttribute("Success",response.body());
            return "redirect:/Admin/OrderDetails-" + id;
        }
        redirectAttributes.addFlashAttribute("Success",response.body());
        return "redirect:/Admin/OrderDetails-" + id;
    }

    @GetMapping("Admin/Order/cancelOrder/{id}")
    public String Cancel(@PathVariable int id, HttpSession session,  RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var response = orderService.cancel(id,session);
        if (response.statusCode() == 200){
            redirectAttributes.addFlashAttribute("Success",response.body());
            return "redirect:/Admin/OrderDetails-" + id;
        }
        redirectAttributes.addFlashAttribute("Error",response.body());
        return "redirect:/Admin/OrderDetails-" + id;
    }

    @GetMapping("Admin/OrderDetails-{id}")
    public String GoDetails(@PathVariable Long id, HttpSession session,Model model,RedirectAttributes redirectAttributes) throws IOException, InterruptedException, URISyntaxException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var response = orderService.details(id,session);
        JSONObject ob = new JSONObject(response.body());
        Orders orders = JSONUtils.convertToObject(Orders.class,ob.toString());
        model.addAttribute("orders", orders);
        //create pdf file
        pdfService.Create(orders.getId(),orders.getFullname(), orders.getProduct().getImageUrlcode());
        model.addAttribute("pdf", "./product-pdf/"+ orders.getId()+".pdf");
        return "/Admin/Orders/OrderDetails";
    }

    @GetMapping("Admin/product-pdf/{order_id}")
    public void Download(@PathVariable String order_id, HttpServletResponse response) throws IOException {

        response.setContentType("text/plain");
        response.setHeader("Content-disposition","attachment; filename=OneTouch.pdf");
        File my_file = new File("D:\\ProjectClient\\src\\main\\resources\\templates\\Admin\\product-pdf\\"+order_id); // We are downloading .txt file, in the format of doc with name check - check.doc


        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(my_file);
        byte[] buffer = new byte[100000];
        int length;
        while ((length = in.read(buffer)) > 0){
            out.write(buffer, 0, length);
        }
        in.close();
        out.flush();
    }


}
