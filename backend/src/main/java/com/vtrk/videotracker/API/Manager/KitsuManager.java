package com.vtrk.videotracker.API.Manager;

import com.vtrk.videotracker.API.Kitsu;
import com.vtrk.videotracker.utils.Properties;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Kitsu API Manager
 * Unified interface for Kitsu API
 */
public class KitsuManager implements API_Manager{

    @Override
    public String search(String query, String content_type, HashMap<String, String> params) throws IOException {
        if(params.isEmpty()) {
            return switch (content_type) {
                case "anime" -> Kitsu.getInstance().searchAnime(query);
                case "manga" -> ""; //TODO: add manga support
                default -> TYPE_ERROR;
            };
        }
        else {
            return switch (content_type) {
                case "anime" -> Kitsu.getInstance().searchAnimeFilters(new HashMap<>(){{
                    put("text", query);
                    putAll(params);
                }});
                case "manga" -> ""; //TODO: add manga support
                default -> TYPE_ERROR;
            };
        }
    }

    @Override
    public String getById(String id, String content_type) throws IOException {
        return switch (content_type) {
            case "anime" -> Kitsu.getInstance().getAnimeById(id);
            case "manga" -> "";//TODO: add manga support
            default -> TYPE_ERROR;
        };
    }

    @Override
    public String getAPIName() {
        JSONObject response = new JSONObject();
        response.put("API", Kitsu.getInstance().getApiName());
        response.put("Version", Properties.getInstance().getProperty("VERSION"));
        return response.toString();
    }

    @Override
    public String getTrending(String content_type) throws IOException {
        return switch (content_type) {
            case "anime" -> Kitsu.getInstance().getTrendingAnime();
            case "manga" -> "";//TODO: add manga support
            default -> TYPE_ERROR;
        };
    }

}
