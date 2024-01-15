package com.vtrk.videotracker.Database.Dao;

import com.vtrk.videotracker.Database.Model.UserList;

public interface UserListDao {
    UserList findByIdUser(int user);
    UserList findById(int id);
    void add(int id_user);
}
