package com.example.projectclient.Controllers.Client;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.*;
import com.example.projectclient.Service.CloudBinary.CloudBinaryService;
import com.example.projectclient.Service.OrderService;
import com.example.projectclient.Service.ProductService;
import com.example.projectclient.Service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;

    @Autowired
    private CloudBinaryService cloudBinaryService;
    @Value("${javadocfast.cloudinary.folder.product}")
    private String image;
    private Map<String, String> options = new HashMap<>();

    @GetMapping("/Product")
    public String MyProduct(HttpSession sessions, ModelMap model
    ,@RequestParam(value = "month",defaultValue = "7",required = false) int month
            ,@RequestParam(value = "year",defaultValue = "0",required = false) int year) throws IOException, InterruptedException {
         var session = (String) sessions.getAttribute("token");
        model.addAttribute("isLogined",userService.isLogined(sessions,session));
        if(productService.GetProduct(sessions,session) != null  && productService.GetProduct(sessions,session).getStatus() ==1){
            Product product = productService.GetProduct(sessions,session);
            EditProduct editProduct = new EditProduct(product.getId(), product.getDescription(), product.getName());
            model.addAttribute("EditProduct", editProduct);
            model.addAttribute("Product",  product);
            model.addAttribute("listUrl", productService.ShowAllUrl((String) sessions.getAttribute("usernamesClient")));
            model.addAttribute("listLinkType", productService.ShowAllLinkType(session));
            model.addAttribute("UrlProduct", new UrlProduct());
            model.addAttribute("EditUrl", new EditUrl());

            if(month < 13){
                Map<String, Integer> data = new HashMap<>();
                var responseMonth = productService.getChartsMonthUserProduct(session,month,product.getId());
                System.out.println(responseMonth.body());
                JSONArray obMonth = new JSONArray(responseMonth.body());
                System.out.println(responseMonth.body());
                ChartProductAccess[] charts = JSONUtils.convertToObject(ChartProductAccess[].class,obMonth.toString());
                System.out.println(charts);
                assert charts != null;
                for (ChartProductAccess c : charts){
                    data.put(c.getDay() + " / " + month,c.getCount());
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

                model.addAttribute("keySet",sortedMap.keySet());
                model.addAttribute("values",sortedMap.values());
            }else{
                Map<String, Integer> data = new HashMap<>();
                var responseYear = productService.getChartsYearUserProduct(session,2022,product.getId());
                System.out.println(responseYear.body());
                JSONArray obYear = new JSONArray(responseYear.body());
                System.out.println(responseYear.body());
                ChartProductAccess[] charts = JSONUtils.convertToObject(ChartProductAccess[].class,obYear.toString());
                System.out.println(charts);
                assert charts != null;
                for (ChartProductAccess c : charts){
                    data.put(c.getMonth()+" / "+c.getYear(),c.getCount());
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
                model.addAttribute("keySet",sortedMap.keySet());
                model.addAttribute("values",sortedMap.values());
            }
            return "Client/Product";
        }
        return  "redirect:/Error";
    }

    @GetMapping("/Display/{username}")
    public String DisplayProduct(@PathVariable String username, HttpSession session, ModelMap model) throws IOException, InterruptedException {
        if(userService.checkUsername(username, session)){
            var myProduct = productService.Display(username);
            if(myProduct != null){

                model.addAttribute("Product", myProduct);
                model.addAttribute("listUrl", productService.ShowAllUrl(username));
                return "CLient/DisplayProduct";
            }else{
                return  "redirect:/Error";
            }

        }
        return  "redirect:/Error";
    }

    @PostMapping("/Product/AddNewUrl")
    public String AddNewUrl(@ModelAttribute("UrlProduct") UrlProduct urlProduct,@RequestParam("type") Long linkTypeId, HttpSession sessions, ModelMap model) throws IOException, InterruptedException {
        String session = (String) sessions.getAttribute("token");
        model.addAttribute("isLogined",userService.isLogined(sessions,session));
        urlProduct.setProduct(productService.GetProduct(sessions,session));
        urlProduct.setUser(productService.GetProduct(sessions,session).getUser());
        var response = productService.addUrl(urlProduct,linkTypeId, session);
        if(response.statusCode() == 200){
            return "redirect:/Product";
        }
        return  "redirect:/Error";
    }

    @PostMapping("/Product/EditUrl")
    public String EditUrl(@ModelAttribute("EditUrl") EditUrl urlEdit, HttpSession sessions, ModelMap model) throws IOException, InterruptedException {
        String session =(String) sessions.getAttribute("token");
        model.addAttribute("isLogined",userService.isLogined(sessions,session));
        urlEdit.setUsername((String) sessions.getAttribute("usernamesClient"));
        String json = JSONUtils.convertToJSON(urlEdit);
        var response = productService.editUrl(json, session);
        return "redirect:/Product";

    }

    @PostMapping("/Product/Edit")
    public String Edit(@ModelAttribute("EditProduct") EditProduct editProduct, HttpSession sessions, ModelMap model) throws IOException, InterruptedException {
        String session =(String) sessions.getAttribute("token");
        model.addAttribute("isLogined",userService.isLogined(sessions,session));

        Product product = productService.GetProduct(sessions,session);
        product.setName(editProduct.getName());
        product.setDescription(editProduct.getDescription());
        product.setUpdate_at(new Date());

        options.put("folder", image);
        if(!editProduct.getAvatar().isEmpty()){
            // Folder To Save Image
            MultipartFile frontFile = editProduct.getAvatar();
            // Update New Image
            Map frontResult = cloudBinaryService.upload(frontFile, options);
            product.setAvatar(frontResult.get("url").toString());
        }
        String json = JSONUtils.convertToJSON(product);
        var response  = productService.edit(json,session);
        if(response.statusCode() == 200){
            return "redirect:/Product";
        }
        return  "redirect:/Error";
    }

    @GetMapping("/Product/DeleteUrl/{id}")
    public String DeleteUrl(@PathVariable Long id, HttpSession sessions, ModelMap model) throws IOException, InterruptedException {
         String session = (String) sessions.getAttribute("token");
        model.addAttribute("isLogined",userService.isLogined(sessions,session));

        productService.deleteUrl(id,session);
        return "redirect:/Product";
    }
}
