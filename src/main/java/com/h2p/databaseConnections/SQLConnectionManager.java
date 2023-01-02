package com.h2p.databaseConnections;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class SQLConnectionManager {

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

    private SQLConnectionManager() {}

    public void open() {
        try {
            if (this.conn == null) {
                Properties props = new Properties();
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                InputStream stream = loader.getResourceAsStream("config.properties");
                props.load(stream);
                String DB_URL = props.getProperty("DB_URL");
                String DB_USER = props.getProperty("DB_USER");
                String DB_PASS = props.getProperty("DB_PASS");
                this.conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            }
        } catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (this.conn != null)
                this.conn.close();
        } catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }
}
