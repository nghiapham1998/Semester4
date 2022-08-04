package com.example.projectclient.Controllers.Admin;

import com.example.projectclient.Service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@MultipartConfig
@Validated
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("Admin/Revenue")
    public String getAllRevenue(HttpSession session, Model model) throws IOException, InterruptedException {
        var response = revenueService.showAllRevenue(session);
        var usersActive = Integer.parseInt(revenueService.countUsersActive(session));
        var userLocked = Integer.parseInt(revenueService.countUsersLocked(session));
        var allOrders = revenueService.countAllProductOrder(session);
        var countOrderStatus = revenueService.countOrderByStatus(session);//index: 0:waiting, 1:delivery, 2:success, 3:cancel
        model.addAttribute("revenue", response);
        model.addAttribute("allUsers", usersActive+userLocked);
        model.addAttribute("usersActive",usersActive);
        model.addAttribute("userLocked",userLocked);
        model.addAttribute("allOrders",allOrders);
        model.addAttribute("countOrderStatus", countOrderStatus);
        return "Admin/Revenue/RevenueReport";
    }
}
