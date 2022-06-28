package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;


public class UserSignUpController {

    //initialises for redirection

    private Stage stage;
    private Scene scene;
    private Parent root;

    //initialises to get and give info to GUI
    @FXML
    private TextField USUNameBox;
    @FXML
    private TextField USUAddressBox;

    @FXML
    private TextField USUIDBox;

    @FXML
    private TextField USUPasswordBox;

    //initialises to handle database queries later on
    String sqlStatement = "";

    public void confirmSignup(ActionEvent actionEvent) throws IOException, SQLException {
        boolean success = voterSignup();
        if (success) {
            //redirects to correct screen on success
            root = FXMLLoader.load(getClass().getResource("user_login.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public boolean voterSignup() throws FileNotFoundException, SQLException {
        Connection sqlconn = dbConnection.getConnection();
        if (!USUNameBox.getText().equals("") && !USUAddressBox.getText().equals("")
                && !USUIDBox.getText().equals("") && !USUPasswordBox.getText().equals("")) {//makes sure uner input isn't null
            List<Voter> userList = new ArrayList<>();
            Voter user;
            sqlStatement = "SELECT * FROM Voters";
            ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
            while (rs.next()) {//gets list of all voters
                user= new Voter(rs.getString(1),rs.getString(2), rs.getString(3),
                rs.getString(4), Boolean.parseBoolean(rs.getString(5)));
                userList.add(user);//Populates list with disks both game and music
            }
            boolean uniqueID = true;
            for (int i = 0; i < userList.size(); i++) {
                if (USUIDBox.getText().equals(userList.get(i).getVoterID())) {//tests if VoteId is unique
                    uniqueID = false;
                }
            }
            if (uniqueID) {//if VoterID is unique than it creates and saves the user
                Voter currentVoter = new Voter(USUNameBox.getText(), USUAddressBox.getText(), USUIDBox.getText(), USUPasswordBox.getText());
                currentVoter.Save();
                return true;
            }
        }
        return false;

    }

    public void USUGoBack(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("voting_portal_landing.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
