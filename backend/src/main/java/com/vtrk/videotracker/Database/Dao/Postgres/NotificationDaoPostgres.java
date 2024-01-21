package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.NotificationDao;
import com.vtrk.videotracker.Database.Dao.Subject;
import com.vtrk.videotracker.Database.Model.Content;
import com.vtrk.videotracker.Database.Model.Notification;
import com.vtrk.videotracker.Database.Model.Review;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDaoPostgres implements NotificationDao, Subject {
    Connection connection = null;

    public NotificationDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    /**
     * Find notification by id
     * @param id id
     * @return notification
     */
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
            System.out.println("Error in findAll "+e);
        }
        return notification;
    }

    /**
     * Add notification
     * @param notification notification
     */
    @Override
    public void add(Notification notification) {
        //
        try{
            String query = "INSERT INTO public.notification (id, title, description) VALUES(nextval('notification_id_seq'::regclass), '"+notification.getTitle()+"', '"+notification.getDescription()+"') RETURNING id;";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                notification.setId(rs.getInt("id"));
            }
        }catch(SQLException e){
            System.out.println("Error in add "+e);
        }
    }

    /**
     * Update notification
     * @param notification notification
     */
    @Override
    public void update(Notification notification) {
        try{
            String query = "UPDATE public.notification SET title='"+notification.getTitle()+"', description='"+notification.getDescription()+"' WHERE id="+notification.getId()+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in update "+e);
        }
    }

    /**
     * Remove notification
     * @param notification notification
     */
    @Override
    public void remove(Notification notification) {
        try{
            String query = "DELETE FROM public.notification WHERE id= "+notification.getId()+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in remove "+e);
        }
    }

    /**
     * Request based on given choice
     * @param choice choice
     * @param object object
     */
    @Override
    public void request(int choice, Object object) {
        Notification notification = (Notification) object;
        switch(choice){
            case 1:
                add(notification);
                break;
            case 2:
                update(notification);
                break;
            case 3:
                remove(notification);
                break;
            default:
                break;
        }
    }
}
