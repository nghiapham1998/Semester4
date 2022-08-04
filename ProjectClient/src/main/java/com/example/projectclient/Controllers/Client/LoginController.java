package com.example.projectclient.Controllers.Client;


import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.SignUpRequest;
import com.example.projectclient.Models.UpdateProfileUser;
import com.example.projectclient.Models.User;
import com.example.projectclient.Models.changePasswordReset;
import com.example.projectclient.Service.Client.LoginClientService;
import com.example.projectclient.Service.FileUploadUtil;
import com.example.projectclient.Service.LoginService;
import com.example.projectclient.Service.UserService;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Controller
@SessionAttributes({"token"})
public class LoginController {
    private final LoginClientService loginService;

    private final UserService userService;
    public LoginController(LoginClientService loginService ,UserService userService)
    {
        this.userService = userService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password, ModelMap model, HttpSession session,
                        RedirectAttributes attributes) throws IOException, InterruptedException {
        var response = loginService.login(username,password);
        JSONObject ob = new JSONObject(response.body());
        if (response.statusCode() != 200){
            model.addAttribute("errorMessage",ob.getString("message"));
            model.addAttribute("SignUpRequest", new SignUpRequest());
            model.addAttribute("changePassword",new changePasswordReset());
            return "/Client/Login";
        }

        String roles = ob.getJSONArray("roles").toString();


        Long id = ob.getLong("id");
        String usernames = ob.getString("username");
        String linkImage = ob.getString("linkImage");
        String nameImage = ob.getString("nameImage");
        String emails = ob.getString("email");
        String tokenType = ob.getString("tokenType");
        String token = ob.getString("accessToken");

        session.setAttribute("usernamesClient",usernames);
        session.setAttribute("linkImage",linkImage);
        session.setAttribute("nameImage",nameImage);
        session.setAttribute("roles",roles);
        session.setAttribute("emails",emails);
        session.setAttribute("tokenType",tokenType);
        session.setAttribute("token",token);
        session.setAttribute("id",id);


        return "redirect:/Client";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session, SessionStatus sessionStatus){
        sessionStatus.setComplete();
        session.removeAttribute("usernamesClient");
        return "redirect:/Login";
    }


    @PostMapping(value = "/Signup")
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


            return "redirect:/Login";
        }
        if(respone.statusCode() == 500){
            JSONObject ob = new JSONObject(respone.body());

            redirectAttributes.addFlashAttribute("errorMessageSignUp",ob.getString("message"));
            return "redirect:/Login";
        }
        redirectAttributes.addFlashAttribute("successProfile","Successful registration please verify email");
        return "redirect:/Login";


    }

    @PostMapping("/changePassword")
    public String changePasswordAdmin(ModelMap model, changePasswordReset request
            , RedirectAttributes redirectAttributes, HttpSession session) throws IOException, InterruptedException {
        String json = JSONUtils.convertToJSON(request);
        var response = userService.changePasswordAdmin(json);

        if (response.statusCode() != 200){
            JSONObject ob = new JSONObject(response.body());
            redirectAttributes.addFlashAttribute("SendEmailError",ob.getString("message"));
            return "redirect:/Login";
        }
        session.setAttribute("tokenSendMailChangePassword",response);
        redirectAttributes.addFlashAttribute("SendEmailSuccess","Please login email to confirm");
        return "redirect:/Login";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(changePasswordReset request
            ,RedirectAttributes redirectAttributes) throws IOException, InterruptedException {

        String json = JSONUtils.convertToJSON(request);

        var response = userService.updatePassword(json);

        if (response.statusCode() == 500){
            JSONObject ob = new JSONObject(response.body());
            redirectAttributes.addFlashAttribute("UpdatePasswordError",ob.getString("message"));
            return "redirect:/resetPassword";
        }
        if (response.statusCode() == 400){
            JSONObject ob = new JSONObject(response.body());
            changePasswordReset passwordReset = JSONUtils.convertToObject(changePasswordReset.class,ob.toString());
            redirectAttributes.addFlashAttribute("errorResetPasswordEmail",passwordReset.getEmail());
            redirectAttributes.addFlashAttribute("errorResetPasswordPassword",passwordReset.getPassword());
            return "redirect:/resetPassword";
        }
        redirectAttributes.addFlashAttribute("UpdatePasswordSuccess","Update Password Successfully");
        return "redirect:/Login";
    }


    @GetMapping("/Profile")
    public String Profile(Model model,HttpSession sessions) throws IOException, InterruptedException {
        String session = (String) sessions.getAttribute("token");
        model.addAttribute("users", userService.userProfile(sessions,session));
        model.addAttribute("updateProfile",new UpdateProfileUser());
        return "Client/Profile";
    }

    @PostMapping("/changeImageProfile")
    public String changeImageProfile(HttpSession sessions, Model model
            , @RequestParam("image") MultipartFile multipartFile) throws IOException, InterruptedException, URISyntaxException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "user-photos";
        String session = (String) sessions.getAttribute("token");
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        File file = new File(multipartFile.getOriginalFilename());

        userService.changeImageUser(sessions,file);
        var user = userService.userProfile(sessions,session);
        sessions.setAttribute("linkImage",user.getLinkImage());
        return "redirect:/Profile";

    }

    @PostMapping("/updateProfile")
    public String updateProfile(RedirectAttributes redirectAttributes, ModelMap model,
                                @Valid User user,
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
            return "redirect:/Profile";
        }
        redirectAttributes.addFlashAttribute("successProfile","Save change successfully");




        return "redirect:/Profile";

    }
}
