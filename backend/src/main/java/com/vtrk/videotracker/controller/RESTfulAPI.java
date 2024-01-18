package com.vtrk.videotracker.controller;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.*;
import com.vtrk.videotracker.Database.Model.*;
import com.vtrk.videotracker.VideoTrackerApplication;
import com.vtrk.videotracker.utils.Properties;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
        User user = new User(0, email, username, password, false);
        UserDaoPostgres userDaoPostgres = new UserDaoPostgres(DBManager.getInstance().getConnection());

        JSONObject response = new JSONObject();

        if(userDaoPostgres.emailInUse(email))
            return response.put("response", "email_in_use").toString();

        userDaoPostgres.add(user);
        User userAdded = userDaoPostgres.findByEmail(email, password);
        if(userAdded == null)
            return response.put("response", "registration_failed").toString();

        UserListDaoPostgres userListDaoPostgres = new UserListDaoPostgres(DBManager.getInstance().getConnection());
        userListDaoPostgres.add(userAdded.getId());

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
     *   "email_username": "email_username",
     *   "password": "password"
     * }
     * <br>
     * Response: "0" if login fails, the user id otherwise
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
        String email_username = json.getString("email_username");
        String password = json.getString("password");

        UserDaoPostgres userDaoPostgres = new UserDaoPostgres(DBManager.getInstance().getConnection());
        User user = userDaoPostgres.findByEmail(email_username, password);
        JSONObject response = new JSONObject();
        if(user.getId() == 0)
            return response.put("response", "0").toString();
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

        UserDaoPostgres userDaoPostgres = new UserDaoPostgres(DBManager.getInstance().getConnection());
        userDaoPostgres.updateFromSettings(id, credential, choice);
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
        UserListDaoPostgres userListDaoPostgres = new UserListDaoPostgres(DBManager.getInstance().getConnection());
        UserList list = userListDaoPostgres.findByIdUser((int)id_user);
        ContainsDaoPostgres containsDaoPostgres = new ContainsDaoPostgres(DBManager.getInstance().getConnection());

        List<Contains> responseList = containsDaoPostgres.findContentInList(list.getId());
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
    public String profile(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        UserDaoPostgres userDaoPostgres = new UserDaoPostgres(DBManager.getInstance().getConnection());
        User user = userDaoPostgres.findById(id_user);
        UserListDaoPostgres userListDaoPostgres = new UserListDaoPostgres(DBManager.getInstance().getConnection());
        UserList list = userListDaoPostgres.findByIdUser(id_user);
        ContainsDaoPostgres containsDaoPostgres = new ContainsDaoPostgres(DBManager.getInstance().getConnection());
        String completed = containsDaoPostgres.countByState(list.getId(), "completed");
        String watching = containsDaoPostgres.countByState(list.getId(), "watching");
        String on_hold = containsDaoPostgres.countByState(list.getId(), "on-hold");
        String dropped = containsDaoPostgres.countByState(list.getId(), "dropped");
        String planning = containsDaoPostgres.countByState(list.getId(), "planning");
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
    public String addToList(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        String id_content = json.getString("id_content");
        String state = json.getString("status");

        // Add content to database if it doesn't exist
        ContentDaoPostgres contentDaoPostgres = new ContentDaoPostgres(DBManager.getInstance().getConnection());
        if(!contentDaoPostgres.exists(id_content)){
            String title = json.getString("title");
            int duration = json.getInt("duration");
            int n_episode = json.getInt("n_episode");
            String link = json.getString("link");
            Content content = new Content(id_content, title, duration, n_episode, link);
            contentDaoPostgres.add(content);
        }

        UserListDaoPostgres userListDaoPostgres = new UserListDaoPostgres(DBManager.getInstance().getConnection());
        UserList list = userListDaoPostgres.findByIdUser(id_user);
        ContainsDaoPostgres containsDaoPostgres = new ContainsDaoPostgres(DBManager.getInstance().getConnection());
        if(containsDaoPostgres.exists(list.getId(), id_content)){
            containsDaoPostgres.update(list.getId(), id_content, state);
            return "1";
        }
        containsDaoPostgres.add(list.getId(), id_content, state);
        if(containsDaoPostgres.exists(list.getId(), id_content))
            return "1";
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
    public String removeFromList(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        String id_content = json.getString("id_content");

        UserListDaoPostgres userListDaoPostgres = new UserListDaoPostgres(DBManager.getInstance().getConnection());
        UserList list = userListDaoPostgres.findByIdUser(id_user);
        ContainsDaoPostgres containsDaoPostgres = new ContainsDaoPostgres(DBManager.getInstance().getConnection());

        JSONObject response = new JSONObject();
        if(!containsDaoPostgres.exists(list.getId(), id_content))
            return response.put("response", "1").toString();

        containsDaoPostgres.remove(list.getId(), id_content);

        if(containsDaoPostgres.exists(list.getId(), id_content))
            return response.put("response", "1").toString();

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
    public String notifications(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        ReceiveDaoPostgres receiveDaoPostgres = new ReceiveDaoPostgres(DBManager.getInstance().getConnection());
        List<Notification> notifications = receiveDaoPostgres.findByIdUser(id_user);
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
    public String removeNotification(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        int id_notification = json.getInt("id_notification");
        ReceiveDaoPostgres receiveDaoPostgres = new ReceiveDaoPostgres(DBManager.getInstance().getConnection());
        JSONObject response = new JSONObject();
        if(!receiveDaoPostgres.exists(id_user, id_notification))
            return response.put("response", "1").toString();
        receiveDaoPostgres.remove(id_user, id_notification);
        if(receiveDaoPostgres.exists(id_user, id_notification))
            return response.put("response", "1").toString();
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
    public String reviews(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        String id_content = json.getString("id_content");
        ReviewDaoPostgres reviewDaoPostgres = new ReviewDaoPostgres(DBManager.getInstance().getConnection());
        List<Review> reviews = reviewDaoPostgres.findByIdContent(id_content);
        JSONObject response = new JSONObject();
        JSONArray responseArray = new JSONArray();
        for(Review review : reviews){
            JSONObject reviewJSON = new JSONObject();
            reviewJSON.put("id", review.getId());
            reviewJSON.put("vote", review.getVote());
            reviewJSON.put("user_comment", review.getUserComment());
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
    public String addReview(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        int vote = json.getInt("vote");
        String user_comment = json.getString("user_comment");
        int id_user = json.getInt("id_user");
        String id_content = json.getString("id_content");
        ReviewDaoPostgres reviewDaoPostgres = new ReviewDaoPostgres(DBManager.getInstance().getConnection());

        JSONObject response = new JSONObject();
        if(reviewDaoPostgres.exists(id_user, id_content))
            return response.put("response", "2").toString();

        reviewDaoPostgres.add(new Review(0, vote, user_comment, id_user, id_content));

        if(!reviewDaoPostgres.exists(id_user, id_content))
            return response.put("response", "1").toString();

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
    public String removeReview(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        int id_user = json.getInt("id_user");
        String id_content = json.getString("id_content");
        ReviewDaoPostgres reviewDaoPostgres = new ReviewDaoPostgres(DBManager.getInstance().getConnection());

        JSONObject response = new JSONObject();
        if(!reviewDaoPostgres.exists(id_user, id_content))
            return response.put("response", "1").toString();

        reviewDaoPostgres.remove(new Review(0, 0, "", id_user, id_content));

        if(reviewDaoPostgres.exists(id_user, id_content))
            return response.put("response", "1").toString();

        return response.put("response", "0").toString();
    }

}
