package com.shiba.arthea.handlers;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by balex on 09.07.2017.
 */
class DatabaseHandler {
    private static DatabaseHandler ourInstance = new DatabaseHandler();

    public static DatabaseHandler getInstance() {
        return ourInstance;
    }

    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASS;

    ////Initializeaza baza de date
    private DatabaseHandler() {
        DB_URL = "jdbc:mysql://db4free.net:3306/artheadb";
        DB_USER = "artheadbadmin";
        DB_PASS = "e02d8e";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            //TODO
            Log.d("tag", "Driver class loaded succesfully");
        } catch (ClassNotFoundException e) {
            //TODO
            Log.d("tag", "Driver class loading failed");
            e.printStackTrace();
        }
    }

    private ArrayList<String> get(String tableName) {
        //TODO
        Log.d("tag", "DatabaseHandler.get(String) called");
        ArrayList<String> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = connection.prepareStatement("SELECT content FROM " + tableName);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<String> getSubjects() {
        return get("Subjects");
    }

    private ArrayList<String> getAttributes() {
        return get("Attributes");
    }

    private ArrayList<String> getPredicates() {
        return get("Predicates");
    }
    ////
    public List<String>[] retrieveDatabaseContents(){
        return new List[]{getSubjects(), getAttributes(), getPredicates()};
    }

    public boolean isOnline() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}