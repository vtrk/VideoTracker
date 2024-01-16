package com.vtrk.videotracker;

import com.vtrk.videotracker.API.Kitsu;
import com.vtrk.videotracker.API.TMDB;
import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.*;
import com.vtrk.videotracker.Database.Model.*;
import com.vtrk.videotracker.utils.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

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

    @Test
    void DatabaseTest() {
        System.out.println("Database Test");

        //Texting UserDaoPostgres
        System.out.println("Testing UserDaoPostgres");
        UserDaoPostgres userDaoPostgres = new UserDaoPostgres(DBManager.getInstance().getConnection());
        List<User> prova = userDaoPostgres.findAll();
        for (User user : prova) {
            System.out.println(user.getUsername());
        }
        User a = userDaoPostgres.findByEmailOrUsername("Hokage","hinata");
        if (a.getId()!= 0){
            System.out.println("found");
        }else{
            System.out.println("not found");
        }
        User b = userDaoPostgres.findById(1);
        if (Objects.equals(b.getUsername(), "")){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        //Texting ContentDaoPostgres
        System.out.println("Testing ContentDaoPostgres");
        ContentDaoPostgres contentDaoPostgres = new ContentDaoPostgres(DBManager.getInstance().getConnection());
        Content c = contentDaoPostgres.findById("000Anime");
        if (Objects.equals(c.getTitle(), "")){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        Content d = contentDaoPostgres.findById("000anime");
        if (Objects.equals(d.getTitle(), "")){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        //Texting NotificationDaoPostgres
        System.out.println("Testing NotificationDaoPostgres");
        NotificationDaoPostgres notificationDaoPostgres = new NotificationDaoPostgres(DBManager.getInstance().getConnection());
        Notification e = notificationDaoPostgres.findById(1);
        if (Objects.equals(e.getTitle(), "")){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        /*
        List<Integer> prova2 = notificationDaoPostgres.findByIdUser(1);

        for (int id_notification : prova2) {
            System.out.println(id_notification);
        }
        */
        //Texting ReviewDaoPostgres
        System.out.println("Testing ReviewDaoPostgres");
        ReviewDaoPostgres reviewDaoPostgres = new ReviewDaoPostgres(DBManager.getInstance().getConnection());
        Review h = reviewDaoPostgres.findById(4);
        if (h.getIdUser() == 0){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        List<Review> prova3 = reviewDaoPostgres.findByIdContent("000anime");
        for (Review review : prova3) {
            System.out.println(review.getIdUser());
        }
        //Texting UserListDaoPostgres
        System.out.println("Testing UserListDaoPostgres");
        UserListDaoPostgres userListDaoPostgres = new UserListDaoPostgres(DBManager.getInstance().getConnection());
        UserList f =userListDaoPostgres.findByIdUser(1);
        if (f.getId() == 0){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        UserList g = userListDaoPostgres.findById(1);
        if (g.getIdUser() == 0){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        System.out.println("Testing ContainsDaoPostgres");
        ContainsDaoPostgres containsDaoPostgres = new ContainsDaoPostgres(DBManager.getInstance().getConnection());
        List<Content> provaContains =containsDaoPostgres.findContentInList(1);
        for (Content Cont: provaContains) {
            System.out.println(Cont.getTitle());
        }
        System.out.println("Testing ReceiveDaoPostgres");
        ReceiveDaoPostgres receiveDaoPostgres = new ReceiveDaoPostgres(DBManager.getInstance().getConnection());
        List<Notification> provaNotification =receiveDaoPostgres.findByIdUser(1);
        for (Notification Not: provaNotification) {
            System.out.println(Not.getDescription());
        }

    }

    @Test
    void testAddToDB(){
        UserDaoPostgres userDaoPostgres = new UserDaoPostgres(DBManager.getInstance().getConnection());

        User user = new User(0, "", "test", "test", false);

        userDaoPostgres.add(user);
        User user2 = userDaoPostgres.findByEmailOrUsername("test", "test");
        System.out.println(user2.getId());

        //Insert/Update/Delete (Nelle insert tutti gli id devono essere a zero, per chi ha un int, perchè viene fatto in automatico dal db)

        //User

        UserDaoPostgres userDao = new UserDaoPostgres(DBManager.getInstance().getConnection());

        User u1 = new User(2, "email1", "username1", "password1", false);
        User u2 = new User(0, "email2", "username2", "password2", true);
        User u3 = new User(3, "email2", "username2", "passwordChanged", false);

        userDao.add(u1);
        userDao.add(u2);
        /*
        userDao.update(u3);
        userDao.remove(u1);
        */

        //UserList
        UserListDaoPostgres list = new UserListDaoPostgres(DBManager.getInstance().getConnection());
        list.add(3);

        //Content
        ContentDaoPostgres content = new ContentDaoPostgres(DBManager.getInstance().getConnection());

        Content c1 = new Content("001anime", "Boruto", 24,293, "anime.com");
        Content c2 = new Content("002anime", "Naruto", 24,220, "anime.com");
        Content c3 = new Content("003anime", "Naruto Shippuden", 24,500, "anime.com");
        Content c4 = new Content("004anime", "Made in abyss", 24,48, "anime.com");

        content.add(c1);
        content.add(c2);
        content.add(c3);
        content.add(c4);

        /*c3 = new Content("003anime", "Naruto Shippuden", 22,500, "anime.com");

        content.update(c3);

        content.remove(c4);*/

        //Review

        ReviewDaoPostgres review = new ReviewDaoPostgres(DBManager.getInstance().getConnection());

        Review r1 = new Review(0, 5,"That's my son!!",1,"001anime");
        Review r2 = new Review(0, 5,"One of my favourite!",1,"002anime");
        Review r3 = new Review(6, 5,"Cool",1,"003anime");
        Review r4 = new Review(7, 2,"Buuuuu",3,"000anime");

        review.add(r1);
        review.add(r2);
        review.add(r3);
        review.add(r4);

        /*r4.setVote(1);

        review.update(r4);

        review.remove(r3);*/
        //Notification

        NotificationDaoPostgres notification = new NotificationDaoPostgres(DBManager.getInstance().getConnection());

        Notification n = new Notification(2, "Here something you might be interested in", "Vita di x");
        Notification n1 = new Notification(4, "Just a test", "just a test");

        notification.add(n);
        n.setDescription("Naruto");
        notification.add(n);
        notification.add(n1);

        /*n.setDescription("Boruto");
        notification.update(n);

        notification.remove(n1);*/

        //Receive
        ReceiveDaoPostgres receive = new ReceiveDaoPostgres(DBManager.getInstance().getConnection());

        receive.add(1,2);
        receive.add(3,1);
        receive.add(3,3);

        //receive.remove(3,3);

        //Contains
        ContainsDaoPostgres contains = new ContainsDaoPostgres(DBManager.getInstance().getConnection());

        contains.add(1,"001anime","watching");
        contains.add(1,"002anime","watching");
        contains.add(1,"003anime","watching");
        contains.add(2,"000anime","completed");
        contains.add(2,"001anime","completed");

        /*contains.update(1,"002anime","completed");
        contains.update(1,"003anime","completed");

        contains.remove(2,"001anime");*/
    }

}
