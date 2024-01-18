package com.vtrk.videotracker.Database;

import com.vtrk.videotracker.Database.Dao.*;
import com.vtrk.videotracker.Database.Dao.Postgres.*;
import com.vtrk.videotracker.utils.Properties;

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
                con = DriverManager.getConnection("jdbc:postgresql://" + Properties.getInstance().getProperty("DB_HOST"), Properties.getInstance().getProperty("DB_USER"), Properties.getInstance().getProperty("DB_PASSWORD"));
            } catch (SQLException e) {
                System.out.println("Error in getConnection\n"+e.getErrorCode());
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

    public ReviewDao getReviewDao(){return new ReviewDaoPostgres(getConnection());}

    public NotificationDao getNotificationDao(){return new NotificationDaoPostgres(getConnection());}
    public ReceiveDao getReceiveDao(){return new ReceiveDaoPostgres(getConnection());}
    public ContainsDao getContainsDao(){return new ContainsDaoPostgres(getConnection());}

}
