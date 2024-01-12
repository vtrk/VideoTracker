package com.vtrk.videotracker.API;

import com.vtrk.videotracker.utils.Properties;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 Kitsu API class
 <br>
 <a href="https://kitsu.docs.apiary.io/#reference">API Documentation</a>
 */
public class Kitsu extends API {

    /** Search results only need the following fields
     *  <ul>
     *      <li>canonicalTitle</li>
     *      <li>posterImage</li>
     *  </ul>
     *  */
    private static final List<String> searchResultsFields = List.of(
            "canonicalTitle",
            "posterImage"
    );

    /** Fields that should be displayed in anime page
     * <ul>
     *     <li>slug</li>
     *     <li>titles</li>
     *     <li>posterImage</li>
     *     <li>synopsis</li>
     *     <li>episodeCount</li>
     *     <li>episodeLength</li>
     *     <li>showType</li>
     *     <li>status</li>
     *     <li>startDate</li>
     *     <li>endDate</li>
     *     <li>ageRating</li>
     *     <li>genres</li>
     * */
    private static final List<String> animeFields = List.of(
            "slug",
            "titles",
            "posterImage",
            "synopsis",
            "episodeCount",
            "episodeLength",
            "showType",
            "status",
            "startDate",
            "endDate",
            "ageRating",
            "genres"
    );

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
        Search anime by name <br>
        Gets only the fields in {@link Kitsu#searchResultsFields}
        @param query query
        @return JSON response
        @throws IOException if connection fails
     */
    public String searchAnime(String query) throws IOException {
        return makeRequest("/anime?page%5Blimit%5D=20&filter%5Btext%5D=" + query.replaceAll(" ", "%20") + "&fields%5Banime%5D=" + String.join(",", searchResultsFields), "GET", new java.util.HashMap<>() {{
            put("Accept", "application/vnd.api+json");
            put("Content-Type", "application/vnd.api+json");
            put("User-Agent", "VideoTracker/" + Properties.getInstance().getProperty("VERSION"));
        }});
    }

    /**
        Search anime by queries and filters <br>
        Gets only the fields in {@link Kitsu#searchResultsFields}
        @param queryFilters map of query and filters
        @return JSON response
        @throws IOException if connection fails
     */
    public String searchAnimeFilters(Map<String, String> queryFilters) throws IOException {
        StringBuilder query = new StringBuilder();
        for(String key : queryFilters.keySet())
            query.append("filter%5B").append(key).append("%5D=").append(queryFilters.get(key)).append("&");
        query.append("fields%5Banime%5D=").append(String.join(",", searchResultsFields));
        return makeRequest("/anime?page%5Blimit%5D=20&" + query, "GET", new java.util.HashMap<>() {{
            put("Accept", "application/vnd.api+json");
            put("Content-Type", "application/vnd.api+json");
            put("User-Agent", "VideoTracker/" + Properties.getInstance().getProperty("VERSION"));
        }});
    }

    /**
        Get anime by ID
        Gets only the fields in {@link Kitsu#animeFields}
        @param id anime ID
        @return JSON response
        @throws IOException if connection fails
     */
    public String getAnimeById(String id) throws IOException {
        return makeRequest("/anime/" + id + "?include=genres&fields%5Banime%5D=" + String.join(",", animeFields), "GET", new java.util.HashMap<>() {{
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
