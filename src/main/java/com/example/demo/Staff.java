package com.example.demo;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;

public class Staff extends User{
    protected String Username;
    protected String Password;
    public Staff(String Username, String Password){
        //this constructor is used when the Admin logsin
        this.Username = Username;
        this.Password = Password;
    }
    public Staff(){
        //this constructor can be used to access methods in other areas
        //of the program after the admin has already logged in
        this.Username = "Username";
        this.Password = "Password";
    }

    public String getUsername(){
        return this.Username;
    }

    public String getPassword(){
        return this.Password;
    }
    //method with electionTypeLabel used in the actual program
    public boolean createElection(TextField NENameBox, Label NEErrorLabel, Label electionTypeLabel ) throws FileNotFoundException, SQLException {
        //establishes database connection
        Connection sqlconn = dbConnection.getConnection();
        //array of objects from Candidate class
        List<Candidate> candidateList = new ArrayList<>();
        //instantiate a single ovject from candidate class
        Candidate candidate;

        //array of objects from voter class
        List<Voter> voterList = new ArrayList<>();
        List<String> voter;

        List<String> electionDetailsList = new ArrayList<>();
        List<Election> electionList = new ArrayList<>();
        Election election;
        String sqlStatement = "SELECT * FROM Elections";
        ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
        if (rs.next()) {
            do  {//gets all Elections and puts them in a list
                election = new Election(rs.getString(1), rs.getString(2),
                        Boolean.parseBoolean(rs.getString(3)),
                        rs.getString(4));
                electionList.add(election);//Populates list with current election
            }while(rs.next());
            boolean uniqueName = true;
            for (int i = 0; i < electionList.size(); i++) {//iterates through list to see if the inputted name in unique
                if (electionList.get(i).getElectionName().equals(NENameBox.getText())) {
                    uniqueName = false;

                }
            }
            if (!NENameBox.getText().equals("")) {//checks it's not null
                if (uniqueName) {
                    for (int i = 0; i < electionList.size(); i++) {
                        if (!electionList.get(i).getElectionStatus()) {//if an election is currently running it ends it and selects a winner
                            sqlStatement = "SELECT * FROM Candidates";
                            rs = sqlconn.createStatement().executeQuery(sqlStatement);
                            while (rs.next()) {
                                candidate = new Candidate(rs.getString(1), rs.getString(2),
                                        (rs.getString(3)),
                                        rs.getInt(4), rs.getString(5));
                                if(candidate.getElectionName().equals(electionList.get(i).getElectionName())){
                                    candidateList.add(candidate);//Populates list with disks both game and music
                                }

                            }
                            int tieVotes = 0;
                            boolean tie = false;
                            if (!(candidateList.size() < 1)) {
                                Candidate winner = candidateList.get(0);

                                for (int j = 1; j < candidateList.size(); j++) {
                                    if (winner.compare(candidateList.get(j)) && candidateList.get(j).getNumVotes() > tieVotes) {
                                        winner = candidateList.get(j);
                                        tie = false;
                                    } else if (winner.getNumVotes() == candidateList.get(j).getNumVotes()) {
                                        tie = true;
                                    }

                                }
                                if (!tie) {
                                    electionList.get(i).endElection(winner.getName());
                                } else {
                                    electionList.get(i).endElection("inconclusive");
                                }
                            } else {
                                electionList.get(i).endElection("n/a");
                            }
                            sqlStatement = "UPDATE Elections SET Winner =(?), Status = (?) WHERE Name = (?)";
                            PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
                            ps.setString(1, electionList.get(i).getElectionWinner());
                            ps.setString(2, String.valueOf(electionList.get(i).getElectionStatus()));
                            ps.setString(3, electionList.get(i).getElectionName());
                            ps.execute();
                            ps.close();


                        }
                    }
                    //adds election to database
                    Election newElection = (new Election(NENameBox.getText(), electionTypeLabel.getText()));
                    sqlStatement = "INSERT INTO Elections (Name, Winner, Status, ElectionType) VALUES (?,?,?,?)";
                    PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
                    ps.setString(1, newElection.getElectionName());
                    ps.setString(2, newElection.getElectionWinner());
                    ps.setString(3, String.valueOf(newElection.getElectionStatus()));
                    ps.setString(4, newElection.getElectionType());
                    ps.execute();
                    ps.close();

                    //updates users table so people can vote again
                    sqlStatement = "UPDATE Voters SET hasVoted =(?)";
                    ps = sqlconn.prepareStatement(sqlStatement);
                    ps.setString(1, "false");
                    ps.execute();
                    ps.close();
                    sqlconn.close();

                    return true;


                }
                else{
                    NEErrorLabel.setText("Please use a unique election name"); //validation
                }


        }
            else{
                NEErrorLabel.setText("Please enter a name"); //validation
            }


        }
        else {
            election = new Election(NENameBox.getText(), electionTypeLabel.getText());
            election.Save();//adds new election to the database
        }
        return false;
    }

