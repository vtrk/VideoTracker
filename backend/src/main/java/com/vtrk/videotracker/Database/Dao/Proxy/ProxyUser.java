package com.vtrk.videotracker.Database.Dao.Proxy;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.UserDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Subject;

public class ProxyUser implements Subject {

    private UserDaoPostgres user;

    @Override
    public void request(int choice, Object object) {
        if(user == null){
            user = (UserDaoPostgres)  DBManager.getInstance().getUserDao();
        }
        user.request(choice, object);
    }

}
