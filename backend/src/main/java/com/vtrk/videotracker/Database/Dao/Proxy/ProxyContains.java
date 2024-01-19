package com.vtrk.videotracker.Database.Dao.Proxy;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.ContainsDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Subject;

public class ProxyContains implements Subject {
    private ContainsDaoPostgres contains;

    @Override
    public void request(int choice, Object object) {
        if(contains == null){
            contains = (ContainsDaoPostgres)  DBManager.getInstance().getContainsDao();
        }
        contains.request(choice, object);
    }
}
