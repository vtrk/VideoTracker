package com.vtrk.videotracker.Database.Dao;

import com.vtrk.videotracker.Database.Model.Content;

import java.util.List;

public interface ContainsDao {
    List<Content> findContentInList(int id_list);
    void add(int id_list, String id_content);
    void update(int id_list, String id_content, String state);
    void remove(int id_list, String id_content);
}
