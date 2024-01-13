package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.UserListDao;
import com.vtrk.videotracker.Database.Model.Content;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.Connection;
import java.util.List;

public class UserListDaoPostgres implements UserListDao {
    Connection connection = null;

    public UserListDaoPostgres(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Content> findByIdUser(int id_user) {
        return null;
    }

    @Override
    public UserListDao findById(int id) {
        return null;
    }
}
