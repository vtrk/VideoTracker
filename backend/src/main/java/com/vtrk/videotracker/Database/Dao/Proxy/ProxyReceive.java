package com.vtrk.videotracker.Database.Dao.Proxy;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.ReceiveDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Subject;

public class ProxyReceive implements Subject {

    private ReceiveDaoPostgres receive;

    @Override
    public void request(int choice, Object object) {
        if(receive == null){
            receive = (ReceiveDaoPostgres)  DBManager.getInstance().getReceiveDao();
        }
        receive.request(choice, object);
    }

}
