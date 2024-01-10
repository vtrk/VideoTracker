package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.ReviewDao;
import com.vtrk.videotracker.Database.Model.Review;
import com.vtrk.videotracker.Database.Model.User;

import java.sql.Connection;
import java.util.List;

public class ReviewDaoPostgres implements ReviewDao {
    Connection connection = null;

    public ReviewDaoPostgres(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Review> findById(int id) {
        return null;
    }

    @Override
    public List<Review> findByUser(User user) {
        return null;
    }

    @Override
    public void add(Review review) {

    }

    @Override
    public void update(Review review) {

    }

    @Override
    public void remove(Review review) {

    }
}
