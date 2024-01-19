package com.vtrk.videotracker.Database.Dao.Proxy;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.ContentDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Subject;

public class ProxyContent implements Subject {
    private ContentDaoPostgres content;

    @Override
    public void request(int choice, Object object) {
        if(content == null){
            content = (ContentDaoPostgres)  DBManager.getInstance().getContentDao();
        }
        content.request(choice, object);
    }
}
