package com.vtrk.videotracker.API.Manager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * API Manager interface <br>
 * Purpose: to provide a common interface for all API classes
 * Acts as a wrapper for API classes
 */
public interface API_Manager {

    /**
     * Error message for invalid content type
     */
    String TYPE_ERROR = new JSONObject().put("error", "Content type not found").toString();

    /**
     * Search content via query
     * @param query query
     * @param content_type content type
     * @param params additional parameters
     * @return JSON response
     */
    String search(String query, String content_type, HashMap<String, String> params) throws IOException;

    /**
     * Get content by ID
     * @param id content ID
     * @param content_type content type
     * @return JSON response
     */
    String getById(String id, String content_type) throws IOException;

    /**
     * Get API name
     * @return JSON with API name and program version
     */
    String getAPIName();

    /**
     * Get trending content
     * @param content_type content type
     * @return JSON response
     */
    String getTrending(String content_type) throws IOException;

}
