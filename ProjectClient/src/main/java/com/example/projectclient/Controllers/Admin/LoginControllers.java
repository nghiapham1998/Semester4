package com.example.projectclient.Controllers.Admin;

import com.example.projectclient.Service.LoginService;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@SessionAttributes({"tokenAdmin"})

public class LoginControllers {
    private final LoginService loginService;
    private final static String BASE_URL = "http://localhost:8080";
    public LoginControllers(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("Admin/login")
    public String login(Model model,HttpSession session,SessionStatus sessionStatus){
//        String role = (String) session.getAttribute("roles");
//        if (role.contains("[\"ROLE_USER\"]")){
//            return "Admin/Login";
//        }

        if (session.getAttribute("tokenAdmin") != null){
            return "Admin/index";
        }

//        sessionStatus.setComplete();
//        session.removeAttribute("token");
//        session.removeAttribute("roles");

        return "Admin/Login";
    }

    @PostMapping("Admin/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password, ModelMap model, HttpSession session,
                        RedirectAttributes attributes) throws IOException, InterruptedException {
        var response = loginService.login(username,password);
        JSONObject ob = new JSONObject(response.body());
        if (response.statusCode() != 200){
            model.addAttribute("error",ob.getString("message"));
            return "/Admin/Login";
        }


        String roles = ob.getJSONArray("roles").toString();
        if (roles.contains("USER")){
            model.addAttribute("accessDenied","Forbiden !!!");

            return "/Admin/Login";
        }

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
        session.setAttribute("tokenAdmin",token);
        session.setAttribute("id",id);


        return "redirect:/Admin/";
    }

    @GetMapping("Admin/logout")
    public String logout(HttpSession session, SessionStatus sessionStatus){
        sessionStatus.setComplete();
        session.removeAttribute("tokenAdmin");
        return "redirect:login";
    }
}
