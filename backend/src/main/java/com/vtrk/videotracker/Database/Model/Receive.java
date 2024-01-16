package com.vtrk.videotracker.Database.Model;

public class Receive {

    private int id_user;
    private int id_notification;

    public Receive(int id_user, int id_notification) {
        this.id_user = id_user;
        this.id_notification = id_notification;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
    public int getId_notification() {
        return id_notification;
    }

    public void setId_notification(int id_notification) {
        this.id_notification = id_notification;
    }

}
