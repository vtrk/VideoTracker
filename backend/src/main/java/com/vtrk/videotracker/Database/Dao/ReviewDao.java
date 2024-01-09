package com.vtrk.videotracker.Database.Dao;
import com.vtrk.videotracker.Database.Model.Review;
import com.vtrk.videotracker.Database.Model.User;

import java.util.List;

public interface ReviewDao {

    List<Review> findById(int id);
    List<Review> findByUser(User user);
    void add(Review review);
    void update(Review review);
    void remove(Review review);

}
