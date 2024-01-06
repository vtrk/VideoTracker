package com.vtrk.videotracker;

import com.vtrk.videotracker.API.Kitsu;
import com.vtrk.videotracker.API.TMDB;
import com.vtrk.videotracker.utils.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = Kitsu.class)
class VideoTrackerApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void KitsuAPITest() {
        System.out.println("Kitsu API Test");
        assertEquals("Kitsu", Kitsu.getInstance().getApiName());
        try {
            System.out.println("Testing searchAnime\nJSON API Response: " + Kitsu.getInstance().searchAnime("bleach"));
            System.out.println("Testing getAnimeById\nJSON API Response: " + Kitsu.getInstance().getAnimeById("244"));
            System.out.println("Testing trendingAnime\nJSON API Response: " + Kitsu.getInstance().getTrendingAnime());
            System.out.println("Testing searchAnimeFilters\nJSON API Response: " + Kitsu.getInstance().searchAnimeFilters(new java.util.HashMap<>() {{
                put("text", "bleach");
                put("categories", "Action");
            }}));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        System.out.println("Kitsu API Test Passed");
    }

    @Test
    void TMDBAPITest() {
        System.out.println("TMDB API Test");
        if(Properties.getInstance().getProperty("TMDB_API_KEY").isEmpty())
            fail("TMDB_API_KEY not found in application.properties");
        assertEquals("TMDB", com.vtrk.videotracker.API.TMDB.getInstance().getApiName());
        try {
            System.out.println("Testing searchMovie\nJSON API Response: " + TMDB.getInstance().searchMovie("spider man"));
            System.out.println("Testing searchMovie (with year)\nJSON API Response: " + TMDB.getInstance().searchMovie("spider man", "2021"));
            System.out.println("Testing getMovieById\nJSON API Response: " + TMDB.getInstance().getMovieById("634649"));
            System.out.println("Testing searchTV\nJSON API Response: " + TMDB.getInstance().searchTV("the office"));
            System.out.println("Testing searchTV (with year)\nJSON API Response: " + TMDB.getInstance().searchTV("the office", "2005"));
            System.out.println("Testing getTVById\nJSON API Response: " + TMDB.getInstance().getTVById("2316"));
            System.out.println("Testing getAllTrending\nJSON API Response: " + TMDB.getInstance().getAllTrending());
            System.out.println("Testing getTrendingMovies\nJSON API Response: " + TMDB.getInstance().getTrendingMovies());
            System.out.println("Testing getTrendingTV\nJSON API Response: " + TMDB.getInstance().getTrendingTV());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        System.out.println("TMDB API Test Passed");
    }

}
