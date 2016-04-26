package com.dk.thread.threadlocal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author dk
 * @date 2016/4/26
 */
public class ThreadLocalTest {

    private static ThreadLocal<Connection> connectionHandler = new ThreadLocal<Connection>() {
        public Connection initialValue(){
            try {
                return DriverManager.getConnection("DB_URL");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    public static Connection getConnection(){
        return connectionHandler.get();
    }
}


