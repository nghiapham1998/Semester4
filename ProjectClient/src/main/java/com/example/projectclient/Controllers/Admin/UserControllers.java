package com.example.projectclient.Controllers.Admin;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.SignUpRequest;
import com.example.projectclient.Models.UpdateProfileUser;
import com.example.projectclient.Models.User;
import com.example.projectclient.Models.changePasswordReset;
import com.example.projectclient.Service.FileUploadUtil;
import com.example.projectclient.Service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@MultipartConfig
@Validated
public class UserControllers {

    @Autowired
    private  UserService userService;

    @Autowired
    ServletContext application;

    @InitBinder
    public void initBender(WebDataBinder webDataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
    }

    public UserControllers(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("Admin/Profile")
    public String user(HttpSession sessions, Model model) throws IOException, InterruptedException {
        String session = (String) sessions.getAttribute("tokenAdmin");
        System.out.println(userService.userProfile(sessions,session));
        String role = (String) sessions.getAttribute("roles");
        if (sessions.getAttribute("tokenAdmin") == null  ){

            return "redirect:/Admin/login";
        }
        if (role.contains("[\"ROLE_USER\"]")){
            return "redirect:/Admin/login";
        }
        model.addAttribute("users", userService.userProfile(sessions,session));
        model.addAttribute("updateProfile",new UpdateProfileUser());
        return "Admin/Users/Profile";
    }

    @PostMapping("Admin/changeImageProfile")
    public String changeImageProfile(HttpSession session, Model model
    , @RequestParam("image") MultipartFile multipartFile) throws IOException, InterruptedException, URISyntaxException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "user-photos";

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        File file = new File(multipartFile.getOriginalFilename());

        userService.changeImageUser(session,file);

        return "redirect:/Admin/Profile";

    }

    @PostMapping("Admin/profile/update")
    public String updateProfile(RedirectAttributes redirectAttributes, ModelMap model,
                             @Valid     User user,
                                HttpSession session) throws IOException, InterruptedException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       UpdateProfileUser profileUser = new UpdateProfileUser();
        profileUser.setAddress(user.getAddress());
        profileUser.setDescription(user.getDescription());
        profileUser.setEmail(user.getEmail());
        profileUser.setFullname(user.getFullname());
        profileUser.setLastname(user.getLastname());
        profileUser.setPhone(user.getPhone());
        var dateBirth = simpleDateFormat.format(user.getDateOfbirth());
        profileUser.setDateOfbirth(dateBirth);
        profileUser.setGender(user.getGender());
        String json = JSONUtils.convertToJSON(profileUser);
        var respone = userService.updateProfile(json,session);

        System.out.println(json);

        if (respone.statusCode() != 200){
            JSONObject ob = new JSONObject(respone.body());
            User userParse = JSONUtils.convertToObject(User.class,ob.toString());
            redirectAttributes.addFlashAttribute("errorProfileFullname",userParse.getFullname());
            redirectAttributes.addFlashAttribute("errorProfileLastname",userParse.getLastname());
            redirectAttributes.addFlashAttribute("errorProfilePhone",userParse.getPhone());
            redirectAttributes.addFlashAttribute("errorProfileEmail",userParse.getEmail());
            redirectAttributes.addFlashAttribute("errorProfileAddress",userParse.getAddress());
            redirectAttributes.addFlashAttribute("errorProfileDescription",userParse.getDescription());
            return "redirect:/Admin/Profile";
        }
        redirectAttributes.addFlashAttribute("successProfile","Save change successfully");




