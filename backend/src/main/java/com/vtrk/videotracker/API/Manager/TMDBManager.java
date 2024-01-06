package com.vtrk.videotracker.API.Manager;

import com.vtrk.videotracker.API.TMDB;
import com.vtrk.videotracker.utils.Properties;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * TMDB API Manager
 * Unified interface for TMDB API
 */
public class TMDBManager implements API_Manager {


    @Override
    public String search(String query, String content_type, HashMap<String, String> params) throws IOException {
        if(params.isEmpty()){
            return switch (content_type) {
                case "movie" -> TMDB.getInstance().searchMovie(query);
                case "tv" -> TMDB.getInstance().searchTV(query);
                default -> TYPE_ERROR;
            };
        }
        else {
            return switch (content_type) {
                case "movie" -> TMDB.getInstance().searchMovie(query, params.get("primary_release_year"));
                case "tv" -> TMDB.getInstance().searchTV(query, params.get("first_air_date_year"));
                default -> TYPE_ERROR;
            };
        }
    }

    @Override
    public String getById(String id, String content_type) throws IOException {
        return switch (content_type) {
            case "movie" -> TMDB.getInstance().getMovieById(id);
            case "tv" -> TMDB.getInstance().getTVById(id);
            default -> TYPE_ERROR;
        };
    }

    @Override
    public String getAPIName() {
        JSONObject response = new JSONObject();
        response.put("API", TMDB.getInstance().getApiName());
        response.put("Version", Properties.getInstance().getProperty("VERSION"));
        return response.toString();
    }

    @Override
    public String getTrending(String content_type) throws IOException {
        return switch (content_type) {
            case "movie" -> TMDB.getInstance().getTrendingMovies();
            case "tv" -> TMDB.getInstance().getTrendingTV();
            default -> TYPE_ERROR;
        };
    }
}
