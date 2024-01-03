package com.vtrk.videotracker.utils;

/**
    Singleton class to access application.properties
 */
public class Properties {
    private static Properties instance = null;
    private final java.util.Properties properties;

    /**
        Get singleton instance
        @return Properties instance
     */
    public static Properties getInstance() {
        if (instance == null) {
            instance = new Properties();
        }
        return instance;
    }

    /**
        Constructor
     */
    private Properties() {
        properties = new java.util.Properties();
        try {
            properties.load(Properties.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
        Get property by key
        @param key key
        @return property
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
