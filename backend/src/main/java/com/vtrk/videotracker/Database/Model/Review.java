package com.vtrk.videotracker.Database.Model;

public class Review {

    private int id;
    private int vote;
    private String user_comment;
    private User user;
    private Content content;

    public Review( int id, int vote, String user_comment, User user, Content content ){
        this.id = id;
        this.vote = vote;
        this.user_comment = user_comment;
        this.user = user;
        this.content = content;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
