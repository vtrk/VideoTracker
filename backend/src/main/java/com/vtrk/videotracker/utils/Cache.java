package com.vtrk.videotracker.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Singleton class to cache trending and search results
 */
public class Cache {

        /**
         * Max size of search cache<br>
         * Uses application.properties to get cache size<br>
         * Property: CACHE_SIZE; defaults to 250 <br>
         */
        private final int MAX_SIZE = Properties.getInstance().getProperty("CACHE_SIZE") == null ? 250 : Integer.parseInt(Properties.getInstance().getProperty("CACHE_SIZE"));

        /**
         * Trending cache
         */
        private String trendingCache;

        /**
         * Search cache <br>
         * Key: query <br>
         * Value: result
         */
        private final LinkedHashMap<String, String> searchCache;
        private static Cache instance = null;

        /**
         * Constructor<br>
         * Search cache is a LinkedHashMap with a custom removeEldestEntry method to limit the size of the cache.<br>
         * When the size of the cache exceeds MAX_SIZE, the eldest entry is removed.<br>
         * @see LinkedHashMap
         */
        private Cache() {
            trendingCache = null;
            searchCache = new java.util.LinkedHashMap<>() {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    return size() > MAX_SIZE;
                }
            };
        }

        public static Cache getInstance() {
            if (instance == null) {
                instance = new Cache();
            }
            return instance;
        }

        /**
         * Get trending cache
         * @return trending cache
         */
        public String getTrendingCache() {
            return trendingCache;
        }

        /**
         * Clear trending cache
         */
        public void clearTrendingCache() {
            trendingCache = null;
        }

        /**
         * Set trending cache
         * @param trendingCache trending cache
         */
        public void setTrendingCache(String trendingCache) {
            this.trendingCache = trendingCache;
        }


        /**
         * Get search cache
         * @return search cache
         */
        public Map<String, String> getSearchCache() {
            return searchCache;
        }

        /**
         * Add search cache
         * @param query query key
         * @param result result value
         */
        public void addSearchCache(String query, String result) {
            this.searchCache.put(query, result);
        }

        /**
         * Clear search cache
         */
        public void clearSearchCache() {
            this.searchCache.clear();
        }
}
