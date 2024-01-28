package com.vtrk.videotracker.Database.Dao;

import com.vtrk.videotracker.Database.Model.Notification;

public interface NotificationDao {

    Notification findById(int id);
    void add(Notification notification);
    void update(Notification notification);
    void remove(Notification notification);

}
