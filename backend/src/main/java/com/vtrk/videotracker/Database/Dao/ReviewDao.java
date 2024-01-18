package com.vtrk.videotracker.Database.Dao;

import com.vtrk.videotracker.Database.Model.Review;

import java.util.List;

public interface ReviewDao {

    Review findById(int id);
    List<Review> findByIdContent(String id_content);
    void add(Review review);
    void update(Review review);
    void remove(Review review);

    void removeAllReviewsOfAUser(int id_user);
    boolean exists(int id_user, String id_content);

}
