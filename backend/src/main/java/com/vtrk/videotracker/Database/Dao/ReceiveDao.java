package com.vtrk.videotracker.Database.Dao;

import com.vtrk.videotracker.Database.Model.Notification;

import java.util.List;

public interface ReceiveDao {
    List<Notification> findByIdUser(int id_user);
    void add(int id_user, int id_notification);
    void remove(int id_user, int id_notification);
    boolean exists(int id_user, int id_notification);
}
