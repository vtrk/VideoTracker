package com.vtrk.videotracker.controller;

import com.vtrk.videotracker.Database.Model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(name = "users") List<User> user , Model model) {
        for (User u : user) {
            System.out.println(u.getId());
            model.addAttribute("users", Integer.toString(u.getId()));
        }

        return "dashboard";
    }

    // Aggiungi altri metodi del controller, se necessario, per gestire altre richieste
}