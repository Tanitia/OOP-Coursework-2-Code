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

public class VotingPortalLandingController {

    //GUI and redirection values
    @FXML
    private Label welcomeText;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void redirectUserLogin(ActionEvent actionEvent) throws IOException {
        //Page Redirect
        root = FXMLLoader.load(getClass().getResource("user_login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void redirectUserSignUp(ActionEvent actionEvent) throws IOException {
        //Page Redirect
        root = FXMLLoader.load(getClass().getResource("user_sign_up.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void redirectAdminLogin(ActionEvent actionEvent) throws IOException {
        //Page Redirect
        root = FXMLLoader.load(getClass().getResource("admin_log_in.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}