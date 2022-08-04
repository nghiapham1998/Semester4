package com.example.projectclient.Controllers.Admin;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.*;
import com.example.projectclient.Service.CategoryService;
import com.example.projectclient.Service.CloudBinary.CloudBinaryService;
import com.example.projectclient.Service.FileUploadUtil;
import com.example.projectclient.Service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@MultipartConfig
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CloudBinaryService cloudBinaryService;
    private Map<String, String> options = new HashMap<>();
    @Value("${javadocfast.cloudinary.folder.category}")
    private String image;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("Admin/Category")
    public String ShowAll(HttpSession sessions, Model model) throws IOException, InterruptedException {
        String role = (String) sessions.getAttribute("roles");
        if (sessions.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }


        var session =(String) sessions.getAttribute("tokenAdmin");
        var response = categoryService.ShowAll(session);
        JSONArray ob = new JSONArray(response.body());
        Category[] categories = JSONUtils.convertToObject(Category[].class,ob.toString());
        assert categories != null;
        model.addAttribute("categories", List.of(categories));
        return "Admin/Category/index";
    }


    @GetMapping("Admin/Category/delete/{id}")
    public String delete(@PathVariable int id, HttpSession sessions , RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        var session = (String) sessions.getAttribute("tokenAdmin");
        var response = categoryService.delete(id,session);
        if (response.statusCode() == 200){
            redirectAttributes.addFlashAttribute("Success",response.body());
            return "redirect:/Admin/Category";
        }
        if (response.statusCode() == 403){
            redirectAttributes.addFlashAttribute("Success","You dont have role to do this");
            return "redirect:/Admin/Category";
        }
        redirectAttributes.addFlashAttribute("Success",response.body());
        return "redirect:/Admin/Category";
    }

    @GetMapping(value = "Category/Add")
    public String add (HttpSession session, ModelMap model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        model.addAttribute("CreateCategory", new CreateCategory());
        return "Admin/Category/addNew";
    }

    @PostMapping(value = "/Category/AddNew")
    public String SaveAddNew(@ModelAttribute("CreateCategory") CreateCategory createCategory , HttpSession sessions, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, ParseException {
        Category category = new Category();
        category.setName(createCategory.getName());
        category.setQuantity(createCategory.getPrice());
        category.setCreate_at(new Date());

        options.put("folder", image);
        if(!createCategory.getFrontImage().isEmpty()){
            // Folder To Save Image
            MultipartFile frontFile = createCategory.getFrontImage();
            // Update New Image
            Map frontResult = cloudBinaryService.upload(frontFile, options);
            category.setFrontImage(frontResult.get("url").toString());
        }
        if(!createCategory.getBackImage().isEmpty()){
            MultipartFile backFile = createCategory.getBackImage();
            Map backResult = cloudBinaryService.upload(backFile, options);
            category.setBackImage(backResult.get("url").toString());
        }
        var session = (String)sessions.getAttribute("tokenAdmin");
        String json = JSONUtils.convertToJSON(category);
        var response  = categoryService.add(json,session);
        redirectAttributes.addFlashAttribute("Success",response.body());
        return "redirect:/Admin/Category";

    }

    @GetMapping(value = "Admin/Category-{id}")
    public String edit (@PathVariable Long id, HttpSession sessions, ModelMap model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) sessions.getAttribute("roles");
        if (sessions.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var session =(String) sessions.getAttribute("tokenAdmin");
        var response = categoryService.details(id,session);
        if(response == null){
            redirectAttributes.addFlashAttribute("Error", "Not Found Category");
            return "redirect:/Admin/Category";
        }
        CreateCategory createCategory = new CreateCategory();
        createCategory.setId(response.getId());
        createCategory.setName(response.getName());
        createCategory.setPrice(response.getPrice());
        createCategory.setQuantity(response.getQuantity());
        model.addAttribute("front",response.getFrontImage());
        model.addAttribute("back",response.getBackImage());
        model.addAttribute("CreateCategory", createCategory);
        return "Admin/Category/edit";
    }

    @PostMapping(value = "/Category/SaveEdit")
    public String SaveEdit(@ModelAttribute("CreateCategory") CreateCategory createCategory , HttpSession sessions, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, ParseException {
        Category category = new Category();
        category.setId(createCategory.getId());
        category.setName(createCategory.getName());
        category.setPrice(createCategory.getPrice());
        category.setQuantity(createCategory.getQuantity());
        category.setCreate_at(new Date());
        var session =(String) sessions.getAttribute("tokenAdmin");
        options.put("folder", image);
        if(!createCategory.getFrontImage().isEmpty()){
            // Folder To Save Image
            MultipartFile frontFile = createCategory.getFrontImage();
            // Update New Image
            Map frontResult = cloudBinaryService.upload(frontFile, options);
            category.setFrontImage(frontResult.get("url").toString());
        }else{
            category.setFrontImage(categoryService.details(createCategory.getId(),session).getFrontImage());
        }
        if(!createCategory.getBackImage().isEmpty()){
            MultipartFile backFile = createCategory.getBackImage();
            Map backResult = cloudBinaryService.upload(backFile, options);
            category.setBackImage(backResult.get("url").toString());
        }else{
            category.setBackImage(categoryService.details(createCategory.getId(),session).getBackImage());
        }

        String json = JSONUtils.convertToJSON(category);
        var response  = categoryService.edit(json,session);
        redirectAttributes.addFlashAttribute("Success","Edit Successfully");
        return "redirect:/Admin/Category";
    }

    @GetMapping(value = "Admin/AddQuantity-{id}")
    public String addQuantity (@PathVariable Long id,HttpSession sessions, ModelMap model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) sessions.getAttribute("roles");
        if (sessions.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var session = (String) sessions.getAttribute("tokenAdmin");
        var response = categoryService.details(id,session);
        model.addAttribute("quantity", response.getQuantity());
        response.setQuantity(0);
        model.addAttribute("Category", response);

        return "Admin/Category/addQuantity";
    }

    @PostMapping(value = "Admin/SaveAddQuantity")
    public String SaveAddQuantity (@ModelAttribute("Category") Category category, HttpSession sessions, ModelMap model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        var session =(String) sessions.getAttribute("tokenAdmin");
        var response = categoryService.AddQuantity(category.getId(),category.getQuantity(),session);
        if(response.statusCode() == 200){
            redirectAttributes.addFlashAttribute("Success", "Add Quantity Successfully");
        }else{
            redirectAttributes.addFlashAttribute("Error", "Not Found Category");
        }
        return "redirect:/Admin/Category";
    }
}
