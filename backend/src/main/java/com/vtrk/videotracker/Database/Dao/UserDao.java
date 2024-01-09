package com.vtrk.videotracker.Database.Dao;
import com.vtrk.videotracker.Database.Model.User;

public interface UserDao {
    User findById(int id);
    User findByEmail(String email);
    void add(User user);
    void update(User user);
    void remove(User user);
}
