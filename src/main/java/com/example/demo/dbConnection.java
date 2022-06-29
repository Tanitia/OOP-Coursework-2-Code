package com.example.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//class defined to improve ease of db access
public class dbConnection {

    //locates db

    private static final String SqCon = "jdbc:sqlite:VotingDatabase.db";
    //connection object will be used to establish connection to db
    public static Connection getConnection() throws SQLException {
        //returns database connection
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(SqCon);
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return null;
    }
}
