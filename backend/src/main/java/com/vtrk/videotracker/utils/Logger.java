package com.vtrk.videotracker.utils;

import com.vtrk.videotracker.VideoTrackerApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger class
 */
public class Logger {
    DateTimeFormatter dtf;
    LocalDateTime now;

    java.util.logging.Logger logger;

    String logFilePath;

    BufferedWriter writer;
    FileWriter fileWriter;

    private static Logger instance = null;

    /**
     * Constructor
     */
    private Logger() {
        logger = java.util.logging.Logger.getLogger(VideoTrackerApplication.class.getName());
        logFilePath = Properties.getInstance().getProperty("LOG_FILE_PATH");
        if(!logFilePath.isEmpty()) {
            dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            now = LocalDateTime.now();
            try {
                fileWriter = new FileWriter(logFilePath, true);
                writer = new BufferedWriter(fileWriter);
            } catch (Exception e) {
                System.out.println("Error in Logger constructor: " + e);
            }
        }
    }

    /**
     * Get instance
     * @return instance
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Log
     * @param message message
     */
    public void log(String message, java.util.logging.Level logLevel) {
        switch (logLevel.toString()) {
            case "SEVERE":
                logger.severe(message);
                break;
            case "WARNING":
                logger.warning(message);
                break;
            case "CONFIG":
                logger.config(message);
                break;
            case "FINE":
                logger.fine(message);
                break;
            case "FINER":
                logger.finer(message);
                break;
            case "FINEST":
                logger.finest(message);
                break;
            default:
                logger.info(message);
                break;
        }
        if(!logFilePath.isEmpty()) {
            try {
                writer.write(dtf.format(now) + ": " + message + "\n");
                writer.flush();
            } catch (Exception e) {
                System.out.println("Error in Logger log: " + e);
            }
        }
    }


}
