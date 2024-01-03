package com.vtrk.videotracker.API;

import com.vtrk.videotracker.utils.Properties;

import java.io.IOException;
import java.net.MalformedURLException;

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
        @throws MalformedURLException
     */
    public static Kitsu getInstance() throws MalformedURLException {
        if (instance == null) {
            instance = new Kitsu();
        }
        return instance;
    }

    /**
        Constructor
     */
    protected Kitsu() throws MalformedURLException {
        super("https://kitsu.io/api/edge", "Kitsu");
    }

    /**
        Search anime by name
        @param query query
        @return JSON response
        @throws IOException
     */
    public String searchAnime(String query) throws IOException {
        return makeRequest("/anime?filter%5Btext%5D=" + query, "GET", new java.util.HashMap<String, String>() {{
            put("Accept", "application/vnd.api+json");
            put("Content-Type", "application/vnd.api+json");
            put("User-Agent", "VideoTracker/"+ Properties.getInstance().getProperty("VERSION"));
        }});
    }
}
