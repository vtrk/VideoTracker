package com.vtrk.videotracker.Database.Dao;

import com.vtrk.videotracker.Database.Model.Notification;

import java.util.List;

public interface NotificationDao {

    Notification findById(int id);
    List<Integer> findByIdUser(int id_user);
    void add(Notification notification);
    void update(Notification notification);
    void remove(Notification notification);

}
