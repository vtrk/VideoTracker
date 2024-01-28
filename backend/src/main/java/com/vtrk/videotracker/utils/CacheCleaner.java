package com.vtrk.videotracker.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

/**
 * Class that clears cache according to schedule
 */
@Configuration
@EnableScheduling
public class CacheCleaner {

    /**
     * Clear trending cache<br>
     * Scheduled by Spring<br>
     * Uses application.properties to get cache time<br>
     * Property: TRENDING_CACHE_TIME (in hours) <br>
     * @see Cache
     */
    @Scheduled(fixedDelayString = "${TRENDING_CACHE_TIME}", timeUnit = TimeUnit.HOURS, initialDelayString = "${TRENDING_CACHE_TIME}")
    public void clearTrendingCache() {
        Cache.getInstance().clearTrendingCache();
        Logger.getInstance().log("Trending Cache cleared", java.util.logging.Level.INFO);
    }

    /**
     * Clear search cache<br>
     * Scheduled by Spring<br>
     * Uses application.properties to get cache time<br>
     * Property: SEARCH_CACHE_TIME (in hours) <br>
     * @see Cache
     */
    @Scheduled(fixedDelayString = "${SEARCH_CACHE_TIME}", timeUnit = TimeUnit.HOURS, initialDelayString = "${SEARCH_CACHE_TIME}")
    public void clearSearchCache() {
        Cache.getInstance().clearSearchCache();
        Logger.getInstance().log("Search Cache cleared", java.util.logging.Level.INFO);
    }
}
