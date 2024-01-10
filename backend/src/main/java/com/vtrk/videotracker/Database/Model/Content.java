package com.vtrk.videotracker.Database.Model;

public class Content {
    private String id;
    private String title;
    private int duration;
    private int n_episode;
    private String link;

    public Content(String id, String title, int duration, int n_episode, String link) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.n_episode = n_episode;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getN_episode() {
        return n_episode;
    }

    public void setN_episode(int n_episode) {
        this.n_episode = n_episode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
