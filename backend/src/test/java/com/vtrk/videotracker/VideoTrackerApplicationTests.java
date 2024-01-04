package com.vtrk.videotracker;

import com.vtrk.videotracker.API.Kitsu;
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
            System.out.println("Testing getAnimeById\nJSON API Response: " + Kitsu.getInstance().getAnimeById("1"));
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

}
