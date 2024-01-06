package com.vtrk.videotracker.API;

import com.vtrk.videotracker.utils.Properties;

import java.io.IOException;
import java.util.HashMap;

/**
 * The Movie Database API
 */
public class TMDB extends API{
    private static TMDB instance = null;
    private final String apiKey;

    /**
     * Constructor
     */
    private TMDB() {
        super("https://api.themoviedb.org/3", "TMDB");
        apiKey = Properties.getInstance().getProperty("TMDB_API_KEY");
    }

    /**
     * Get singleton instance
     * @return TMDB instance
     */
    public static TMDB getInstance() {
        if (instance == null) {
            instance = new TMDB();
        }
        return instance;
    }

    /**
     * Get movie by ID
     * @param id movie ID
     * @return JSON response
     * @throws IOException IOException
     */
    public String getMovieById(String id) throws IOException {
        return makeRequest("/movie/" + id + "?api_key=" + apiKey, "GET", new HashMap<>() {{
            put("accept", "application/json");
        }});
    }

    /**
     * Search movie by name and year
     * @param query query
     * @param year year
     * @return JSON response
     * @throws IOException IOException
     */
    public String searchMovie(String query, String year) throws IOException {
        return makeRequest("/search/movie?include_adult=false&api_key=" + apiKey + "&query=" + query.replaceAll(" ", "%20") + "&primary_release_year=" + year, "GET", new HashMap<>() {{
            put("accept", "application/json");
        }});
    }

    /**
     * Search movie by name
     * @param query query
     * @return JSON response
     * @throws IOException IOException
     */
    public String searchMovie(String query) throws IOException {
        return makeRequest("/search/movie?include_adult=false&api_key=" + apiKey + "&query=" + query.replaceAll(" ", "%20"), "GET", new HashMap<>() {{
            put("accept", "application/json");
        }});
    }

    /**
     * Get TV show by ID
     * @param id TV show ID
     * @return JSON response
     * @throws IOException IOException
     */
    public String getTVById(String id) throws IOException {
        return makeRequest("/tv/" + id + "?api_key=" + apiKey, "GET", new HashMap<>() {{
            put("accept", "application/json");
        }});
    }

    /**
     * Search TV show by name and year
     * @param query query
     * @param year year
     * @return JSON response
     * @throws IOException IOException
     */
    public String searchTV(String query, String year) throws IOException {
        return makeRequest("/search/tv?include_adult=false&api_key=" + apiKey + "&query=" + query.replaceAll(" ", "%20") + "&first_air_date_year=" + year, "GET", new HashMap<>() {{
            put("accept", "application/json");
        }});
    }

    /**
     * Search TV show by name
     * @param query query
     * @return JSON response
     * @throws IOException IOException
     */
    public String searchTV(String query) throws IOException {
        return makeRequest("/search/tv?include_adult=false&api_key=" + apiKey + "&query=" + query.replaceAll(" ", "%20"), "GET", new HashMap<>() {{
            put("accept", "application/json");
        }});
    }

    /**
     * Get trending movies and TV shows
     * @return JSON response
     * @throws IOException IOException
     */
    public String getAllTrending() throws IOException {
        return makeRequest("/trending/all/day?api_key=" + apiKey, "GET", new HashMap<>() {{
            put("accept", "application/json");
        }});
    }

    /**
     * Get trending movies
     * @return JSON response
     * @throws IOException IOException
     */
    public String getTrendingMovies() throws IOException {
        return makeRequest("/trending/movie/day?api_key=" + apiKey, "GET", new HashMap<>() {{
            put("accept", "application/json");
        }});
    }

    /**
     * Get trending TV shows
     * @return JSON response
     * @throws IOException IOException
     */
    public String getTrendingTV() throws IOException {
        return makeRequest("/trending/tv/day?api_key=" + apiKey, "GET", new HashMap<>() {{
            put("accept", "application/json");
        }});
    }

}
