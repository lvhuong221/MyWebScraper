package com.lvhuong.myproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author LeVanHuong
 */
public class ConnectionManager {
    private static Connection conn;

    public static Connection getConnection(){
        String dbURL = "jdbc:mysql://localhost:3306/fgosimdb";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                conn = DriverManager.getConnection(dbURL, username, password);
                if (conn != null) {
                    System.out.println("Connected");
                    //return conn;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

}