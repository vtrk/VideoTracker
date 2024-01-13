package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.NotificationDao;
import com.vtrk.videotracker.Database.Model.Notification;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.Connection;
import java.util.List;

public class NotificationDaoPostgres implements NotificationDao {
    Connection connection = null;

    public NotificationDaoPostgres(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Notification> findById(int id) {
        return null;
    }

    @Override
    public List<Notification> findByIdUser(int id_user) {
        return null;
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