        return "redirect:/Admin/Profile";

    }


    @PostMapping("Admin/changePasswordAdmin")
    public String changePasswordAdmin(ModelMap model,changePasswordReset request
    ,RedirectAttributes redirectAttributes,HttpSession session) throws IOException, InterruptedException {
        String json = JSONUtils.convertToJSON(request);
        var response = userService.changePasswordAdmin(json);

        if (response.statusCode() != 200){
            JSONObject ob = new JSONObject(response.body());
            redirectAttributes.addFlashAttribute("SendEmailError",ob.getString("message"));
            return "redirect:/Admin/settings";
        }
        session.setAttribute("tokenSendMailChangePassword",response);
        redirectAttributes.addFlashAttribute("SendEmailSuccess","Please login email to confirm");
        return "redirect:/Admin/settings";
    }

    @PostMapping("Admin/updatePassword")
    public String updatePassword(changePasswordReset request
            ,RedirectAttributes redirectAttributes) throws IOException, InterruptedException {

        String json = JSONUtils.convertToJSON(request);

        var response = userService.updatePassword(json);

        if (response.statusCode() == 500){
            JSONObject ob = new JSONObject(response.body());
            redirectAttributes.addFlashAttribute("UpdatePasswordError",ob.getString("message"));
            return "redirect:/Admin/resetPassword";
        }
        if (response.statusCode() == 400){
            JSONObject ob = new JSONObject(response.body());
            changePasswordReset passwordReset = JSONUtils.convertToObject(changePasswordReset.class,ob.toString());
            redirectAttributes.addFlashAttribute("errorResetPasswordEmail",passwordReset.getEmail());
            redirectAttributes.addFlashAttribute("errorResetPasswordPassword",passwordReset.getPassword());
            return "redirect:/Admin/resetPassword";
        }
        redirectAttributes.addFlashAttribute("UpdatePasswordSuccess","Update Password Successfully");
        return "redirect:/Admin/settings";
    }


    @GetMapping("Admin/Users/delete/{id}")
    public String deleteUser(@PathVariable int id,HttpSession session ,RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var response = userService.deleteUserAdmin(id,session);
        if (response.statusCode() == 400){
            redirectAttributes.addFlashAttribute("DeleteUserSuccess",response.body());
            return "redirect:/Admin/Users";
        }
        if (response.statusCode() == 403){
            redirectAttributes.addFlashAttribute("DeleteUserSuccess","Forbidden !!!");
            return "redirect:/Admin/Users";
        }
        redirectAttributes.addFlashAttribute("DeleteUserSuccess",response.body());
        return "redirect:/Admin/Users";
    }

    @GetMapping("Admin/Users/unlocked/{id}")
    public String unlocked(@PathVariable int id,HttpSession session ,RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var response = userService.unlocked(id,session);
        if (response.statusCode() == 400){
            redirectAttributes.addFlashAttribute("DeleteUserSuccess",response.body());
            return "redirect:/Admin/Users";
        }
        if (response.statusCode() == 403){
            redirectAttributes.addFlashAttribute("DeleteUserSuccess","Forbidden !!!");
            return "redirect:/Admin/Users";
        }
        redirectAttributes.addFlashAttribute("DeleteUserSuccess",response.body());
        return "redirect:/Admin/Users";
    }


    @GetMapping(value = "/Users/Add")
    public String  addUser( HttpSession session, ModelMap model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        if (role.contains("[\"ROLE_MODERATOR\"]")){
            return "Admin/403";
        }
        model.addAttribute("AddUser", new SignUpRequest());
        return "Admin/Users/AddNewUsers";
    }
    @PostMapping(value = "/Users/Addnew")
    public String  addNewUser(SignUpRequest request, HttpSession session, ModelMap model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = inputFormat.parse(request.getDateOfbirth());
        var dateBirth = inputFormat.format(date);
        System.out.println(dateBirth);
        request.setDateOfbirth(dateBirth);

        String json = JSONUtils.convertToJSON(request);
        var respone = userService.CreateAccount(json,session);

        if (respone.statusCode() == 400){
            JSONObject ob = new JSONObject(respone.body());

            User userParse = JSONUtils.convertToObject(User.class,ob.toString());
            assert userParse != null;
            redirectAttributes.addFlashAttribute("userParseError",userParse);
            return "redirect:/Users/Add";
        }
        if(respone.statusCode() == 500){
            JSONObject ob = new JSONObject(respone.body());

            redirectAttributes.addFlashAttribute("errorMessageSignUp",ob.getString("message"));
            return "redirect:/Users/Add";
        }

        if (respone.statusCode() == 403){
            return "Admin/403";
        }

        redirectAttributes.addFlashAttribute("successProfile","Register change successfully");




        return "redirect:/Admin/Users";


    }

    @GetMapping("/Users/AccountBand")
    public  String AccountBand(Model model,HttpSession session) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var response = userService.FindUserBand(session);
        System.out.println(response);
        if (response.statusCode() != 200){
            return "Admin/403";
        }
        JSONArray ob = new JSONArray(response.body());

        User[] user = JSONUtils.convertToObject(User[].class,ob.toString());


        assert user != null;
        model.addAttribute("listUser", List.of(user));


        return "Admin/Users/AccountBand";
    }

}
