package com.vtrk.videotracker.Database.Dao.Postgres;

import com.vtrk.videotracker.Database.Dao.ContentDao;
import com.vtrk.videotracker.Database.Model.Content;

import java.sql.Connection;

public class ContentDaoPostgres implements ContentDao{

    Connection connection = null;

    public ContentDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Content findById(String id) {
        return null;
    }

    @Override
    public void add(Content content) {

    }

    @Override
    public void update(Content content) {

    }

    @Override
    public void remove(Content content) {

    }
}
