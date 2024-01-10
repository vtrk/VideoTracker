package com.vtrk.videotracker.Database;

import com.vtrk.videotracker.Database.Dao.*;
import com.vtrk.videotracker.Database.Dao.Postgres.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static DBManager instance = null;

    private DBManager() {}

    public static DBManager getInstance(){
        if (instance == null){
            instance = new DBManager();
        }
        return instance;
    }

    Connection con = null;

    public Connection getConnection(){
        if (con == null){
            try {
                con = DriverManager.getConnection("da_inserire", "postgres", "postgres");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return con;
    }

    public UserDao getUserDao(){
        return new UserDaoPostgres(getConnection());
    }

    public ContentDao getContentDao(){
        return new ContentDaoPostgres(getConnection());
    }

    public UserListDao getUserListDao(){
        return new UserListDaoPostgres(getConnection());
    }

    public ReviewDao getReviewDao(){
        return new ReviewDaoPostgres(getConnection());
    }

    public NotificationDao getNotificationDao(){
        return new NotificationDaoPostgres(getConnection());
    }

}
