package com.vtrk.videotracker;

import com.vtrk.videotracker.API.Manager.API_Manager;
import com.vtrk.videotracker.API.Manager.KitsuManager;
import com.vtrk.videotracker.API.Manager.TMDBManager;
import com.vtrk.videotracker.utils.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.logging.Logger;

@SpringBootApplication
public class VideoTrackerApplication {

    public static API_Manager API_MANAGER;
    static Logger logger = Logger.getLogger(VideoTrackerApplication.class.getName());

    public static void main(String[] args) {
        if (Properties.getInstance().getProperty("API").equals("TMDB")) {
            API_MANAGER = new TMDBManager();
        } else if (Properties.getInstance().getProperty("API").equals("Kitsu")) {
            API_MANAGER = new KitsuManager();
        } else {
            throw new RuntimeException("API not found");
        }
        SpringApplication.run(VideoTrackerApplication.class, args);

        logger.info("Version: " + Properties.getInstance().getProperty("VERSION"));
    }

}
