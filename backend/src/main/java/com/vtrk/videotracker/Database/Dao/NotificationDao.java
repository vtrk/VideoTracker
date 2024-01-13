package com.vtrk.videotracker.Database.Dao;
import com.vtrk.videotracker.Database.Model.Notification;
import com.vtrk.videotracker.Database.Model.User;

import java.util.List;

public interface NotificationDao {

    List<Notification> findById(int id);
    List<Notification> findByIdUser(int id_user);
    void add(Notification notification);
    void update(Notification notification);
    void remove(Notification notification);

}
