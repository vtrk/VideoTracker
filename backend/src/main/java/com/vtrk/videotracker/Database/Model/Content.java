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


}
