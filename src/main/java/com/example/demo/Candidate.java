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

    //encapsulation to create a self containing, more secure class
    //these private attributes can be accessed by the below public methods
    //such as setters/getters
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
        //increases number of votes
        this.numVotes++;
        Update();//saves the state of the candidate class
    //connection object used to establish connection to database
    Connection sqlconn = dbConnection.getConnection();
    //all from elections
    String sqlStatement = "SELECT * FROM Elections";

    //creates empty object from Election class
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
        //connection object establishes connection
        Connection sqlconn = dbConnection.getConnection();
        //begins to prepare query for candidate insertion to database
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
            //object establishes db connection
            Connection sqlconn = dbConnection.getConnection();
            //prepares query to update the candidate's own number of votes after they have been voted for
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

    //getter, enables encapsulation (improves security)
public String getName(){
        return this.candidateName;
};

    public String getCandidateID(){
        //getter, enables encapsulation
        return this.candidateID;

    }

    //getter for encapsulation

    public int getNumVotes(){
        return this.numVotes;
    }
    public String getElectionName(){
        return this.electionName;
    }
    //comparator

    //compare receives a candidate object as its parameter
    //this is an example of message passing
    //this enables objects to get each other to take action
    public Boolean compare(Candidate nextCandidate){
        return this.numVotes< nextCandidate.getNumVotes();
    }
}

