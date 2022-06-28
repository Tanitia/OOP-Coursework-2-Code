package com.example.demo;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class VotingScreenControllerTest {

@Test
    public void Election() throws FileNotFoundException, SQLException {
    Voter tea =new Voter("tea","tea","tea","tea",false);
    Staff staff = new Staff();

    //Tests Highest Votes Wins election type
    assertTrue(staff.createElection("TestElection","Highest Votes Wins"));//can create election
    assertTrue(staff.candidateCreationLogic("1235","123","123"));//can create candidate
    assertTrue(staff.candidateCreationLogic("123","123","1234"));//can create candidate
    assertTrue(staff.candidateCreationLogic("123","123","12345"));//can create candidate
    assertTrue(tea.Vote("123"));//can user Vote
    assertFalse(staff.candidateCreationLogic("123s","123s","123"));//cannot create candidate with an already used ID
    assertTrue(staff.endElection());//can end election


    Connection sqlconn = dbConnection.getConnection();
    String sqlStatement = "SELECT * FROM Elections";
    List<Election> electionList = new ArrayList<>();
    Election election;


    ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
    while (rs.next()) {
        election = new Election(rs.getString(1), rs.getString(2),
                Boolean.parseBoolean(rs.getString(3)),
                rs.getString(4));
        electionList.add(election);
    }

    assertTrue(electionList.get(electionList.size()-1).getElectionWinner().equals("1235"));//has the correct candidate won
    assertFalse(staff.endElection());//cannot end an election if non is currently running

    //Cleans up Database to allow for repeat testing
    sqlStatement = "DELETE FROM Elections WHERE Name=(?)";
    PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
    ps.setString(1, "TestElection");
    ps.execute();
    ps.close();
    sqlStatement = "DELETE FROM Candidates WHERE ID=(?)";
    ps = sqlconn.prepareStatement(sqlStatement);
    ps.setString(1, "123");
    ps.execute();
    ps.close();
    sqlStatement = "DELETE FROM Candidates WHERE ID=(?)";
    ps = sqlconn.prepareStatement(sqlStatement);
    ps.setString(1, "1234");
    ps.execute();
    ps.close();
    sqlStatement = "DELETE FROM Candidates WHERE ID=(?)";
    ps = sqlconn.prepareStatement(sqlStatement);
    ps.setString(1, "12345");
    ps.execute();
    ps.close();


}
@Test
    public void electionWithMultipleVoters() throws FileNotFoundException, SQLException {
    Connection sqlconn = dbConnection.getConnection();
    Staff staff = new Staff();
    List<Voter> voterList = new ArrayList<>();
    Voter voter;
    int numOfVoters = 10;

    String sqlStatement = "SELECT * FROM Voters";
    ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
    while (rs.next()) {
        //gets all voters from database to compare later
        voter = new Voter(rs.getString(1), rs.getString(2), rs.getString(3),
                rs.getString(4), Boolean.parseBoolean(rs.getString(5)));
        voterList.add(voter);//Populates list with disks both game and music
    }
    //Tests First Past The Post election type where the first candidate to get 10 votes wins
    assertTrue(staff.createElection("TestElection","First Past The Post"));//can create election
    assertTrue(staff.candidateCreationLogic("jeff","123","123"));//can create candidate
    assertTrue(staff.candidateCreationLogic("beat","123","234"));//can create candidate
    assertTrue(staff.candidateCreationLogic("greet","123","345"));//can create candidate
    List<Voter> voters = new ArrayList<>();
    try{
        for (int i = 0; i < numOfVoters; i++) {
            voters.add(voterList.get(i));
        }
    }
    catch (ArrayIndexOutOfBoundsException e){
        for (int i = 0; i < numOfVoters; i++) {
            voters = new ArrayList<>();
            voters.add(new Voter("Name","Address",String.valueOf(i),"Password"));
        }
    }

    for (int i = 0; i < numOfVoters; i++) {
        voters.get(i).Vote("345");
    }
    //assertTrue(staff.endElection());//can end election


    sqlStatement = "SELECT * FROM Elections";
    List<Election> electionList = new ArrayList<>();
    Election election;


    rs = sqlconn.createStatement().executeQuery(sqlStatement);
    while (rs.next()) {
        election = new Election(rs.getString(1), rs.getString(2),
                Boolean.parseBoolean(rs.getString(3)),
                rs.getString(4));
        electionList.add(election);
    }

    assertTrue(electionList.get(electionList.size()-1).getElectionWinner().equals("greet"));//has the correct candidate won


    //Cleans up Database to allow for repeat testing
    sqlStatement = "DELETE FROM Elections WHERE Name=(?)";
    PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
    ps.setString(1, "TestElection");
    ps.execute();
    ps.close();
    sqlStatement = "DELETE FROM Candidates WHERE ID=(?)";
    ps = sqlconn.prepareStatement(sqlStatement);
    ps.setString(1, "123");
    ps.execute();
    ps.close();
    sqlStatement = "DELETE FROM Candidates WHERE ID=(?)";
    ps = sqlconn.prepareStatement(sqlStatement);
    ps.setString(1, "234");
    ps.execute();
    ps.close();
    sqlStatement = "DELETE FROM Candidates WHERE ID=(?)";
    ps = sqlconn.prepareStatement(sqlStatement);
    ps.setString(1, "345");
    ps.execute();
    ps.close();


    sqlconn.close();

}
}