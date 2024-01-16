package com.vtrk.videotracker.Database.Model;

public class Review {

    private int id;
    private int vote;
    private String user_comment;
    private int id_user;
    private String id_content;

    public Review( int id, int vote, String user_comment, int id_user, String id_content ){
        this.id = id;
        this.vote = vote;
        this.user_comment = user_comment;
        this.id_user = id_user;
        this.id_content = id_content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getUserComment() {
        return user_comment;
    }

    public void setUserComment(String user_comment) {
        this.user_comment = user_comment;
    }

    public int getIdUser() {
        return this.id_user;
    }

    public void setIdUser(int id_user) {
        this.id_user = id_user;
    }

    public String getIdContent() {
        return this.id_content;
    }

    public void setIdContent(String id_content) {
        this.id_content = id_content;
    }
}
