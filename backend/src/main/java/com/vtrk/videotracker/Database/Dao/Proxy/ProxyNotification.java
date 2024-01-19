package com.vtrk.videotracker.Database.Dao.Proxy;

import com.vtrk.videotracker.Database.DBManager;
import com.vtrk.videotracker.Database.Dao.Postgres.NotificationDaoPostgres;
import com.vtrk.videotracker.Database.Dao.Subject;

public class ProxyNotification implements Subject {

    private NotificationDaoPostgres notification;

    @Override
    public void request(int choice, Object object) {
        if(notification == null){
            notification = (NotificationDaoPostgres)  DBManager.getInstance().getNotificationDao();
        }
        notification.request(choice, object);
    }

}
