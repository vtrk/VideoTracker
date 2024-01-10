package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.UserDao;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.Connection;

public class UserDaoPostgres implements UserDao {
    Connection connection = null;

    public UserDaoPostgres(Connection connection) {
        this.connection = connection;
    }
    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public void add(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void remove(User user) {

    }
}
