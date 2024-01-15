package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.ReceiveDao;
import com.vtrk.videotracker.Database.Model.Notification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReceiveDaoPostgres implements ReceiveDao {

    Connection connection = null;
    public ReceiveDaoPostgres(Connection connection){ this.connection = connection; }
    @Override
    public List<Notification> findByIdUser(int id_user) {
        List<Notification> receive = new ArrayList<Notification>();
        try {
            String query = "SELECT * FROM receive WHERE id_user = '"+id_user+"';";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                Notification notification = new NotificationDaoPostgres(connection).findById(rs.getInt("id_notification"));
                receive.add(notification);
            }
        } catch (SQLException e) {
            System.out.println("Error in findAll"+e);
        }
        return receive;
    }

    @Override
    public void add(int id_user, int id_notification) {

    }

    @Override
    public void update(int id_user, int id_notification) {

    }

    @Override
    public void remove(int id_user, int id_notification) {

    }
}
