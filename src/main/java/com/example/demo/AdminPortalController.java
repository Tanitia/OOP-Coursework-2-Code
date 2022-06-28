package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPortalController {
    //instantiates for GUI redirection
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void NewElectionGo(ActionEvent actionEvent) throws IOException {
        //page redirect
        root = FXMLLoader.load(getClass().getResource("admin_new_election.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void EndElectionGo(ActionEvent actionEvent) throws IOException {
        //page redirect
        root = FXMLLoader.load(getClass().getResource("end_election.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void ViewResultsGo(ActionEvent actionEvent) throws IOException {
        //page redirect
        root = FXMLLoader.load(getClass().getResource("view_results.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void redirectCreateCandidate(ActionEvent actionEvent) throws IOException {
        //page redirect
        root = FXMLLoader.load(getClass().getResource("create_candidate.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void redirectToVP(ActionEvent actionEvent) throws IOException {
        //page redirect
        root = FXMLLoader.load(getClass().getResource("voting_portal_landing.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void DeleteUsersGo(ActionEvent actionEvent) throws IOException {
        //page redirect
        root = FXMLLoader.load(getClass().getResource("admin_delete_users.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
