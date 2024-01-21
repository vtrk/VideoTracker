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
import java.util.logging.Level;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Dashboard controller
 */
@Controller
public class DashboardController {

    private final String cookieName = "user";
    private final BCryptPasswordEncoder bc = new BCryptPasswordEncoder();

    /**
     * Show dashboard
     * @param model model
     * @param request request
     * @return dashboard page if cookie is valid, otherwise redirect to login page
     */
    @GetMapping
    @CrossOrigin
    public String showDashboard(Model model, HttpServletRequest request) {
        Logger.getInstance().logREST("Requested dashboard", Level.INFO, request);
        model.addAttribute("title", "Dashboard");
        if(Arrays.stream(request.getCookies()).noneMatch(cookie -> cookie.getName().equals(cookieName))) {
            Logger.getInstance().logREST("Client not logged in", Level.WARNING, request);
            return "redirect:/login";
        }
        String userId = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName)).findFirst().get().getValue();
        UserDao userDao = DBManager.getInstance().getUserDao();
        User user = userDao.findById(Integer.parseInt(userId));
        if(user.getEmail().isEmpty()){
            Logger.getInstance().logREST("Client has an invalid cookie", Level.WARNING, request);
            return "redirect:/login?error=Invalid cookie";
        }
        List<User> users = userDao.findAll(); // Get users from database
        users.removeIf(User::isIs_admin); // Remove admin from list
        model.addAttribute("users", users); // Add users to model
        model.addAttribute("version", Properties.getInstance().getProperty("VERSION"));
        Logger.getInstance().logREST("Dashboard sent", Level.INFO, request);
        return "dashboard";
    }

    /**
     * Show login page
     * @param variableName error message
     * @param model model
     * @return login page
     */
    @GetMapping("/login")
    @CrossOrigin
    public String showLogin(@RequestParam("error") Optional<String> variableName, Model model, HttpServletRequest request) {
        Logger.getInstance().logREST("Requested login page", Level.INFO, request);
        model.addAttribute("error", variableName.orElse(""));
        return "login";
    }

    /**
     * Login
     * @param paramMap parameters
     * @param request request
     * @param response response
     * @return redirect to dashboard
     */
    @RequestMapping(
            value = "/dashboardLogin",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @CrossOrigin
    public String login(@RequestParam MultiValueMap<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) {
        String email = paramMap.get("email").get(0);
        Logger.getInstance().logREST("Logging in user " + email, Level.INFO, request);
        String password = paramMap.get("password").get(0);
        UserDao userDao = DBManager.getInstance().getUserDao();
        User user = userDao.findByEmailOnly(email);
        if(user.getId() == 0 || !user.isIs_admin() || !bc.matches(password, user.getPassword())) {
            Logger.getInstance().logREST(email + ": Invalid email or password", Level.WARNING, request);
            return "redirect:/login?error=Invalid email or password";
        }
        else {
            Cookie cookie = new Cookie(cookieName, Integer.toString(user.getId()));
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
            Logger.getInstance().logREST("Logged in user with id " + user.getId(), Level.INFO, request);
            return "redirect:/";
        }
    }

    /**
     * Logout
     * @param request request
     * @param response response
     * @return redirect to login page
     */
    @RequestMapping(
            value = "/dashboardLogout",
            method = RequestMethod.GET
    )
    @CrossOrigin
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Logger.getInstance().logREST("User logged out", Level.INFO, request);
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }

    /**
     * Ban user
     * @param paramMap parameters
     * @param request request
     * @param response response
     * @return redirect to dashboard
     */
    @RequestMapping(
            value = "/ban",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @CrossOrigin
    public String ban(@RequestParam MultiValueMap<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) {
        String userId = paramMap.get("button").get(0);
        Logger.getInstance().logREST("Update ban status for user with ID " + userId, Level.INFO, request);
        UserDao userDao = DBManager.getInstance().getUserDao();
        userDao.ban(Integer.parseInt(userId));
        return "redirect:/";
    }

    /**
     * Send notification to all users
     * @param paramMap parameters
     * @param request request
     * @param response response
     * @return redirect to dashboard
     */
    @RequestMapping(
            value = "/sendNotification",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @CrossOrigin
    public String sendNotification(@RequestParam MultiValueMap<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) {
        Logger.getInstance().logREST("Sending notification", Level.INFO, request);
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
    public ResponseEntity<Resource> getLog(HttpServletRequest request) {
        Logger.getInstance().logREST("Requested a log file", Level.INFO, request);
        if(Logger.getInstance().getLog() == null) {
            Logger.getInstance().logREST("Log file not found", Level.WARNING, request);
            return ResponseEntity.notFound().build();
        }
        InputStreamResource resource;
        try {
             resource = new InputStreamResource(new FileInputStream(Logger.getInstance().getLog()));
        } catch (FileNotFoundException e) {
            Logger.getInstance().logREST("Error while reading log file " + e, Level.SEVERE, request);
            throw new RuntimeException(e);
        }
        Logger.getInstance().logREST("Log file sent", Level.INFO, request);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + Logger.getInstance().getLog().getName())
                .contentLength(Logger.getInstance().getLog().length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}