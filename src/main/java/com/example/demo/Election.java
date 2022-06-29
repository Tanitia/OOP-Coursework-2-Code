package com.example.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Election {

    //all attributes private, but accessible through public methods
    //this is encapsulation to improve the security of the application and data flow
    private String electionName;

    private String electionWinner;

    private boolean electionStatus;

    private String electionType;
    //constructor used when creating a new election
    public Election(String electionName, String electionType){
        this.electionName = electionName;
        this.electionWinner = "null";
        this.electionStatus = false;
        this.electionType = electionType;
    }
    //constructor used when creating an old election
    public Election(String electionName, String electionWinner, boolean electionStatus, String electionType){
        this.electionName = electionName;
        this.electionWinner = electionWinner;
        this.electionStatus = electionStatus;
        this.electionType = electionType;
    }

    public void endElection(String electionWinner){
        this.electionWinner = electionWinner;
        this.electionStatus= true;
    }

    //getters to access private attributes (encapsulation)
    public String getElectionName(){
        return this.electionName;
    }

    public String getElectionWinner(){
        return this.electionWinner;
    }

    public boolean getElectionStatus(){
        return this.electionStatus;
    }

    public String getElectionType() {return this.electionType;}

    //save method enables data to be saved to db

    public boolean Save() throws SQLException {
        //saves the election to the database
        //connection object establishes connection to db
        Connection sqlconn = dbConnection.getConnection();
        //query is prepared to insert self into elections table in db
        String sqlStatement = "INSERT INTO Elections (Name, Winner, Status, ElectionType) VALUES (?,?,?,?)";
        PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
        ps.setString(1, this.electionName);
        ps.setString(2, this.electionWinner);
        ps.setString(3, String.valueOf(this.electionStatus));
        ps.setString(4, this.electionType);
        ps.execute();
        ps.close();
        sqlconn.close();
        return true;
}
}
