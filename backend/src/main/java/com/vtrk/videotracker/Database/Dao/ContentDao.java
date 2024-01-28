package com.vtrk.videotracker.Database.Dao;

import com.vtrk.videotracker.Database.Model.Content;

public interface ContentDao {

    Content findById(String id);
    void add(Content content);
    void update(Content content);
    void remove(Content content);
    boolean exists(String id);

}
