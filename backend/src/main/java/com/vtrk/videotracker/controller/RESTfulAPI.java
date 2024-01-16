package com.vtrk.videotracker.controller;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.UserDaoPostgres;
import com.vtrk.videotracker.Database.Model.User;
import com.vtrk.videotracker.VideoTrackerApplication;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * RESTful API
 */
@RestController
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class RESTfulAPI {

    /**
     * Get API info
     * @param request request
     * @return JSON API response
     */
   @GetMapping("/api")
   @CrossOrigin
    public String api(HttpServletRequest request) {
       return VideoTrackerApplication.API_MANAGER.getAPIName();
    }

    // ***** The following endpoints use data from external APIs *****
    /**
     * Get content by ID
     * @param type content type
     * @param request request
     * @return JSON API response
     * @throws IOException IOException
     */
    @GetMapping("/content/**")
    @CrossOrigin
    public String content(@RequestParam String type, HttpServletRequest request) throws IOException {
        String contentID = request.getRequestURI().substring(9);
       return VideoTrackerApplication.API_MANAGER.getById(contentID, type);
    }

    /**
     * Get trending content
     * @param type content type
     * @param request request
     * @return JSON API response
     * @throws IOException IOException
     */
    @GetMapping("/trending")
    @CrossOrigin
    public String trending(@RequestParam String type, HttpServletRequest request) throws IOException {
        return VideoTrackerApplication.API_MANAGER.getTrending(type);
    }



    /**
     * Search content by query
     * @param data JSON search query
     * @param request request
     * @return JSON API response
     * @throws IOException IOException
     * A valid JSON search query looks like this:<br>
     * {
     *    "type": "content_type"
     *    "query": "search_query",
     *    "args": {
     *      "arg1": "arg1",
     *      "arg2": "arg2"
     *    }
     * }
     * mandatory fields: type, query
     */
    @RequestMapping(
            value = "/search",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String search(@RequestBody String data, HttpServletRequest request) throws IOException {
        JSONObject json = new JSONObject(data);
        String type = json.getString("type");
        String query = json.getString("query");
        if(!json.has("args")) // No args -> perform normal search query
            return VideoTrackerApplication.API_MANAGER.search(query, type, new HashMap<>());
        JSONObject args = json.getJSONObject("args");
        HashMap<String, String> argsMap = new HashMap<>();
        for(String key : args.keySet())
            argsMap.put(key, args.getString(key));
        return VideoTrackerApplication.API_MANAGER.search(query, type, argsMap);
    }
    //***** End of endpoints that use data from external APIs *****

    @RequestMapping("/error")
    @CrossOrigin
    public String error(HttpServletRequest request) {
        return request.getAttribute("javax.servlet.error.status_code").toString();
    }


    /**
     * Register a new user
     * @param data JSON user data
     * @param request request
     * @return
     *
     * A valid JSON user data looks like this:<br>
     * {
     *   "username": "username",
     *   "email": "email",
     *   "password": "password"
     * }
     */
    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String register(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        String username = json.getString("username");
        String email = json.getString("email");
        String password = json.getString("password");

        User user = new User(0, email, username, password, false);
        UserDaoPostgres userDaoPostgres = new UserDaoPostgres(DBManager.getInstance().getConnection());
        userDaoPostgres.add(user);
        User userAdded = userDaoPostgres.findByEmailOrUsername(email, password);
        return Integer.toString(userAdded.getId());
    }

    /**
     * Login
     * @param data JSON user data
     * @param request request
     * @return
     *
     * A valid JSON user data looks like this:<br>
     * {
     *   "email_username": "email_username",
     *   "password": "password"
     * }
     */
    @RequestMapping(
            value = "/login",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String login(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        String email_username = json.getString("email_username");
        String password = json.getString("password");

        UserDaoPostgres userDaoPostgres = new UserDaoPostgres(DBManager.getInstance().getConnection());
        User user = userDaoPostgres.findByEmailOrUsername(email_username, password);
        if(user.getId() == 0)
            return "0";
        return Integer.toString(user.getId());
    }
}
