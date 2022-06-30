package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;


public class UserLoginController {

    String sqlStatement = "";

    static VotingScreenController VoterController ;
    private Stage stage;
    private Scene scene;
    private Parent root;

    //mvc
    @FXML
    private TextField ULIIDBox;

    @FXML
    private PasswordField ULIPasswordBox;

    @FXML
    private Label ULIErrorLabel;


    public void logInAction(ActionEvent actionEvent) throws IOException, SQLException {
        boolean success = confirmLogin();
        //nav on success
        if (success) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("voting_screen.fxml"));
            Parent root = loader.load();

            VoterController = loader.getController();
            VoterController.setVoterID(ULIIDBox.getText());

            Scene scene = new Scene(root);
            Stage dash = new Stage();
            dash.setScene(scene);
            dash.show();

            Stage stage = (Stage) ULIIDBox.getScene().getWindow();
            stage.close();

        }
    }
    public boolean confirmLogin() throws IOException, SQLException {
        //connection object establishes connection to db

        Connection sqlconn = dbConnection.getConnection();
        sqlStatement = "SELECT * FROM Elections";
        //creates object of Election class
        Election election;
        //list of election objects
        List<Election>electionList = new ArrayList<>();
        ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
            while (rs.next()) {
                //gets all elections  from database to compare later
                election = new Election(rs.getString(1), rs.getString(2),
                        Boolean.parseBoolean(rs.getString(3)), rs.getString(4));
                electionList.add(election);
            }
        for (int k = 0; k < electionList.size(); k++) {

        if(!electionList.get(k).getElectionStatus()){//checks if an election is currently running
        List<Voter> userList = new ArrayList<>();
        Voter user;
        //query to get all voter info from db
        sqlStatement = "SELECT * FROM Voters";
        rs = sqlconn.createStatement().executeQuery(sqlStatement);
        while (rs.next()) {
            //gets all voters from database to compare later
            user = new Voter(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), Boolean.parseBoolean(rs.getString(5)));
            userList.add(user);
        }


        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getVoterID().equals(ULIIDBox.getText())
                    && userList.get(i).getPassword().equals(ULIPasswordBox.getText())
                    && !userList.get(i).gethasVoted()) {//tests if the user credentials are correct

                return true;

            } else if (userList.get(i).getVoterID().equals(ULIIDBox.getText())
                    && userList.get(i).getPassword().equals(ULIPasswordBox.getText())
                    && userList.get(i).gethasVoted()) {//handles if the user has already voted
                ULIErrorLabel.setText("You have already voted");
                break;
            }
            else {//handles incorrect credentials
                ULIErrorLabel.setText("Incorrect username or password");
            }
        }
        return false;
        }



        }
        //users cannot login when no election is running - label shows this;
        ULIErrorLabel.setText("No Election Running");
        return false;
    }

    public void ULIBackButton(ActionEvent actionEvent) throws IOException {
        //redirection
        root = FXMLLoader.load(getClass().getResource("voting_portal_landing.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
