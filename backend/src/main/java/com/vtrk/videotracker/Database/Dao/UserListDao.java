package com.vtrk.videotracker.Database.Dao;


import com.vtrk.videotracker.Database.Model.Content;
import com.vtrk.videotracker.Database.Model.User;
import com.vtrk.videotracker.Database.Model.UserList;

import java.util.List;

public interface UserListDao {
    UserList findByIdUser(int user);
    UserList findById(int id);
}
