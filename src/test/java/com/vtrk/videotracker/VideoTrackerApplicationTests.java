package com.vtrk.videotracker;

import com.vtrk.videotracker.API.Kitsu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Kitsu.class)
class VideoTrackerApplicationTests {

    @Autowired
    Kitsu kitsu;
    @Test
    void contextLoads() {
    }

    @Test
    void KitsuAPITest() {
        System.out.println("Kitsu API Test");
        try {
            System.out.println("JSON API Response: " + kitsu.searchAnime("bleach"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
