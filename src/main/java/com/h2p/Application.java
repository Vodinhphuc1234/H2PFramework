package com.h2p;

import com.h2p.databaseAccessManagement.H2PDeleteQuery;
import com.h2p.databaseAccessManagement.H2PSelectQuery;
import com.h2p.databaseAccessManagement.IH2PSelectQuery;
import com.h2p.databaseAccessManagement.IH2PUpsertdelQuery;
import com.h2p.databaseConnections.SQLConnectionManager;
import com.h2p.models.Class;

import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {
        SQLConnectionManager.getInstance().open();
        Class aClass = new Class();
        aClass.setClassName("Bao thu");
        aClass.setClassCode("bt");
        aClass.setClassId(3);
        H2PSelectQuery<Class> selectQuery = new H2PSelectQuery<>(Class.class);
        selectQuery.selectBuilder.where().logic("class_id>2").done();
        System.out.println(selectQuery.select(true));
        SQLConnectionManager.getInstance().close();
    }
}
