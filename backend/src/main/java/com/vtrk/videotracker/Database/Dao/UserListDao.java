package com.vtrk.videotracker.Database.Dao;


import com.vtrk.videotracker.Database.Model.Content;
import com.vtrk.videotracker.Database.Model.User;
import java.util.List;

public interface UserListDao {
    List<Content> findAllByUser(User user);
    UserListDao findById(int id);
}
