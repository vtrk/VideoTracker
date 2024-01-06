package com.vtrk.videotracker.API;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/** Common API class for all API classes*/
public abstract class API {
    protected final String BASE_URL;
    protected final String API_NAME;

    protected API(String baseUrl, String apiName) {
        BASE_URL = baseUrl;
        API_NAME = apiName;
    }

    /**
        Get base URL
        @return base URL
     */
    public String getBaseUrl() {
        return BASE_URL;
    }

    /**
        Get API name
        @return API name
     */
    public String getApiName(){
        return API_NAME;
    }


    /**
        Make request to API
        @param request request
        @param method request method
        @param properties request properties
        @return JSON response
        @throws IOException if connection fails
     */
    protected String makeRequest(String request, String method, HashMap<String, String> properties) throws IOException {
        URL url = new URL(BASE_URL + request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if(properties != null)
            for(String key : properties.keySet())
                connection.setRequestProperty(key, properties.get(key));
        if(connection.getResponseCode() == 200) {
            java.util.Scanner s = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
        else
            return "Error " + connection.getResponseCode() + ": " + connection.getResponseMessage() + "\n" + connection.getErrorStream().toString();
    }

}
