package com.h2p.databaseConnections;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Created by vodinhphuc on 27/12/2022
 */
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


    public void open() throws SQLException {
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
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
