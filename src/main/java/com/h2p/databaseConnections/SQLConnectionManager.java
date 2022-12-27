package com.h2p.databaseConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by vodinhphuc on 27/12/2022
 */
public class SQLConnectionManager {
    static final String DB_URL = "jdbc:mysql://localhost/designPattern";
    static final String USER = "root";
    static final String PASS = "Dinhphuc2009.";
    Connection conn;
    public static ThreadLocal<SQLConnectionManager> INSTANCE;

    static {
        ThreadLocal<SQLConnectionManager> dm;
        try {
            dm = ThreadLocal.withInitial(() -> {
                try {
                    return new SQLConnectionManager();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            dm = null;
        }
        INSTANCE = dm;
    }

    public static SQLConnectionManager getInstance() {
        return INSTANCE.get();
    }

    private SQLConnectionManager() {
    }


    public void open() throws SQLException {
        try {
            if (this.conn == null) {
                this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
            }
        } catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            throw e;
        }
    }

    public void close() throws SQLException {
        try {
            if (this.conn != null)
                this.conn.close();
        } catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConn() {
        return conn;
    }
}
