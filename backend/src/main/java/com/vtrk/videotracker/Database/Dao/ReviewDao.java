package com.vtrk.videotracker.Database.Dao;

import com.vtrk.videotracker.Database.Model.Review;

import java.util.List;

public interface ReviewDao {

    Review findById(int id);
    int findByIdUserAndContent(int id_user, String id_content);
    List<Review> findByIdContent(String id_content);
    void add(Review review);
    void update(Review review);
    void remove(int id);

    void removeAllReviewsOfAUser(int id_user);
    boolean exists(int id_user, String id_content);

}
