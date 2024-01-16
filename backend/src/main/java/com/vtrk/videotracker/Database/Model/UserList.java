package com.vtrk.videotracker.Database.Model;

public class UserList {

    private int id;

    private int id_user;

    public UserList( int id, int user ){
        this.id = id;
        this.id_user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return id_user;
    }

    public void setIdUser(int id_user) {
        this.id_user = id_user;
    }
}
