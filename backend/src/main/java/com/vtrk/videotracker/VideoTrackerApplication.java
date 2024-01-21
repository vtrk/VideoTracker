package com.vtrk.videotracker;

import com.vtrk.videotracker.API.Manager.API_Manager;
import com.vtrk.videotracker.API.Manager.KitsuManager;
import com.vtrk.videotracker.API.Manager.TMDBManager;
import com.vtrk.videotracker.utils.Logger;
import com.vtrk.videotracker.utils.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class VideoTrackerApplication {

    public static API_Manager API_MANAGER;

    public static void main(String[] args) {
        if (Properties.getInstance().getProperty("API").equals("TMDB")) {
            API_MANAGER = new TMDBManager();
        } else if (Properties.getInstance().getProperty("API").equals("Kitsu")) {
            API_MANAGER = new KitsuManager();
        } else {
            throw new RuntimeException("API not found");
        }
        ConfigurableApplicationContext context =SpringApplication.run(VideoTrackerApplication.class, args);
        Logger.getInstance().log("Application started", java.util.logging.Level.INFO);
        Logger.getInstance().log("Version: " + Properties.getInstance().getProperty("VERSION"), java.util.logging.Level.INFO);
        Logger.getInstance().log("API: " + Properties.getInstance().getProperty("API"), java.util.logging.Level.INFO);
    }

}
