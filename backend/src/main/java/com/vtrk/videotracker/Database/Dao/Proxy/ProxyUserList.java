package com.vtrk.videotracker.Database.Dao.Proxy;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.UserListDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Subject;

public class ProxyUserList implements Subject {

    private UserListDaoPostgres userList;

    @Override
    public void request(int choice, Object object) {
        if(userList == null){
            userList = (UserListDaoPostgres)  DBManager.getInstance().getUserListDao();
        }
        userList.request(choice, object);
    }

}
