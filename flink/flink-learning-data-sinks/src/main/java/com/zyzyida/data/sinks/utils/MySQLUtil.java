package com.zyzyida.data.sinks.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Desc: 通过端口连接mysql数据库
 * Created by zhouyizhe on 2019-08-11
 */
public class MySQLUtil {
    public static Connection getConnection(String driver, String url, String user, String password) {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("mysql get connection has exception,msg=" + e.getMessage());
        }
        return con;
    }

}
