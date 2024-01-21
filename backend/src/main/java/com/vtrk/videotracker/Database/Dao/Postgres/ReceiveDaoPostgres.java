package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.ReceiveDao;
import com.vtrk.videotracker.Database.Dao.Subject;
import com.vtrk.videotracker.Database.Model.Notification;
import com.vtrk.videotracker.Database.Model.Receive;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReceiveDaoPostgres implements ReceiveDao, Subject {

    Connection connection = null;
    public ReceiveDaoPostgres(Connection connection){ this.connection = connection; }

    /**
     * Find all notifications received by a user
     * @param id_user id of the user
     * @return list of notifications
     */
    @Override
    public List<Notification> findByIdUser(int id_user) {
        List<Notification> receive = new ArrayList<Notification>();
        try {
            String query = "SELECT * FROM receive WHERE id_user = '"+id_user+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){

                Notification notification = DBManager.getInstance().getNotificationDao().findById(rs.getInt("id_notification"));
                receive.add(notification);
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll "+e);
        }
        return receive;
    }

    /**
     * Add a notification to a user
     * @param id_user id of the user
     * @param id_notification id of the notification
     */
    @Override
    public void add(int id_user, int id_notification) {
        try{
            String query = "INSERT INTO public.receive (id_user, id_notification) VALUES("+id_user+", "+id_notification+");";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in add "+e);
        }
    }

    /**
     * Remove a notification from a user
     * @param id_user id of the user
     * @param id_notification id of the notification
     */
    @Override
    public void remove(int id_user, int id_notification) {
        try{
            String query = "DELETE FROM public.receive WHERE id_user="+id_user+" AND id_notification="+id_notification+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in remove "+e);
        }
    }

    /**
     * Remove all notifications from a user
     * @param id_user id of the user
     */
    @Override
    public void removeAllForAUser(int id_user) {
        try{
            String query = "DELETE FROM public.receive WHERE id_user="+id_user+";";
            Statement st = connection.createStatement();
            st.executeQuery(query);
        }catch(SQLException e){
            System.out.println("Error in remove "+e);
        }
    }

    /**
     * Check if a notification is already received by a user
     * @param id_user id of the user
     * @param id_notification id of the notification
     * @return true if the notification is already received by the user, false otherwise
     */
    @Override
    public boolean exists(int id_user, int id_notification) {
        try{
            String query = "SELECT * FROM public.receive WHERE id_user="+id_user+" AND id_notification="+id_notification+";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(!rs.isBeforeFirst())
                return false;
        }catch(SQLException e){
            System.out.println("Error in exists "+e);
        }
        return true;
    }

    /**
     * Request based on the given choice
     * @param choice choice
     * @param object object
     */
    @Override
    public void request(int choice, Object object) {
        if(choice == 3){
            removeAllForAUser((int) object);
        }else{
            Receive receive = (Receive) object;
            switch(choice){
                case 1:
                    add(receive.getId_user(), receive.getId_notification());
                    break;
                case 2:
                    remove(receive.getId_user(), receive.getId_notification());
                    break;
                default:
                    break;
            }
        }
    }
}
