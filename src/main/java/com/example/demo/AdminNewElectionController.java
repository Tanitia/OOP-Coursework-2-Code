package com.example.demo;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import static java.util.Arrays.asList;


public class AdminNewElectionController implements Initializable {

    //instantiates to give and receive info to and from GUI - mvc design pattern
    @FXML
    private TextField NENameBox;
@FXML
    private Label NEErrorLabel;

@FXML
    private ListView<String> electionTypeListView;

@FXML
    private Label electionTypeLabel;

//lists of candidates and voters created

    List<Candidate> candidateList = new ArrayList<>();
    List<String> candidate;

    List<Voter> voterList = new ArrayList<>();
    List<String> voter;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {//runs on page launch

        electionTypeListView.getItems().add("First Past The Post");//two types of election that currently exist
        electionTypeListView.getItems().add("Highest Votes Wins");


        //adds on click functionality to the list view
        electionTypeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

                String currentElectionType = electionTypeListView.getSelectionModel().getSelectedItem();

                electionTypeLabel.setText(currentElectionType);//when the list view is clicked saves what was clicked in the label

            }
        });
    }
    public void createElection(ActionEvent actionEvent) throws IOException, SQLException {
        //new instance from Staff class
        Staff admin = new Staff("Admin","Admin");
        boolean success = admin.createElection(NENameBox, NEErrorLabel, electionTypeLabel);
        if (success){
            //page redirect
            root = FXMLLoader.load(getClass().getResource("admin_portal.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void returnToAdminPortal(ActionEvent actionEvent) throws IOException {
        //page redirect
        root = FXMLLoader.load(getClass().getResource("admin_portal.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
