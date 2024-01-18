package com.vtrk.videotracker.Database.Model;

public class Contains {
    private int id_list;
    private Content content;
    private String state;

    public Contains(int id_list, Content content, String state) {
        this.id_list = id_list;
        this.content = content;
        this.state = state;
    }

    public int getId_list() {
        return id_list;
    }

    public void setId_list(int id_list) {
        this.id_list = id_list;
    }

    public Content getContent() {return content;}

    public void setContent(Content content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
