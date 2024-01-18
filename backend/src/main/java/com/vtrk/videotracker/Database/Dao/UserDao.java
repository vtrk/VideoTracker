package com.vtrk.videotracker.Database.Dao;
import com.vtrk.videotracker.Database.Model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
    User findById(int id);
    User findByEmail(String email, String password);
    void add(User user);
    void update(User user);
    void updateFromSettings(int id, String credential, int choice);
    void remove(User user);
    boolean emailInUse(String email);
}
