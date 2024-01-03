package com.vtrk.videotracker;

import com.vtrk.videotracker.utils.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.logging.Logger;

@SpringBootApplication
public class VideoTrackerApplication {

    static Logger logger = Logger.getLogger(VideoTrackerApplication.class.getName());

    public static void main(String[] args) throws IOException {
        SpringApplication.run(VideoTrackerApplication.class, args);

        logger.info("Version: " + Properties.getInstance().getProperty("VERSION"));
    }

}
