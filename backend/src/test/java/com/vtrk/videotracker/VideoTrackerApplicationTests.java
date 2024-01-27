package com.vtrk.videotracker;

import com.vtrk.videotracker.API.Kitsu;
import com.vtrk.videotracker.API.TMDB;
import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.*;
import com.vtrk.videotracker.Database.Dao.Postgres.*;
import com.vtrk.videotracker.Database.Model.*;
import com.vtrk.videotracker.utils.Properties;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


@SpringBootTest(classes = Kitsu.class)
class VideoTrackerApplicationTests {

    private Contains contains;
    private ContainsDaoPostgres containsDaoPostgres;
    private Content content;
    private ContentDaoPostgres contentDaoPostgres;
    private Notification notification;
    private NotificationDaoPostgres notificationDaoPostgres;
    private Receive receive;
    private ReceiveDaoPostgres receiveDaoPostgres;
    private Review review;
    private ReviewDaoPostgres reviewDaoPostgres;
    private User user;
    private UserDaoPostgres userDaoPostgres;
    private UserList userList;
    private UserListDaoPostgres userListDaoPostgres;

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

    /*
    * Test to check if functions of UserDao work
    */
    @Test
    void UserDaoTestFunction(){
        System.out.println("Testing UserDao");
        UserDao user = DBManager.getInstance().getUserDao();
        List<User> prova = user.findAll();
        for (User u : prova) {
            System.out.println(u.getUsername());
        }
        User a = user.findByEmail("naruto@konoga.com","hinata");
        if (a.getId()!= 0){
            System.out.println("found");
        }else{
            System.out.println("not found");
        }
        User b = user.findById(1);
        if (Objects.equals(b.getUsername(), "")){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
    }

     /*
     * Test to check if functions of ContentDao work
     */
    @Test
     void ContentDaoTestFunction(){
        System.out.println("Testing ContentDao");
        ContentDao content = DBManager.getInstance().getContentDao();
        Content c = content.findById("000_anime");
        if (Objects.equals(c.getTitle(), "")){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        Content d = content.findById("000_anime");
        if (Objects.equals(d.getTitle(), "")){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
    }

    /*
     * Test to check if functions of NotificationDao work
     */
    @Test
    void NotificationDaoTestFunction(){
        System.out.println("Testing NotificationDao");
        NotificationDao notification =DBManager.getInstance().getNotificationDao();
        Notification e = notification.findById(1);
        if (Objects.equals(e.getTitle(), "")){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
    }

    /*
     * Test to check if functions of ReviewDao work
     */
    @Test
    void ReviewDaoTestFunction(){
        System.out.println("Testing ReviewDao");
        ReviewDao review = DBManager.getInstance().getReviewDao();
        Review h = review.findById(4);
        if (h.getIdUser() == 0){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        List<Review> prova3 = review.findByIdContent("000_anime");
        for (Review r : prova3) {
            System.out.println(r.getIdUser());
        }
    }

    /*
     * Test to check if functions of UserListDao work
     */
    @Test
    void UserListDaoTestFunction(){
        System.out.println("Testing UserListDao");
        UserListDao userList = DBManager.getInstance().getUserListDao();
        UserList f =userList.findByIdUser(1);
        if (f.getId() == 0){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
        UserList g = userList.findById(1);
        if (g.getIdUser() == 0){
            System.out.println("not found");
        }else{
            System.out.println("found");
        }
    }

    /*
     * Test to check if functions of ContainsDao work
     */
    @Test
    void ContainsDaoTestFunction(){
        System.out.println("Testing ContainsDao");
        ContainsDao contains = DBManager.getInstance().getContainsDao();
        List<Contains> provaContains =contains.findContentInList(1);
        for (Contains Cont: provaContains) {
            System.out.println(Cont.getContent().getTitle());
        }
    }

    /*
     * Test to check if functions of ReceiveDao work
     */
    @Test
    void ReceiveDaoTestFunction(){
        System.out.println("Testing ReceiveDao");
        ReceiveDao receive = DBManager.getInstance().getReceiveDao();
        List<Notification> provaNotification =receive.findByIdUser(1);
        for (Notification Not: provaNotification) {
            System.out.println(Not.getDescription());
        }
    }

    /*
     * For testing the database add/remove/update functions we are not using the proxy, because we just need to test the query
     *
     * Run this test to initialize the database with some data to enter the website
     */

    @Test
    void initDB(){

        UserDao userDao = DBManager.getInstance().getUserDao();
        User user = new User(0, "test@test.com", "Test", "test", false,true);
        userDao.add(user);

        int id_user = userDao.findByEmail("test", "test").getId();
        UserListDao list = DBManager.getInstance().getUserListDao();
        list.add(id_user);

        NotificationDao notification = DBManager.getInstance().getNotificationDao();

        Notification n = new Notification(0, "Here something you might be interested in", "Vita di x");

        notification.add(n);

        ReceiveDao receive = DBManager.getInstance().getReceiveDao();

        receive.add(id_user,1);
    }


    @Test
    void testCounting(){
        ContainsDao contains = DBManager.getInstance().getContainsDao();
        String count = contains.countByState(1,"completed");
        String count1 = contains.countByState(1,"watching");
        String count2 = contains.countByState(1,"planned");
        System.out.println(count);
        System.out.println(count1);
        System.out.println(count2);
    }

    @Test
    void passwordEncryptionTest(){
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();

        String p1 = bc.encode("password");
        String p2 = bc.encode("password");
        String p3 = bc.encode("password");

        assertNotEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p2, p3);

        assertTrue(bc.matches("password", p1));
        assertTrue(bc.matches("password", p2));
        assertTrue(bc.matches("password", p3));

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
    }
    @Test
    void testUpdateFromSettings(){
        UserDao userDao = DBManager.getInstance().getUserDao();
        userDao.updateFromSettings(1,"", 4);
    }


    @BeforeEach
    void setUp(){
        content = new Content("ID000", "TestTitle", 0, 0, "testLink");
        notification = new Notification(0, "TestTitle", "TestDescription");
        user = new User(000, "test@test.it", "UsernameTest", "PasswordTest", false, false, false);
        userList = new UserList(0, 000);
        contains = new Contains(0,content,"TestState");
        receive = new Receive(000, 0);
        review = new Review(0, 0, "TestComment", 000, "ID000");

        containsDaoPostgres = new ContainsDaoPostgres(DBManager.getInstance().getConnection());
        contentDaoPostgres = new ContentDaoPostgres(DBManager.getInstance().getConnection());
        notificationDaoPostgres = new NotificationDaoPostgres(DBManager.getInstance().getConnection());
        receiveDaoPostgres = new ReceiveDaoPostgres(DBManager.getInstance().getConnection());
        reviewDaoPostgres = new ReviewDaoPostgres(DBManager.getInstance().getConnection());
        userDaoPostgres = new UserDaoPostgres(DBManager.getInstance().getConnection());
        userListDaoPostgres = new UserListDaoPostgres(DBManager.getInstance().getConnection());
    }

    //test on DB
    @Test
    void testAddUpdateAndRemoveContainsOnDB(){
        //add
        containsDaoPostgres.add(contains.getId_list(), contains.getContent().getId(), contains.getState());
        assertTrue(containsDaoPostgres.exists(user.getId(), contains.getContent().getId()));
        //update
        contains.setState("TestState2");
        containsDaoPostgres.update(contains.getId_list(), contains.getContent().getId(), contains.getState());
        assertEquals(contains, containsDaoPostgres.findByIDListAndIDContent(contains.getId_list(), contains.getContent()));
        //remove
        containsDaoPostgres.remove(contains.getId_list(), contains.getContent().getId());
        assertFalse(containsDaoPostgres.exists(user.getId(), contains.getContent().getId()));
    }

    @Test
    void testAddUpdateAndRemoveContentOnDB(){
        //add
        contentDaoPostgres.add(content);
        assertTrue(contentDaoPostgres.exists(content.getId()));
        //update
        content.setTitle("TestTitle2");
        content.setDuration(1);
        content.setN_episode(1);
        content.setLink("TestLink2");
        contentDaoPostgres.update(content);
        assertEquals(content, contentDaoPostgres.findById(content.getId()));
        //remove
        contentDaoPostgres.remove(content);
        assertFalse(contentDaoPostgres.exists(content.getId()));
    }

    @Test
    void testAddUpdateAndRemoveNotificationOnDB(){
        //add
        notificationDaoPostgres.add(notification);
        assertTrue(notificationDaoPostgres.exists(notification.getId()));
        //update
        notification.setTitle("TestTitle2");
        notification.setDescription("TestDescription2");
        notificationDaoPostgres.update(notification);
        assertEquals(notification, notificationDaoPostgres.findById(notification.getId()));
        //remove
        notificationDaoPostgres.remove(notification);
        assertFalse(notificationDaoPostgres.exists(notification.getId()));
    }

    @Test
    void testAddAndRemoveReceiveOnDB(){
        //add
        receiveDaoPostgres.add(receive.getId_user(), receive.getId_notification());
        assertTrue(receiveDaoPostgres.exists(receive.getId_user(), receive.getId_notification()));
        //remove
        receiveDaoPostgres.remove(receive.getId_user(), receive.getId_notification());
        assertFalse(receiveDaoPostgres.exists(receive.getId_user(), receive.getId_notification()));
    }

    @Test
    void testAddUpdateAndRemoveReviewOnDB(){
        //add
        reviewDaoPostgres.add(review);
        assertTrue(reviewDaoPostgres.exists(review.getIdUser(), review.getIdContent()));
        //update
        review.setVote(1);
        review.setUserComment("TestComment1");
        review.setIdUser(111);
        review.setIdContent("ID111");
        reviewDaoPostgres.update(review);
        assertEquals(review, reviewDaoPostgres.findById(review.getId()));
        //remove
        reviewDaoPostgres.remove(review.getId());
        assertFalse(reviewDaoPostgres.exists(review.getIdUser(), review.getIdContent()));
    }

    @Test
    void testAddUpdateAndRemoveUserOnDB(){
        //add
        userDaoPostgres.add(user);
        assertTrue(userDaoPostgres.exists(user.getId()));
        //update
        user.setEmail("test2@test.it");
        user.setPassword("Password2Test");
        user.setUsername("Username2Test");
        user.setIs_admin(true);
        userDaoPostgres.update(user);
        assertEquals(user, userDaoPostgres.findById(user.getId()));
        //delete
        userDaoPostgres.remove(user.getId());
        assertFalse(userDaoPostgres.exists(user.getId()));
    }

    @Test
    void testAddAndRemoveUserListOnDB(){
        //add
        userListDaoPostgres.add(userList.getIdUser());
        assertTrue(userListDaoPostgres.exists(userList.getIdUser()));
        //remove
        userListDaoPostgres.remove(userList.getIdUser());
        assertFalse(userListDaoPostgres.exists(userList.getIdUser()));
    }
}
