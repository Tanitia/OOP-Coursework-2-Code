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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;

public class AdminDeleteUsersController {
    //initialises for redirection in GUI
    private Stage stage;
    private Scene scene;
    private Parent root;

    //initialises to use and output info in GUI - mvc design pattern
    @FXML
    private TextField DUUserIDBox;

    @FXML
    private Label DUErrorLabel;
    public void deleteUser(ActionEvent actionEvent) throws IOException, SQLException {
        //method handles enceptions
        //boolean used similarly, but not identically, to message pass;
        boolean success = deleteUserMethod();
        if(success) {
            //page redirect
            root = FXMLLoader.load(getClass().getResource("admin_portal.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    public boolean deleteUserMethod() throws FileNotFoundException, SQLException {
        //implements connection
        Connection sqlconn = dbConnection.getConnection();
        //list of voter objects
        List<Voter> userList = new ArrayList<>();
        //new voer object
        Voter user;

        String sqlStatement = "SELECT * FROM Voters";
        ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
        while (rs.next()) {//gets all Voters from database
            //gets all voters from database to compare later
            user = new Voter(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), Boolean.parseBoolean(rs.getString(5)));
            userList.add(user);//Populates list voters
        }

        if(userList.size()>1){//checks if any voters exist
            boolean validID = false;
            for (int i = 0; i < userList.size(); i++) {
                if (DUUserIDBox.getText().equals(userList.get(i).getVoterID())){
                    validID = true;
                    userList.remove(i);//removes the Voter from list if they exist

                }
            }
            if (validID == true){
                sqlStatement = "DELETE FROM Voters WHERE VoterID = (?)";//removes the Voter from the Database if they exist
                PreparedStatement ps = sqlconn.prepareStatement(sqlStatement);
                ps.setString(1, DUUserIDBox.getText());
                //closes connection for security
                ps.execute();
                ps.close();
                sqlconn.close();
                return true;
            }
            else {
                DUErrorLabel.setText("No user found with that ID");//validation
            }

        } else {
            DUErrorLabel.setText("No users currently exist");//validation
        }
        return false;
    }
    public void DUBack(ActionEvent actionEvent) throws IOException {
        //Page redirect
        root = FXMLLoader.load(getClass().getResource("admin_portal.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
