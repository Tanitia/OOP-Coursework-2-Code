package com.example.demo;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Candidate {
    private String candidateName;

    private String candidateDescription;

    private String candidateID;

    private Integer numVotes;

    private String electionName;

    public Candidate(String candidateName, String candidateDescription, String candidateID, Integer numVotes, String electionName) {
        //constructor
        this.candidateName = candidateName;
        this.candidateDescription = candidateDescription;
        this.candidateID = candidateID;
        this.numVotes = numVotes;
        this.electionName = electionName;

}
public void Increment() throws SQLException, FileNotFoundException {
        this.numVotes++;
        Update();//saves the state of the candidate class
    Connection sqlconn = dbConnection.getConnection();
    String sqlStatement = "SELECT * FROM Elections";

    Election election = null;

    //read all elections in database to find the one that is running
    ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
    while (rs.next()) {
        if(rs.getString(3).equals("false")){
            election = new Election(rs.getString(1), rs.getString(2),
                    Boolean.parseBoolean(rs.getString(3)),
                    rs.getString(4));
        }
    }
    //if the election that is running is a First Past The Post type than checks if they have passed
    //the hard coded threshold than it automatically ends the election
    if(election.getElectionType().equals("First Past The Post") && this.numVotes == 10){
        //creates an admin object to call endElection method from staff
        Staff admin = new Staff("Admin", "Admin");
        admin.endElection();
    }

}
    public boolean Save() throws SQLException {
        //when adding a new candidate, it saves it to the database
        Connection sqlconn = dbConnection.getConnection();

        String sqlStatement = "INSERT INTO Candidates (Name, " +
                "Description, ID, numVotes, Election) VALUES (?,?,?,?,?)";
        PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
        ps.setString(1, this.candidateName);
        ps.setString(2, this.candidateDescription);
        ps.setString(3, this.candidateID);
        ps.setString(4, String.valueOf(this.numVotes));
        ps.setString(5, this.electionName);
        ps.execute();
        ps.close();
        sqlconn.close();
        return true;
    }
    public boolean Update() {
        try {
            //when a candidate gets a vote, updates the candidate's database entry
            Connection sqlconn = dbConnection.getConnection();

            String sqlStatement = "UPDATE Candidates SET numVotes = (?) WHERE ID = (?)";
            PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);

            ps.setString(1, String.valueOf(this.numVotes));
            ps.setString(2, this.candidateID);
            ps.execute();
            ps.close();
            sqlconn.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
public String getName(){
        return this.candidateName;
};

    public String getCandidateID(){
        System.out.println(this.candidateID);
        return this.candidateID;

    }

    public int getNumVotes(){
        return this.numVotes;
    }
    public String getElectionName(){
        return this.electionName;
    }
    //comparator
    public Boolean compare(Candidate nextCandidate){
        return this.numVotes< nextCandidate.getNumVotes();
    }
}

