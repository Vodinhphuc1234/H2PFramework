package com.h2p;

import com.h2p.databaseAccessManagement.H2PSelectQuery;
import com.h2p.databaseAccessManagement.IH2PReadingQuery;
import com.h2p.databaseConnections.SQLConnectionManager;
import com.h2p.models.Class;

public class Application {
    public static void main(String[] args) {
        SQLConnectionManager.getInstance().open();
        Class aClass = new Class();
        aClass.setClassName("Bao thu");
        aClass.setClassCode("bt");
        aClass.setClassId(3);
        IH2PReadingQuery<Class> selectQuery = new H2PSelectQuery<>(Class.class);
        selectQuery.selectBuilder.where().logic("class_id>2").done();
        System.out.println(selectQuery.select(true));
        SQLConnectionManager.getInstance().close();
    }
}
