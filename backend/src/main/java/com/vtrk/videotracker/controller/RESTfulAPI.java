package com.vtrk.videotracker.controller;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.ContentDao;
import com.vtrk.videotracker.Database.Dao.Postgres.ContainsDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Postgres.ContentDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Postgres.UserDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Postgres.UserListDaoPostgres;
import com.vtrk.videotracker.Database.Model.Content;
import com.vtrk.videotracker.Database.Model.User;
import com.vtrk.videotracker.Database.Model.UserList;
import com.vtrk.videotracker.VideoTrackerApplication;
import com.vtrk.videotracker.utils.Properties;
import jakarta.servlet.http.HttpServletRequest;
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

        UserListDaoPostgres userListDaoPostgres = new UserListDaoPostgres(DBManager.getInstance().getConnection());
        userListDaoPostgres.add(userAdded.getId());

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


    /**
     * Update user data
     * @param data JSON user data
     * @param request request
     * @return
     *
     * A valid JSON user data looks like this:<br>
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
     * A valid JSON user data looks like this:<br>
     * {
     *  "id_user": "id"
     * }
     * <br>
     * Response (JSON of content):<br>
     * {
     *      ...
     *      "content": {
     *          "id_list": "id",
     *          "title": "title",
     *          "description": "description",
     *          "type": "type",
     *          "poster": "poster"
     *      }
     *      ...
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

        List<Content> responseList = containsDaoPostgres.findContentInList(list.getId());
        JSONObject response = new JSONObject();
        for(Content content : responseList){
            JSONObject contentJSON = new JSONObject();
            String[] id = content.getId().split("_"); // Avoid returning content from other APIs
            if((id[1].equals("movie") || id[1].equals("tv")) && !Properties.getInstance().getProperty("API").equals("TMDB"))
                continue;
            else if (id[1].equals("anime") && !Properties.getInstance().getProperty("API").equals("Kitsu"))
                continue;
            contentJSON.put("id", id[0]);
            contentJSON.put("title", content.getTitle());
            contentJSON.put("description", content.getDuration());
            contentJSON.put("type", content.getLink());
            contentJSON.put("poster", content.getN_episode());
            response.put(content.getId(), contentJSON);
        }
        return response.toString();
    }

    /**
     * Get profile data
     * @param data JSON user data
     * @return JSON API response
     * <br>
     * A valid JSON user data looks like this:<br>
     * {
     *      "id_user": "id"
     * }
     *<br>
     * Response (JSON of user data):<br>
     * {
     *     "id": "id",
     *     "email": "email",
     *     "username": "username",
     * }
     */


    /**
     * Add content to user list
     * @param data JSON user data
     * @return JSON API response
     * <br>
     * A valid JSON user data looks like this:<br>
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
        containsDaoPostgres.add(list.getId(), id_content, state);
        if(containsDaoPostgres.exists(list.getId(), id_content))
            return "1";
        return "0";
    }
}
