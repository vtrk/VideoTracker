package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.NotificationDao;
import com.vtrk.videotracker.Database.Model.Content;
import com.vtrk.videotracker.Database.Model.Notification;
import com.vtrk.videotracker.Database.Model.Review;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDaoPostgres implements NotificationDao {
    Connection connection = null;

    public NotificationDaoPostgres(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Notification findById(int id) {
        Notification notification = new Notification(id, "","");
        try {
            String query = "SELECT * FROM notification WHERE id = "+id+";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                notification.setTitle(rs.getString("title"));
                notification.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll"+e);
        }
        return notification;
    }

    /*
        Returns a list of notifications id for a given user
    */
    @Override
    public List<Integer> findByIdUser(int id_user) {
        List<Integer> notifications = new ArrayList<Integer>();
        try {
            String query = "SELECT * FROM review WHERE id_user = "+id_user+";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                notifications.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll"+e);
        }
        return notifications;
    }

    @Override
    public void add(Notification notification) {

    }

    @Override
    public void update(Notification notification) {

    }

    @Override
    public void remove(Notification notification) {

    }
}
