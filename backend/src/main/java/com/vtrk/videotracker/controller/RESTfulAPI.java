package com.vtrk.videotracker.controller;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.*;
import com.vtrk.videotracker.Database.Dao.Proxy.*;
import com.vtrk.videotracker.Database.Model.*;
import com.vtrk.videotracker.VideoTrackerApplication;
import com.vtrk.videotracker.utils.Cache;
import com.vtrk.videotracker.utils.Logger;
import com.vtrk.videotracker.utils.Properties;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * RESTful API
 */
@RestController
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class RESTfulAPI {

    private final BCryptPasswordEncoder bc = new BCryptPasswordEncoder();

    /**
     * Get API info
     * @param request request
     * @return JSON API response
     */
   @GetMapping("/api")
   @CrossOrigin
    public String api(HttpServletRequest request) {
       Logger.getInstance().logREST("API info requested", java.util.logging.Level.INFO, request);
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
        Logger.getInstance().logREST("Content requested: " + contentID + " Type: "+ type, java.util.logging.Level.INFO, request);
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
        if(Cache.getInstance().getTrendingCache() != null) {
            Logger.getInstance().logREST("Trending content requested. Cache used", java.util.logging.Level.INFO, request);
            return Cache.getInstance().getTrendingCache();
        }
        Logger.getInstance().logREST("Trending content requested", java.util.logging.Level.INFO, request);
        String response = VideoTrackerApplication.API_MANAGER.getTrending(type);
        Cache.getInstance().setTrendingCache(response);
        return response;
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
        Logger.getInstance().logREST("Search requested", java.util.logging.Level.INFO, request);
        JSONObject json = new JSONObject(data);
        String type = json.getString("type");
        String query = json.getString("query");

        // Use cached response if available
        if(Cache.getInstance().getSearchCache().containsKey(json.toString())) {
            Logger.getInstance().logREST("Using cache to process request", java.util.logging.Level.INFO, request);
            return Cache.getInstance().getSearchCache().get(json.toString());
        }

        if(!json.has("args")) // No args -> perform normal search query
            return VideoTrackerApplication.API_MANAGER.search(query, type, new HashMap<>());
        JSONObject args = json.getJSONObject("args");
        HashMap<String, String> argsMap = new HashMap<>();
        for(String key : args.keySet())
            argsMap.put(key, args.getString(key));

        // Add response to cache
        String response = VideoTrackerApplication.API_MANAGER.search(query, type, argsMap);
        Cache.getInstance().addSearchCache(json.toString(), response);
        return response;
    }
    //***** End of endpoints that use data from external APIs *****

    @RequestMapping("/error")
    @CrossOrigin
    public String error(HttpServletRequest request) {
        Logger.getInstance().logREST("Error: " + request.getAttribute("javax.servlet.error.status_code").toString(), Level.WARNING, request);
        return request.getAttribute("javax.servlet.error.status_code").toString();
    }


    /**
     * Register a new user
     * @param data JSON user data
     * @param request request
     * @return
     *
     * A valid JSON request looks like this:<br>
     * {
     *   "username": "username",
     *   "email": "email",
     *   "password": "password"
     * }
     * <br>
     * <h4>Response</h4>
     * <ul>
     *   <li>"email_in_use" if email is already in use</li>
     *   <li>"registration_failed" if the registration fails</li>
     *   <li>the user id of the newly created account if the registration is successful</li>
     * </ul>
     * JSON format:<br>
     * {
     *  "response": "response"
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
        User u = new User(0, email, username, bc.encode(password), false,true);
        ProxyUser proxyUser = new ProxyUser();
        ProxyUserList proxyUserList = new ProxyUserList();
        UserDao user = DBManager.getInstance().getUserDao();

        Logger.getInstance().logREST("Attempting to register user " + email, java.util.logging.Level.INFO, request);

        JSONObject response = new JSONObject();

        if(user.emailInUse(email)) {
            Logger.getInstance().logREST("Email already in use", java.util.logging.Level.WARNING, request);
            return response.put("response", "email_in_use").toString();
        }
        proxyUser.request(1, u);

        User userAdded = user.findByEmailOnly(email);
        if(userAdded == null) {
            Logger.getInstance().logREST("Registration failed", java.util.logging.Level.WARNING, request);
            return response.put("response", "registration_failed").toString();
        }

        proxyUserList.request(1,  userAdded.getId());
        Logger.getInstance().logREST("Registration successful. User ID: " + userAdded.getId(), java.util.logging.Level.INFO, request);
        return response.put("response", Integer.toString(userAdded.getId())).toString();
    }

    /**
     * Login
     * @param data JSON user data
     * @param request request
     * @return
     *
     * A valid JSON request looks like this:<br>
     * {
     *   "email": "email",
     *   "password": "password"
     * }
     * <br>
     * Response: "0" if login fails, -1 if the user is banned, the user id otherwise
     * {
     *     "response": "response"
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
        String email = json.getString("email_username");
        String password = json.getString("password");
        Logger.getInstance().logREST("Attempting to login user " + email, java.util.logging.Level.INFO, request);
        UserDao userDao = DBManager.getInstance().getUserDao();
        User user = userDao.findByEmailOnly(email);
        JSONObject response = new JSONObject();
        if(user.getId() == 0 || !bc.matches(password, user.getPassword())) {
            Logger.getInstance().logREST("Login failed", java.util.logging.Level.WARNING, request);
            return response.put("response", "0").toString();
        }
        if(user.isBanned()) {
            Logger.getInstance().logREST("Login failed. User " + email + " is banned", java.util.logging.Level.WARNING, request);
            return response.put("response", "-1").toString();
        }
        Logger.getInstance().logREST("Login successful. User ID: " + user.getId(), java.util.logging.Level.INFO, request);
        return response.put("response", Integer.toString(user.getId())).toString();
    }


    /**
     * Update user data
     * @param data JSON user data
     * @param request request
     * @return
     *
     * A valid JSON request looks like this:<br>
     * {
     *   "id_user": "id",
     *   "credential": "credential",
     *   "choice": "choice"
     * }
     * choice: 1 - email, 2 - password, 3 - username
     */
    @RequestMapping(
            value = "/update",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String update(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id = json.getInt("id_user");
        String credential = json.getString("credential");
        int choice = json.getInt("choice");
        Logger.getInstance().logREST("Attempting to update user " + id, java.util.logging.Level.INFO, request);
        UserDao userDao = DBManager.getInstance().getUserDao();
        if(choice == 2)
            credential = bc.encode(credential);
        userDao.updateFromSettings(id, credential, choice);
        return "1";
    }

    /**
     * Get user list
     * @param data JSON user data
     * @param request request
     * @return JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *  "id_user": "id"
     * }
     * <br>
     * Response (JSON of content):<br>
     * {
     *      "content":[
     *      ...
     *      {
     *          "id_list": "id",
     *          "title": "title",
     *          "description": "description",
     *          "type": "type",
     *          "poster": "poster"
     *      }
     *      ...
     *      ]
     * }
     */
    @RequestMapping(
            value = "/list",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String list(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        long id_user = json.getLong("id_user");
        UserListDao userListDao = DBManager.getInstance().getUserListDao();
        UserList list = userListDao.findByIdUser((int)id_user);
        ContainsDao containsDao = DBManager.getInstance().getContainsDao();
        Logger.getInstance().logREST("List requested for user " + id_user, java.util.logging.Level.INFO, request);
        List<Contains> responseList = containsDao.findContentInList(list.getId());
        JSONObject response = new JSONObject();
        JSONArray responseArray = new JSONArray();
        for(Contains content : responseList){
            JSONObject contentJSON = new JSONObject();
            String[] id = content.getContent().getId().split("_"); // Avoid returning content from other APIs
            if((id[1].equals("movie") || id[1].equals("tv")) && !Properties.getInstance().getProperty("API").equals("TMDB"))
                continue;
            else if (id[1].equals("anime") && !Properties.getInstance().getProperty("API").equals("Kitsu"))
                continue;
            contentJSON.put("id", id[0]);
            contentJSON.put("title", content.getContent().getTitle());
            contentJSON.put("type", id[1]);
            contentJSON.put("poster", content.getContent().getLink());
            contentJSON.put("state", content.getState());
            responseArray.put(contentJSON);
        }
        response.put("content", responseArray);
        return response.toString();
    }

    /**
     * Get profile data
     * @param data JSON user data
     * @return JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *      "id_user": "id"
     * }
     *<br>
     * Response (JSON of user data):<br>
     * {
     *     "id": "id",
     *     "email": "email",
     *     "username": "username",
     *     "completed": "completed",
     *     "watching": "watching",
     *     "on_hold": "on-hold",
     *     "dropped": "dropped",
     *     "planning": "planning"
     * }
     */
    @RequestMapping(
            value = "/profile",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String profile(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        Logger.getInstance().logREST("Profile requested for user " + id_user, java.util.logging.Level.INFO, request);
        UserDao userDao = DBManager.getInstance().getUserDao();
        User user = userDao.findById(id_user);
        UserListDao userListDao = DBManager.getInstance().getUserListDao();
        UserList list = userListDao.findByIdUser(id_user);
        ContainsDao containsDao = DBManager.getInstance().getContainsDao();
        String completed = containsDao.countByState(list.getId(), "completed");
        String watching = containsDao.countByState(list.getId(), "watching");
        String on_hold = containsDao.countByState(list.getId(), "on hold");
        String dropped = containsDao.countByState(list.getId(), "dropped");
        String planning = containsDao.countByState(list.getId(), "plan to watch");
        JSONObject response = new JSONObject();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("username", user.getUsername());
        response.put("completed", completed);
        response.put("watching", watching);
        response.put("on_hold", on_hold);
        response.put("dropped", dropped);
        response.put("plan_to_watch", planning);
        return response.toString();
    }


    /**
     * Add content to user list
     * @param data JSON user data
     * @return JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *     "id_user": "id",
     *     "id_content": "id",
     *     "status": "state"
     *     "title": "title"
     *     "duration": "duration"
     *     "n_episode": "n_episode"
     *     "link": "link"
     * }
     * <br>
     * Response: "1" if successful, "0" otherwise
     */
    @RequestMapping(
            value = "/addToList",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String addToList(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        String id_content = json.getString("id_content");
        String state = json.getString("status");
        Logger.getInstance().logREST("Adding content " + id_content + " to list of user " + id_user, java.util.logging.Level.INFO, request);
        RESTfulAPI.saveIfNotExist(id_content, json);

        UserListDao userListDao = DBManager.getInstance().getUserListDao();
        UserList list = userListDao.findByIdUser(id_user);

        ProxyContains proxyContains = new ProxyContains();

        if(DBManager.getInstance().getContainsDao().exists(list.getId(), id_content)){
            Logger.getInstance().logREST("Content " + id_content + " already in list of user " + id_user, java.util.logging.Level.WARNING, request);
            proxyContains.request(2, new Contains(list.getId(),DBManager.getInstance().getContentDao().findById(id_content), state));
            return "1";
        }
        proxyContains.request(1, new Contains(list.getId(),DBManager.getInstance().getContentDao().findById(id_content), state));
        if(DBManager.getInstance().getContainsDao().exists(list.getId(), id_content)) {
            Logger.getInstance().logREST("Content " + id_content + " added to list of user " + id_user, java.util.logging.Level.INFO, request);
            return "1";
        }
        Logger.getInstance().logREST("Content " + id_content + " not added to list of user " + id_user, java.util.logging.Level.WARNING, request);
        return "0";
    }

    /**
     * Remove content from user list
     * @param data JSON user data
     * @return JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *     "id_user": "id",
     *     "id_content": "id"
     * }
     * <br>
     * <br>
     * <h4>Response</h4>
     * <ul>
     *     <li>"0" if successful</li>
     *     <li>"1" if it fails</li>
     * </ul>
     * {
     *     "response": "response"
     * }
     */
    @RequestMapping(
            value = "/removeFromList",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String removeFromList(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        String id_content = json.getString("id_content");
        Logger.getInstance().logREST("Removing content " + id_content + " from list of user " + id_user, java.util.logging.Level.INFO, request);

        UserListDao userListDao = DBManager.getInstance().getUserListDao();
        UserList list = userListDao.findByIdUser(id_user);
        ProxyContains proxyContains = new ProxyContains();

        JSONObject response = new JSONObject();
        if(!DBManager.getInstance().getContainsDao().exists(list.getId(), id_content)) {
            Logger.getInstance().logREST("Content " + id_content + " not in list of user " + id_user, java.util.logging.Level.WARNING, request);
            return response.put("response", "1").toString();
        }

        proxyContains.request(3, new Contains(list.getId(),DBManager.getInstance().getContentDao().findById(id_content), ""));

        if(DBManager.getInstance().getContainsDao().exists(list.getId(), id_content)) {
            Logger.getInstance().logREST("Content " + id_content + " not removed from list of user " + id_user, java.util.logging.Level.WARNING, request);
            return response.put("response", "1").toString();
        }

        Logger.getInstance().logREST("Content " + id_content + " removed from list of user " + id_user, java.util.logging.Level.INFO, request);
        return response.put("response", "0").toString();
    }

    /**
     * Get Notifications for a user
     * @param data JSON user data
     * @return JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *     "id_user": "id",
     * }
     * <br>
     * Response: a JSON of notifications<br>
     * {
     *     "notifications": [
     *     ...
     *     {
     *          "id": "id",
     *          "title": "title",
     *          "description": "description",
     *      }
     *      ...
     *      ]
     * }
     */
    @RequestMapping(
            value = "/notifications",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String notifications(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        Logger.getInstance().logREST("Notifications requested for user " + id_user, java.util.logging.Level.INFO, request);
        ReceiveDao receiveDao = DBManager.getInstance().getReceiveDao();
        List<Notification> notifications = receiveDao.findByIdUser(id_user);
        JSONObject response = new JSONObject();
        JSONArray responseArray = new JSONArray();
        for(Notification notification : notifications){
            JSONObject notificationJSON = new JSONObject();
            String id = Integer.toString(notification.getId());
            notificationJSON.put("id", id);
            notificationJSON.put("title", notification.getTitle());
            notificationJSON.put("description", notification.getDescription());
            responseArray.put(notificationJSON);
        }
        response.put("notifications", responseArray);
        return response.toString(); 
    }

    /**
     * Removes notification for a user
     * @param data JSON user data
     * @returns JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *    "id_user": "id",
     *    "id_notification": "id"
     * }
     * <br>
     * <h4>Response</h4>
     * <ul>
     *     <li>"0" if successful</li>
     *     <li>"1" if it fails</li>
     * </ul>
     * {
     *     "response": "response"
     * }
     */
    @RequestMapping(
            value = "/removeNotification",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String removeNotification(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        int id_notification = json.getInt("id_notification");
        Logger.getInstance().logREST("Attempting to remove notification " + id_notification + " for user " + id_user, java.util.logging.Level.INFO, request);

        ReceiveDao receiveDao = DBManager.getInstance().getReceiveDao();
        ProxyReceive proxyReceive = new ProxyReceive();
        JSONObject response = new JSONObject();
        if(!receiveDao.exists(id_user, id_notification)) {
            Logger.getInstance().logREST("Notification " + id_notification + " for user " + id_user + " doesn't exist", java.util.logging.Level.WARNING, request);
            return response.put("response", "1").toString();
        }

        proxyReceive.request(2, new Receive(id_user, id_notification));
        if(receiveDao.exists(id_user, id_notification)) {
            Logger.getInstance().logREST("Notification " + id_notification + " for user " + id_user + " not removed", java.util.logging.Level.WARNING, request);
            return response.put("response", "1").toString();
        }

        Logger.getInstance().logREST("Notification " + id_notification + " for user " + id_user + " removed", java.util.logging.Level.INFO, request);
        return response.put("response", "0").toString();
    }


    /**
     * Get reviews for specified content
     * @params data JSON user data
     * @return JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *    "id_content": "id",
     * }
     * <br>
     * Response: a JSON of reviews<br>
     * {
     *    "reviews": [
     *    ...
     *    {
     *      "id": "id",
     *      "vote": "vote",
     *      "user_comment": "user_comment",
     *      "username": "username",
     *      "id_user": "id_user",
     *      "id_content": "id_content"
     *    }
     *    ...
     *    ]
     */
    @RequestMapping(
            value = "/reviews",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String reviews(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        String id_content = json.getString("id_content");
        Logger.getInstance().logREST("Reviews requested for content " + id_content, java.util.logging.Level.INFO, request);

        ReviewDao reviewDao = DBManager.getInstance().getReviewDao();
        List<Review> reviews = reviewDao.findByIdContent(id_content);
        JSONObject response = new JSONObject();
        JSONArray responseArray = new JSONArray();
        UserDao userDao = DBManager.getInstance().getUserDao();
        for(Review review : reviews){
            JSONObject reviewJSON = new JSONObject();
            User user = userDao.findById(review.getIdUser());
            reviewJSON.put("id", review.getId());
            reviewJSON.put("vote", review.getVote());
            reviewJSON.put("user_comment", review.getUserComment());
            reviewJSON.put("username", user.getUsername());
            reviewJSON.put("id_user", review.getIdUser());
            reviewJSON.put("id_content", review.getIdContent());
            responseArray.put(reviewJSON);
        }
        response.put("reviews", responseArray);
        return response.toString();
    }

    /**
     * Add review for specified content
     * @params data JSON user data
     * @return JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *   "vote": "vote",
     *   "user_comment": "user_comment",
     *   "id_user": "id_user",
     *   "id_content": "id_content"
     *   "title": "title",
     *   "duration": "duration",
     *   "n_episode": "n_episode",
     *   "link": "link"
     * }
     * <br>
     * <h4>Response</h4>
     * <ul>
     *   <li>"0" if successful</li>
     *   <li>"1" if it fails</li>
     *   <li>"2" if the user has already written a review</li>
     * </ul>
     * {
     *  "response": "response"
     *  }
     */
    @RequestMapping(
            value = "/addReview",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String addReview(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int vote = json.getInt("vote");
        String user_comment = json.getString("user_comment");
        int id_user = json.getInt("id_user");
        String id_content = json.getString("id_content");

        Logger.getInstance().logREST("Attempting to add review for content " + id_content + " for user " + id_user, java.util.logging.Level.INFO, request);
        RESTfulAPI.saveIfNotExist(id_content, json);

        ReviewDao reviewDao = DBManager.getInstance().getReviewDao();
        ProxyReview proxyReview = new ProxyReview();

        JSONObject response = new JSONObject();
        if(reviewDao.exists(id_user, id_content)) {
            Logger.getInstance().logREST("User " + id_user + " has already written a review for content " + id_content, java.util.logging.Level.WARNING, request);
            return response.put("response", "2").toString();
        }
        proxyReview.request(1, new Review(0, vote, user_comment, id_user, id_content));

        if(!reviewDao.exists(id_user, id_content)) {
            Logger.getInstance().logREST("Review for content " + id_content + " for user " + id_user + " not added", java.util.logging.Level.WARNING, request);
            return response.put("response", "1").toString();
        }
        Logger.getInstance().logREST("Review for content " + id_content + " for user " + id_user + " added", java.util.logging.Level.INFO, request);
        return response.put("response", "0").toString();
    }

    /**
     * Remove review for specified content
     * @params data JSON user data
     * @return JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *      "id_user": "id_user",
     *      "id_content": "id_content"
     * }
     * <br>
     * <h4>Response</h4>
     * <ul>
     *     <li>"0" if successful</li>
     *     <li>"1" if it fails</li>
     * </ul>
     * {
     *      "response": "response"
     * }
     */
    @RequestMapping(
            value = "/removeReview",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String removeReview(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        String id_content = json.getString("id_content");
        ReviewDao reviewDao = DBManager.getInstance().getReviewDao();
        ProxyReview proxyReview = new ProxyReview();
        Logger.getInstance().logREST("Attempting to remove review for content " + id_content + " for user " + id_user, java.util.logging.Level.INFO, request);
        JSONObject response = new JSONObject();

        if(!reviewDao.exists(id_user, id_content)) {
            Logger.getInstance().logREST("Review of content with id "+ id_content + " by user with id " + id_user +" doesn't exist", java.util.logging.Level.WARNING, request);
            return response.put("response", "1").toString();
        }
        int id_review = reviewDao.findByIdUserAndContent(id_user, id_content);

        if(id_review != -1)
            proxyReview.request(3, id_review);
        else{
            Logger.getInstance().logREST("Review of content with id "+ id_content + " by user with id " + id_user +" doesn't exist", java.util.logging.Level.WARNING, request);
            return response.put("response", "1").toString();
        }

        if(reviewDao.exists(id_user, id_content))
            return response.put("response", "1").toString();

        Logger.getInstance().logREST("Review of content with id "+ id_content + " by user with id " + id_user +" removed", java.util.logging.Level.INFO, request);
        return response.put("response", "0").toString();
    }

    /**
     * Removes a user
     * @param data JSON user data
     * @returns JSON API response
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *     "id_user": "id_user"
     *     "password": "password"
     * }
     * <br>
     * <h4>Response</h4>
     * <ul>
     *     <li>"0" if successful</li>
     *     <li>"1" if it fails</li>
     * </ul>
     * {
     *    "response": "response"
     * }
     */
    @RequestMapping(
            value = "/removeUser",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String removeUser(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        Logger.getInstance().logREST("Attempting to remove user " + id_user, java.util.logging.Level.INFO, request);
        String password = json.getString("password");
        ProxyContains proxyContains = new ProxyContains();
        ProxyUserList proxyUserList = new ProxyUserList();
        ProxyReview proxyReview = new ProxyReview();
        ProxyReceive proxyReceive = new ProxyReceive();
        ProxyUser proxyUser = new ProxyUser();
        UserListDao userListDao = DBManager.getInstance().getUserListDao();
        UserDao userDao = DBManager.getInstance().getUserDao();
        JSONObject response = new JSONObject();

        // Check if the user exists
        if(!userDao.exists(id_user)) {
            Logger.getInstance().logREST("User with id " + id_user + " doesn't exist", java.util.logging.Level.WARNING, request);
            return response.put("response", "1").toString();
        }

        // Check if password is correct
        User user = userDao.findById(id_user);
        if(!user.getPassword().equals(password)) {
            Logger.getInstance().logREST("Wrong password for user with id " + id_user, java.util.logging.Level.WARNING, request);
            return response.put("response", "1").toString();
        }

        // To remove a user, we need to remove all the content in his list, all his reviews and all his notifications
        proxyContains.request(4, userListDao.findByIdUser(id_user).getId());
        proxyUserList.request(2, id_user);
        proxyReview.request(4, id_user);
        proxyReceive.request(3,id_user);
        proxyUser.request(3, id_user);
        Logger.getInstance().logREST("User with id " + id_user + " removed", java.util.logging.Level.INFO, request);
        return response.put("response", "0").toString();
    }


    /**
     * Check if user is banned
     * @param data JSON user data
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *     "id_user": "id_user"
     * }
     * <br>
     * <h4>Response</h4>
     * <ul>
     *     <li>"0" if the user is not banned</li>
     *     <li>"1" if the user is banned</li>
     * </ul>
     * {
     *    "response": "response"
     * }
     */
    @RequestMapping(
            value = "/isBanned",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String isBanned(@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        Logger.getInstance().logREST("Checking if user with id " + id_user + " is banned", java.util.logging.Level.INFO, request);
        UserDao userDao = DBManager.getInstance().getUserDao();
        JSONObject response = new JSONObject();
        if(userDao.isBanned(id_user)) {
            Logger.getInstance().logREST("User with id " + id_user + " is banned", java.util.logging.Level.INFO, request);
            return response.put("response", "1").toString();
        }
        Logger.getInstance().logREST("User with id " + id_user + " is not banned", java.util.logging.Level.INFO, request);
        return response.put("response", "0").toString();
    }

    /**
     * Check if content exists in database. If it doesn't, add it.
     * @param id_content
     * @param json
     */
    private static void saveIfNotExist(String id_content, JSONObject json){
        Logger.getInstance().log("Checking if content " + id_content + " exists in database", java.util.logging.Level.INFO);
        // Add content to database if it doesn't exist
        ContentDao contentDao = DBManager.getInstance().getContentDao();
        if(!contentDao.exists(id_content)){
            Logger.getInstance().log("Content " + id_content + " doesn't exist in database. Adding it", java.util.logging.Level.INFO);
            String title = json.getString("title");
            int duration = json.getInt("duration");
            int n_episode = json.getInt("n_episode");
            String link = json.getString("link");
            ProxyContent proxyContent = new ProxyContent();
            proxyContent.request(1, new Content(id_content, title, duration, n_episode, link));
        }
    }

    /**
     * Check if user wants notification by email
     * @param data JSON user data
     * <br>
     * A valid JSON request looks like this:<br>
     * {
     *     "id_user": "id_user"
     * }
     * <br>
     * <h4>Response</h4>
     * <ul>
     *     <li>"0" if the user does not want notification by email</li>
     *     <li>"1" if the user wants notification by email</li>
     * </ul>
     * {
     *    "response": "response"
     * }
     */
    @RequestMapping(
            value = "/wantNotification",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String  wantNotification (@RequestBody String data, HttpServletRequest request) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        Logger.getInstance().logREST("Checking if user with id " + id_user + " wants notification by email", java.util.logging.Level.INFO, request);
        UserDao userDao = DBManager.getInstance().getUserDao();
        JSONObject response = new JSONObject();
        if(userDao.getNotificationByEmail(id_user)) {
            Logger.getInstance().logREST("User with id " + id_user + " wants notification by email", java.util.logging.Level.INFO, request);
            return response.put("response", "1").toString();
        }
        Logger.getInstance().logREST("User with id " + id_user + " does not want notification by email", java.util.logging.Level.INFO, request);
        return response.put("response", "0").toString();
    }

    /**
     * Check if user wants notification by email
     * @param id_user
     */
    @RequestMapping(
            value = "/setWantNotification",
            method = RequestMethod.POST,
            consumes = "text/plain"
    )
    @CrossOrigin
    public String setWantNotification (@RequestBody String id_user, HttpServletRequest request) {
        JSONObject json = new JSONObject(id_user);
        int id = json.getInt("id_user");
        Logger.getInstance().logREST("Setting if user with id " + id + " wants notification by email", java.util.logging.Level.INFO, request);
        UserDao userDao = DBManager.getInstance().getUserDao();
        userDao.updateFromSettings(id, "", 4);
        JSONObject response = new JSONObject();
        return response.put("response", "1").toString();
    }

}
