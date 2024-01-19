package com.vtrk.videotracker.Database.Dao.Proxy;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.ReviewDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Subject;

public class ProxyReview implements Subject {

    private ReviewDaoPostgres review;

    @Override
    public void request(int choice, Object object) {
        if(review == null){
            review = (ReviewDaoPostgres)  DBManager.getInstance().getReviewDao();
        }
        review.request(choice, object);
    }

}
