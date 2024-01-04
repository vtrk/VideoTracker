package com.vtrk.videotracker.API;

import com.vtrk.videotracker.utils.Properties;

import java.io.IOException;
import java.util.Map;

/**
 Kitsu API class
 <br>
 <a href="https://kitsu.docs.apiary.io/#reference">API Documentation</a>
 */
public class Kitsu extends API {

    /** Singleton instance */
    private static Kitsu instance = null;

    /**
        Get singleton instance
        @return {@link Kitsu} instance
     */
    public static Kitsu getInstance() {
        if (instance == null) {
            instance = new Kitsu();
        }
        return instance;
    }

    /**
        Constructor
     */
    protected Kitsu() {
        super("https://kitsu.io/api/edge", "Kitsu");
    }

    /**
        Search anime by name
        @param query query
        @return JSON response
        @throws IOException if connection fails
     */
    public String searchAnime(String query) throws IOException {
        return makeRequest("/anime?filter%5Btext%5D=" + query, "GET", new java.util.HashMap<>() {{
            put("Accept", "application/vnd.api+json");
            put("Content-Type", "application/vnd.api+json");
            put("User-Agent", "VideoTracker/" + Properties.getInstance().getProperty("VERSION"));
        }});
    }

    /**
        Search anime by queries and filters
        @param filtersQueries map of filters and queries
        @return JSON response
        @throws IOException if connection fails
     */
    public String searchAnimeFilters(Map<String, String> filtersQueries) throws IOException {
        StringBuilder query = new StringBuilder();
        for(String key : filtersQueries.keySet())
            query.append("filter%5B").append(key).append("%5D=").append(filtersQueries.get(key)).append("&");
        return makeRequest("/anime?" + query, "GET", new java.util.HashMap<>() {{
            put("Accept", "application/vnd.api+json");
            put("Content-Type", "application/vnd.api+json");
            put("User-Agent", "VideoTracker/" + Properties.getInstance().getProperty("VERSION"));
        }});
    }

    /**
        Get anime by ID
        @param id anime ID
        @return JSON response
        @throws IOException if connection fails
     */
    public String getAnimeById(String id) throws IOException {
        return makeRequest("/anime/" + id, "GET", new java.util.HashMap<>() {{
            put("Accept", "application/vnd.api+json");
            put("Content-Type", "application/vnd.api+json");
            put("User-Agent", "VideoTracker/" + Properties.getInstance().getProperty("VERSION"));
        }});
    }

    /**
        Get trending anime
        @return JSON response
        @throws IOException if connection fails
     */
    public String getTrendingAnime() throws IOException {
        return makeRequest("/trending/anime", "GET", new java.util.HashMap<>() {{
            put("Accept", "application/vnd.api+json");
            put("Content-Type", "application/vnd.api+json");
            put("User-Agent", "VideoTracker/" + Properties.getInstance().getProperty("VERSION"));
        }});
    }
}
