package com.h2p;

import com.h2p.adapters.SQLAdapter;
import com.h2p.databaseAccessManagement.H2PSelectQuery;
import com.h2p.databaseAccessManagement.IH2PReadingQuery;
import com.h2p.databaseConnections.SQLConnectionManager;
import com.h2p.mappers.TableMapper;
import com.h2p.models.User;

public class Application {
    public static void main(String[] args) {
        SQLConnectionManager.getInstance().open();
        IH2PReadingQuery<User> readingQuery = new H2PSelectQuery<>(User.class, new SQLAdapter(new TableMapper()));
        System.out.println(readingQuery.select(true));
        SQLConnectionManager.getInstance().close();
    }
}
