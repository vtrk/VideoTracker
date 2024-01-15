package com.vtrk.videotracker.Database.Model;

public class Contains {
    private int id_list;
    private String id_content;
    private String state;

    public Contains(int id_list, String id_content, String state) {
        this.id_list = id_list;
        this.id_content = id_content;
        this.state = state;
    }

    public int getId_list() {
        return id_list;
    }

    public void setId_list(int id_list) {
        this.id_list = id_list;
    }

    public String getId_content() {
        return id_content;
    }

    public void setId_content(String id_content) {
        this.id_content = id_content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
