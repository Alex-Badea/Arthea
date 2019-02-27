package com.shiba.arthea.handlers;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
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

    ////Initializeaza baza de date
    private DatabaseHandler() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //TODO
            Log.d("tag", "Driver class loaded succesfully");
        } catch (ClassNotFoundException e) {
            //TODO
            Log.d("tag", "Driver class loading failed");
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> get(String dbName, String dbUrl, String dbUser, String dbPass, String tableName) {
        //TODO
        Log.d("tag", "DatabaseHandler.get(String,String,String,String,String) called");
        ArrayList<String> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + dbUrl, dbUser, dbPass);
             PreparedStatement ps = connection.prepareStatement("SELECT content FROM " + dbName + "." + tableName);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            Log.d("tag", "Get from DB failed");
            throw new RuntimeException(e);
        }
        return list;
    }

    private ArrayList<String> getSubjects(String dbName, String dbUrl, String dbUser, String dbPass) {
        return get(dbName, dbUrl, dbUser, dbPass, "Subject");
    }

    private ArrayList<String> getAttributes(String dbName, String dbUrl, String dbUser, String dbPass) {
        return get(dbName, dbUrl, dbUser, dbPass, "Attribute");
    }

    private ArrayList<String> getPredicates(String dbName, String dbUrl, String dbUser, String dbPass) {
        return get(dbName, dbUrl, dbUser, dbPass, "Predicate");
    }    ////
    public List<String>[] retrieveDatabaseContents(Context context){
        HashMap<String, String> dbInfo = getDbCredentialsFromSensitiveData(context);
        return new List[]{
                getSubjects(dbInfo.get("dbName"), dbInfo.get("dbUrl"), dbInfo.get("dbUser"), dbInfo.get("dbPass")),
                getAttributes(dbInfo.get("dbName"), dbInfo.get("dbUrl"), dbInfo.get("dbUser"), dbInfo.get("dbPass")),
                getPredicates(dbInfo.get("dbName"), dbInfo.get("dbUrl"), dbInfo.get("dbUser"), dbInfo.get("dbPass"))};
    }

    public boolean isOnline(Context context) {
        HashMap<String, String> dbInfo = getDbCredentialsFromSensitiveData(context);
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + dbInfo.get("dbUrl"), dbInfo.get("dbUser"), dbInfo.get("dbPass"))) {
            return true;
        }
        catch (Exception e) {
            Log.d("tag", "DB not online");
            throw new RuntimeException(e);
            //return false;
        }
    }

    private HashMap<String, String> getDbCredentialsFromSensitiveData(Context context) {
        HashMap<String, String> dbInfo = new HashMap<>();
        try {
            String content = new Scanner(context.getApplicationContext().getAssets().
                    open("SENSITIVE_DATA.txt")).useDelimiter("\\Z").next();
            JSONObject reader = new JSONObject(content);
            dbInfo.put("dbName", reader.getString("dbName"));
            dbInfo.put("dbUrl", reader.getString("dbUrl"));
            dbInfo.put("dbUser", reader.getString("dbUser"));
            dbInfo.put("dbPass", reader.getString("dbPass"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dbInfo;
    }
}