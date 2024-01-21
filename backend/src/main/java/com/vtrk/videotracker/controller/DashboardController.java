package com.vtrk.videotracker.controller;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.NotificationDao;
import com.vtrk.videotracker.Database.Dao.ReceiveDao;
import com.vtrk.videotracker.Database.Dao.UserDao;
import com.vtrk.videotracker.Database.Model.Notification;
import com.vtrk.videotracker.Database.Model.User;
import com.vtrk.videotracker.utils.Logger;
import com.vtrk.videotracker.utils.Properties;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @GetMapping
    @CrossOrigin
    public String showDashboard(Model model, HttpServletRequest request) {
        model.addAttribute("title", "Dashboard");
        if(Arrays.stream(request.getCookies()).noneMatch(cookie -> cookie.getName().equals("user")))
            return "redirect:/login";
        String userId = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("user")).findFirst().get().getValue();
        UserDao userDao = DBManager.getInstance().getUserDao();
        User user = userDao.findById(Integer.parseInt(userId));
        if(user.getEmail().isEmpty())
            return "redirect:/login?error=Invalid cookie";

        List<User> users = userDao.findAll(); // Get users from database
        users.removeIf(User::isIs_admin); // Remove admin from list
        model.addAttribute("users", users); // Add users to model
        model.addAttribute("version", Properties.getInstance().getProperty("VERSION"));
        return "dashboard";
    }

    @GetMapping("/login")
    @CrossOrigin
    public String showLogin(@RequestParam("error") Optional<String> variableName, Model model) {
        model.addAttribute("error", variableName.orElse(""));
        return "login";
    }

    @RequestMapping(
            value = "/dashboardLogin",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @CrossOrigin
    public String login(@RequestParam MultiValueMap<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) {
        String email = paramMap.get("email").get(0);
        String password = paramMap.get("password").get(0);
        UserDao userDao = DBManager.getInstance().getUserDao();
        User user = userDao.findByEmail(email, password);
        if(user.getId() == 0 || !user.isIs_admin())
            return "redirect:/login?error=Invalid email or password";
        else {
            System.out.println(user.getId());
            Cookie cookie = new Cookie("user", Integer.toString(user.getId()));
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
            return "redirect:/";
        }
    }

    @RequestMapping(
            value = "/dashboardLogout",
            method = RequestMethod.GET
    )
    @CrossOrigin
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }

    @RequestMapping(
            value = "/ban",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @CrossOrigin
    public String ban(@RequestParam MultiValueMap<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) {
        String userId = paramMap.get("button").get(0);
        UserDao userDao = DBManager.getInstance().getUserDao();
        userDao.ban(Integer.parseInt(userId));
        return "redirect:/";
    }

    @RequestMapping(
            value = "/sendNotification",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @CrossOrigin
    public String sendNotification(@RequestParam MultiValueMap<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) {
        String title = paramMap.get("title").get(0);
        String message = paramMap.get("message").get(0);
        UserDao userDao = DBManager.getInstance().getUserDao();
        List<User> users = userDao.findAll();
        NotificationDao notificationDao = DBManager.getInstance().getNotificationDao();
        Notification notification = new Notification(0, title, message);
        notificationDao.add(notification);
        ReceiveDao receiveDao = DBManager.getInstance().getReceiveDao();
        for(User user : users) {
            receiveDao.add(user.getId(), notification.getId());
        }
        return "redirect:/";
    }

    /**
     * Download log file
     * @return log file if exists, otherwise 404
     */
    @GetMapping("/log")
    @CrossOrigin
    public ResponseEntity<Resource> getLog() {
        if(Logger.getInstance().getLog() == null)
            return ResponseEntity.notFound().build();
        InputStreamResource resource;
        try {
             resource = new InputStreamResource(new FileInputStream(Logger.getInstance().getLog()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + Logger.getInstance().getLog().getName())
                .contentLength(Logger.getInstance().getLog().length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

}