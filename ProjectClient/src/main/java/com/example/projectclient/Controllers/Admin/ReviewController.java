package com.example.projectclient.Controllers.Admin;

import com.example.projectclient.Config.JSONUtils;
import com.example.projectclient.Models.CreateCategory;
import com.example.projectclient.Models.ForbiddenReview;
import com.example.projectclient.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/Admin")
public class ReviewController {

    @Autowired
    ReviewService reviewService;
    @PostMapping("/reviews/add")
    public String showAll(HttpSession session, @ModelAttribute("forbiddenword") ForbiddenReview forbiddenReview) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        String json = JSONUtils.convertToJSON(forbiddenReview);
        var response  = reviewService.addWord(json,session);
        return "redirect:/Admin/reviews";
    }

    @GetMapping("/reviews/delete/{id}")
    public String showAll(@PathVariable Long id ,HttpSession session, @ModelAttribute("forbiddenword") ForbiddenReview forbiddenReview) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var response  = reviewService.deleteWord(id,session);
         if (response.statusCode() != 200){
             return "redirect:/Admin/reviews";
         }
        return "redirect:/Admin/reviews";
    }

    @GetMapping("/reviews/commnet/{id}")
    public String Deletecommnet(@PathVariable Long id ,HttpSession session) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }
        var response  = reviewService.deleteComment(id,session);
        if (response.statusCode() != 200){
            return "redirect:/Admin/reviews";
        }
        return "redirect:/Admin/reviews";
    }


    @GetMapping("/reviews-details-{id}")
    public String DetailsCommnet(@PathVariable Long id , HttpSession session, Model model) throws IOException, InterruptedException {
        String role = (String) session.getAttribute("roles");
        if (session.getAttribute("tokenAdmin") == null ||role.contains("[\"ROLE_USER\"]") ){

            return "redirect:/Admin/login";
        }

        model.addAttribute("reviewDetails",reviewService.getDetails(session,id));
        return "Admin/Review/datails";
    }
}
