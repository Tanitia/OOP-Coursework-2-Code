package com.example.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {

    private static final String SqCon = "jdbc:sqlite:VotingDatabase.db";

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
