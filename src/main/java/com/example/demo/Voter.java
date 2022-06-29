package com.example.demo;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;

//class inherits from User
//inheritance allows for reusability, which can help reduce dev time
public class Voter extends User {

    //all private attributes - enables improved security and class to be self contained
    //via encapsulation
    private String VoterID;
    private String Password;
    private Boolean hasVoted;
    //numerous constructors enables solution robustness via polymorphism
    // constructor used when first creating a new voter
    public Voter(String Name, String Address, String VoterID, String Password) {
        this.Name = Name;
        this.Address = Address;
        this.VoterID = VoterID;
        this.Password = Password;
        this.hasVoted = false;
    }
    // constructor used when creating a voter from the database
    public Voter(String Name, String Address, String VoterID, String Password, Boolean hasVoted) {
        this.Name = Name;
        this.Address = Address;
        this.VoterID = VoterID;
        this.Password = Password;
        this.hasVoted = hasVoted;
    }

    //public getters and setters are able to access private attributes
    //encapsulation
    public boolean gethasVoted(){
        return this.hasVoted;
    }

    public void sethasVoted(){
        this.hasVoted = true;
    }

    public void sethasVotedFalse(){
        this.hasVoted = false;
    }

    public String getVoterUsername(){
        return this.Name;
    }

    public String getVoterID(){return this.VoterID;}

    public String getPassword(){return this.Password;}

    public boolean Save() throws SQLException {
        //saves a new user to the database
        Connection sqlconn = dbConnection.getConnection();

        String sqlStatement = "INSERT INTO Voters (Name, " +
                "Address, VoterID, Password, hasVoted) VALUES (?,?,?,?,?)";
        PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
        ps.setString(1, this.Name);
        ps.setString(2, this.Address);
        ps.setString(3, this.VoterID);
        ps.setString(4, this.Password);
        ps.setString(5, String.valueOf(this.hasVoted));
        ps.execute();
        ps.close();
        sqlconn.close();
        return true;
    }
    public boolean Update() throws SQLException {
        //updates a Voter when they have voted, so they can't vote twice
        Connection sqlconn = dbConnection.getConnection();

        String sqlStatement = "UPDATE Voters SET hasVoted = (?) WHERE VoterID = (?)";
        PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
        ps.setString(1, "true");
        ps.setString(2, this.VoterID);
        ps.execute();
        ps.close();
        sqlconn.close();
        return true;
    }
    public boolean Vote(String candidateID) throws FileNotFoundException, SQLException {
        //connects to database
        Connection sqlconn = dbConnection.getConnection();
        List<Candidate> candidateList = new ArrayList<>();
        Candidate candidate = null;

        String sqlStatement = "SELECT * FROM Candidates";


        ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
        while (rs.next()) {
            if(rs.getString(3).equals(candidateID)){//creates candidate object  that Voter is voting for
                candidate = new Candidate(rs.getString(1), rs.getString(2),
                        rs.getString(3),
                        Integer.parseInt(rs.getString(4)), rs.getString(5));
            }
        }


        if (!gethasVoted()) {//if the user hasn't voted
            sethasVoted();//prevents user from voting twice
            Update();//keeps database up to date
            candidate.Increment();//increments the candidates vote count


            return true;
                }
        return false;

        }




    }

