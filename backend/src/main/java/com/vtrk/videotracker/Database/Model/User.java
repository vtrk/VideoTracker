package com.vtrk.videotracker.Database.Model;

public class User {
    private int id;
    private String email;
    private String username;
    private String password;
    private boolean is_admin;
    private boolean is_banned;

    public User(int id, String email, String username, String password, boolean is_admin) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.is_admin = is_admin;
    }

    public User(int id, String email, String username, String password, boolean is_admin, boolean is_banned) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.is_admin = is_admin;
        this.is_banned = is_banned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }
    public boolean isBanned() {return is_banned;}
}
