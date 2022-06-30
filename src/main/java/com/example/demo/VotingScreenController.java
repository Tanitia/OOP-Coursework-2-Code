package com.example.demo;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.stage.Stage;

import static java.util.Arrays.asList;

public class VotingScreenController implements Initializable{

    //for redirection

    private Stage stage;
    private Scene scene;
    private Parent root;

    //get and display data - mvc design pattern

    @FXML
    private ListView<String> votingCandidateListView;

    @FXML
    private Label selectCandidateLabel;

    private String voterID;

    List<Candidate> candidateList = new ArrayList<>();
    Candidate candidate;

    List<Voter> voterList = new ArrayList<>();
    Voter voter;
    String currentCandidate;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {//method runs on start of page
        try {
            //establishes db connection
            Connection sqlconn = dbConnection.getConnection();
            String sqlStatement = "SELECT * FROM Elections";
            Election election = null;
            List<Election>electionList = new ArrayList<>();
            ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
            while (rs.next()) {
                //gets all elections from database to compare later
                election = new Election(rs.getString(1), rs.getString(2),
                        Boolean.parseBoolean(rs.getString(3)), rs.getString(4));
                electionList.add(election);
            }
            for (int k = 0; k < electionList.size(); k++) {

                if (!electionList.get(k).getElectionStatus()) {//finds out which election is currently running
                    election = electionList.get(k);
                }
            }

            sqlStatement = "SELECT * FROM Candidates";


            rs = sqlconn.createStatement().executeQuery(sqlStatement);
            while (rs.next()) {
                if (rs.getString(5).equals(election.getElectionName())) {
                    //generates list of candidates of the current running election
                    candidate = new Candidate(rs.getString(1), rs.getString(2),
                            rs.getString(3),
                            Integer.parseInt(rs.getString(4)), rs.getString(5));
                    candidateList.add(candidate);
                }
            }


            for (int i = 0; i < candidateList.size(); i++) {
                votingCandidateListView.getItems().add(candidateList.get(i).getName());//populates list view with names of candidates
            }

            //adds on click functionality to the list view
            votingCandidateListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

                    currentCandidate = votingCandidateListView.getSelectionModel().getSelectedItem();

                    selectCandidateLabel.setText(currentCandidate);//saves candidate selected

                }
            });
        }catch (SQLException e) {
            System.out.println("No Election");
        }
    }

        public void setVoterID(String voterID){
        this.voterID = voterID;
    }
    public void confirmVote(ActionEvent actionEvent) throws IOException, SQLException {
        boolean success = confirmVoteLogic();
        if (success){
            //redirects on success
            root = FXMLLoader.load(getClass().getResource("voting_portal_landing.fxml"));
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    public boolean confirmVoteLogic() throws FileNotFoundException, SQLException {
        Voter currentVoter = null;
        //db connection
        Connection sqlconn = dbConnection.getConnection();
        String sqlStatement = "SELECT * FROM Voters";
        
        ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
        while (rs.next()) {
            if(rs.getString(3).equals(voterID)){//Loads Voter that the user logged into
                currentVoter = new Voter(rs.getString(1), rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),Boolean.parseBoolean(rs.getString(5)));
            }
        }
        
        for (int j = 0; j < candidateList.size(); j++) {
            if(selectCandidateLabel.getText().equals(candidateList.get(j).getName())){
                return currentVoter.Vote(candidateList.get(j).getCandidateID());//votes for the selected candidate
            }
            }
        return false;
        }



    public void VMGoBack(ActionEvent actionEvent) throws IOException {
        //redirection
        root = FXMLLoader.load(getClass().getResource("voting_portal_landing.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
