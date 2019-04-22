package com.lvhuong.myproject;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class InsertData {
    // this function insert data into s_summoning_card table
    // this is a BAD PRACTICE of batch insert
    // better aproach down bellow
    public void insertCardTable() {
        String query ="";
        File folder = new File("E:\\SpringProject\\Fgosimulator\\src\\main\\resources\\static\\images\\s_summoning_cards");
        Connection conn;
        Statement stmt;
        ResultSet rs;
        try{
            conn = ConnectionManager.getConnection();
            query = "INSERT INTO s_summoning_cards VALUES(?,?)";
            PreparedStatement preparedStatment = conn.prepareStatement(query);
            for (final File fileEntry : folder.listFiles()) {
                String fileName = fileEntry.getName();
                // since the sorting for file is different, I need to get the number from file name
                // instead of have an INC variable
                preparedStatment.setString(2,fileName.substring(13,fileName.indexOf('.')));
                preparedStatment.setString(1, "/images/s_summoning_cards/"+fileName);
                preparedStatment.addBatch();
            }
            preparedStatment.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // use addBatch() and executebatch() for large number of insert
    public void insertServantTable(ArrayList<String> nameList, ArrayList<Integer> starList){
        String query ="";
        Connection conn;
        try{
            conn = ConnectionManager.getConnection();
            query = "INSERT INTO servants VALUES(?,?,?)";
            PreparedStatement preparedStatment = conn.prepareStatement(query);
            for (int i =0;i<nameList.size();i++) {
                preparedStatment.setString(1,(i+1)+"");
                preparedStatment.setString(2, nameList.get(i));
                preparedStatment.setString(3, starList.get(i)+"");
                preparedStatment.addBatch();
            }//If the batch is too big, routinely execute after x number of statements
            preparedStatment.executeBatch();
            preparedStatment.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
