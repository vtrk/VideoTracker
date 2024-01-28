package com.vtrk.videotracker.utils;

import java.util.Map;

/**
 * Class to cache trending and search results
 */
public class Cache {

        /**
         * Trending cache
         */
        private String trendingCache;

        /**
         * Search cache <br>
         * Key: query <br>
         * Value: result
         */
        private final Map<String, String> searchCache;
        private static Cache instance = null;

        private Cache() {
            trendingCache = null;
            searchCache = new java.util.HashMap<>();
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
