package com.example.projectclient.Controllers.Admin;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.Category;
import com.example.projectclient.Models.Product;
import com.example.projectclient.Service.ProductService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@MultipartConfig
@Validated
public class ProductCategory {
    @Autowired
    ProductService productService;

    public ProductCategory(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("Admin/Product")
    public String ShowAll(HttpSession sessions, Model model) throws IOException, InterruptedException {
        String role = (String) sessions.getAttribute("roles");
        if (sessions.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var session = sessions.getAttribute("tokenAdmin");
        var response = productService.ShowAll((String) session);
        JSONArray ob = new JSONArray(response.body());
        Product[] products = JSONUtils.convertToObject(Product[].class,ob.toString());
        assert products != null;
        model.addAttribute("products", List.of(products));
        return "Admin/Product/index";
    }

    @GetMapping("Admin/Product/ChangeStatus/{id}")
    public String ChangeStatus(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        if(productService.ChangeStatus(id,session)){
            redirectAttributes.addFlashAttribute("Success","Change Status Successfully");
        }else{
            redirectAttributes.addFlashAttribute("Fails","Change Status Fail");
        }

        return "redirect:/Admin/Product";
    }

}
