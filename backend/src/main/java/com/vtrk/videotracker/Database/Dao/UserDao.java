package com.vtrk.videotracker.Database.Dao;
import com.vtrk.videotracker.Database.Model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
    User findById(int id);
    User findByEmail(String email, String password);
    boolean getNotificationByEmail(int id);
    List<User> findWhoWantsNotificationByEmail();
    void add(User user);
    void update(User user);
    void updateFromSettings(int id, String credential, int choice);
    void remove(int id);
    boolean emailInUse(String email);
    boolean exists(int id);
    void ban(int id);
    boolean isBanned(int id);
    User findByEmailOnly(String email);
}
