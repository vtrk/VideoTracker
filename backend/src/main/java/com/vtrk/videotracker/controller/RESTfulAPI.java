package com.vtrk.videotracker.controller;

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

}
