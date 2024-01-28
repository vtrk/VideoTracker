package com.vtrk.videotracker.Database.Dao;

import com.vtrk.videotracker.Database.Model.Contains;

import java.util.List;

public interface ContainsDao {
    List<Contains> findContentInList(int id_list);
    String countByState(int id_list, String state);
    void add(int id_list, String id_content, String state);
    void update(int id_list, String id_content, String state);
    void remove(int id_list, String id_content);
    void removeWholeList(int id_list);
    boolean exists(int id_list, String id_content);
}