    public boolean endElection() throws FileNotFoundException, SQLException {
        //establishes connection
        Connection sqlconn = dbConnection.getConnection();
        //as above - array of objects from candidate class
        List<Candidate> candidateList = new ArrayList<>();
        //single instance of candidate class
        Candidate candidate;

        List<Voter> voterList = new ArrayList<>();
        List<String> voter;

        List<String> electionDetailsList = new ArrayList<>();
        List<Election> electionList = new ArrayList<>();
        Election election;
        boolean electionRunning = false;
        String sqlStatement = "SELECT * FROM Elections";
        ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);

        if (!rs.next()) {//checks if there are any elections in existence
            System.out.println("Error - no available election");
        } else {
            do {
                election = new Election(rs.getString(1), rs.getString(2),
                        Boolean.parseBoolean(rs.getString(3)),
                        rs.getString(4));
                electionList.add(election);//Populates list with current election
            }while (rs.next());
            for (int i = 0; i < electionList.size(); i++) {
                if (!electionList.get(i).getElectionStatus()) {//if an election is running ends it and gets the winner
                    sqlStatement = "SELECT * FROM Candidates";
                    rs = sqlconn.createStatement().executeQuery(sqlStatement);
                    while (rs.next()) {
                        candidate = new Candidate(rs.getString(1), rs.getString(2),
                                (rs.getString(3)),
                                rs.getInt(4), rs.getString(5));
                        if(candidate.getElectionName().equals(electionList.get(i).getElectionName())){
                            candidateList.add(candidate);//Populates list with current candidate
                        }
                    }
                    int tieVotes = 0;
                    boolean tie = false;
                    //below logic calculates winner
                    if (!(candidateList.size()<1)){
                        Candidate winner = candidateList.get(0);

                        for (int j = 1; j < candidateList.size() ; j++) {
                            if (winner.compare(candidateList.get(j)) && candidateList.get(j).getNumVotes()> tieVotes){
                                winner = candidateList.get(j);
                                tie = false;
                            }
                            else if(winner.getNumVotes()==candidateList.get(j).getNumVotes()){
                                tie = true;
                            }

                        }
                        if(!tie){
                            electionList.get(i).endElection(winner.getName());
                        }
                        else{
                            //if election is tied, results are inconclusive
                            electionList.get(i).endElection("inconclusive");
                        }
                    }
                    else {
                        //'n/a' occurs if election is ended without any candidates
                        electionList.get(i).endElection("n/a");
                    }
                    electionRunning = true;
                    sqlStatement = "UPDATE Elections SET Winner =(?), Status = (?) WHERE Name = (?)";
                    PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
                    ps.setString(1, electionList.get(i).getElectionWinner());
                    ps.setString(2, String.valueOf(electionList.get(i).getElectionStatus()));
                    ps.setString(3, electionList.get(i).getElectionName());
                    ps.execute();
                    ps.close();

                }

            }
            if(!electionRunning){
                return false;
            }

            //updates users table so people can vote again
            sqlStatement = "UPDATE Voters SET hasVoted =(?)";
            PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
            ps.setString(1, "false");
            ps.execute();
            ps.close();
            sqlconn.close();
            return true;
        }
        return false;
    }
    //method is only used in unit testing - it is the exact same as the method of the same name, it just doesn't give error messages
    //as they can only be handled when a java fx gui is being run
    public boolean createElection(String NENameBox, String electionTypeLabel) throws FileNotFoundException, SQLException {
        Connection sqlconn = dbConnection.getConnection();
        List<Candidate> candidateList = new ArrayList<>();
        Candidate candidate;

        List<Voter> voterList = new ArrayList<>();
        List<String> voter;

        List<String> electionDetailsList = new ArrayList<>();
        List<Election> electionList = new ArrayList<>();
        Election election;
        String sqlStatement = "SELECT * FROM Elections";
        ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
        if (rs.next()) {
            do  {
                election = new Election(rs.getString(1), rs.getString(2),
                        Boolean.parseBoolean(rs.getString(3)),
                        rs.getString(4));
                electionList.add(election);//Populates list with election details
            }while(rs.next());
            boolean uniqueName = true;
            for (int i = 0; i < electionList.size(); i++) {
                if (electionList.get(i).getElectionName().equals(NENameBox)) {
                    uniqueName = false;

                }
            }
            if (!NENameBox.equals("")) {//validation
                if (uniqueName) {
                    for (int i = 0; i < electionList.size(); i++) {
                        if (!electionList.get(i).getElectionStatus()) {//if an election is running it must be ended before a new one can be created
                            sqlStatement = "SELECT * FROM Candidates";
                            rs = sqlconn.createStatement().executeQuery(sqlStatement);
                            while (rs.next()) {
                                candidate = new Candidate(rs.getString(1), rs.getString(2),
                                        (rs.getString(3)),
                                        rs.getInt(4), rs.getString(5));
                                if(candidate.getElectionName().equals(electionList.get(i).getElectionName())){
                                    candidateList.add(candidate);//Populates list with current candidate
                                }

                            }
                            int tieVotes = 0;
                            boolean tie = false;
                            if (!(candidateList.size() < 1)) {
                                Candidate winner = candidateList.get(0);

                                for (int j = 1; j < candidateList.size(); j++) {
                                    if (winner.compare(candidateList.get(j)) && candidateList.get(j).getNumVotes() > tieVotes) {
                                        winner = candidateList.get(j);
                                        tie = false;
                                    } else if (winner.getNumVotes() == candidateList.get(j).getNumVotes()) {
                                        tie = true;
                                    }

                                }
                                if (!tie) {
                                    electionList.get(i).endElection(winner.getName());
                                } else {
                                    electionList.get(i).endElection("inconclusive");
                                }
                            } else {
                                electionList.get(i).endElection("n/a");
                            }
                            //sets winner of the election
                            sqlStatement = "UPDATE Elections SET Winner =(?), Status = (?) WHERE Name = (?)";
                            PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
                            ps.setString(1, electionList.get(i).getElectionWinner());
                            ps.setString(2, String.valueOf(electionList.get(i).getElectionStatus()));
                            ps.setString(3, electionList.get(i).getElectionName());
                            ps.execute();
                            ps.close();


                        }
                    }
                    //adds election to database
                    Election newElection = (new Election(NENameBox, electionTypeLabel));
                    sqlStatement = "INSERT INTO Elections (Name, Winner, Status, ElectionType) VALUES (?,?,?,?)";
                    PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
                    ps.setString(1, newElection.getElectionName());
                    ps.setString(2, newElection.getElectionWinner());
                    ps.setString(3, String.valueOf(newElection.getElectionStatus()));
                    ps.setString(4, newElection.getElectionType());
                    ps.execute();
                    ps.close();

                    //updates users table so people can vote again
                    sqlStatement = "UPDATE Voters SET hasVoted =(?)";
                    ps = sqlconn.prepareStatement(sqlStatement);
                    ps.setString(1, "false");
                    ps.execute();
                    ps.close();
                    sqlconn.close();

                    return true;


                }
                else{
                    return false;
                }


            }
            else{
                return false;
            }


        }
        else {//if there are no elections in existence it immediately creates the election
            election = new Election(NENameBox, electionTypeLabel);
            election.Save();
        }
        return false;
    }

    public boolean candidateCreationLogic(String ACNameBox,String ACDescriptionBox,String ACIDBox, Label ACErrorLabel) throws SQLException {
        boolean uniqueCandidateID = true;
        //database connection
        Connection sqlconn = dbConnection.getConnection();
        List<Candidate> candidateList = new ArrayList<>();
        Candidate candidate;

        if(!ACNameBox.equals("") && !ACDescriptionBox.equals("") && !ACIDBox.equals("")) {//checks input isn't null
            String sqlStatement = "SELECT * FROM Candidates";
            ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
            while (rs.next()) {//gets list of all candidates
                candidate = new Candidate(rs.getString(1), rs.getString(2),
                        (rs.getString(3)),
                        rs.getInt(4), rs.getString(5));
                candidateList.add(candidate);//Populates list with disks both game and music
            }
            for (int i = 0; i < candidateList.size(); i++) {
                if(candidateList.get(i).getCandidateID().equals(ACIDBox)){//tests if the user inputted candidate ID is Unique
                    uniqueCandidateID = false;
                    ACErrorLabel.setText("ID is not unique");
                }

            }
            if(uniqueCandidateID) {

                sqlStatement = "SELECT * From Elections WHERE Status = (?)";
                PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
                ps.setString(1, "false");
                rs = ps.executeQuery();
                //checks which election is running

                Candidate currentCandidate = new Candidate(ACNameBox, ACDescriptionBox, ACIDBox, 0,
                        rs.getString(1));//assigns candidate to the current running election
                ps.close();
                currentCandidate.Save();
                return true;


            }
        } else{ACErrorLabel.setText("Please ensure all fields are entered");}
        return false;

    }

    //method is only used in unit testing it is the exact same as the method of the same name it just doesn't give error messages
    //as they can only be handled when a java fx gui is being run
    public boolean candidateCreationLogic(String ACNameBox,String ACDescriptionBox,String ACIDBox) throws SQLException {//version of method without error label functionality(Used only for unit Tests)
        boolean uniqueCandidateID = true;
        Connection sqlconn = dbConnection.getConnection();
        List<Candidate> candidateList = new ArrayList<>();
        Candidate candidate;

        if(!ACNameBox.equals("") && !ACDescriptionBox.equals("") && !ACIDBox.equals("")) {
            String sqlStatement = "SELECT * FROM Candidates";
            ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
            while (rs.next()) {
                candidate = new Candidate(rs.getString(1), rs.getString(2),
                        (rs.getString(3)),
                        rs.getInt(4), rs.getString(5));
                candidateList.add(candidate);//Populates list with disks both game and music
            }
            for (int i = 0; i < candidateList.size(); i++) {
                if(candidateList.get(i).getCandidateID().equals(ACIDBox)){
                    uniqueCandidateID = false;
                    return false;
                }

            }
            if(uniqueCandidateID) {
                sqlStatement = "SELECT * From Elections WHERE Status = (?)";
                PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
                ps.setString(1, "false");
                rs = ps.executeQuery();
                Candidate currentCandidate = new Candidate(ACNameBox, ACDescriptionBox, ACIDBox, 0,
                        rs.getString(1));
                ps.close();
                currentCandidate.Save();
                return true;


            }
        }
        return false;

    }
}
