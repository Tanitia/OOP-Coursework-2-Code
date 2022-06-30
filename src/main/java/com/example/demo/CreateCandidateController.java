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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;

public class CreateCandidateController {

    //initialises for GUI redirection
    private Stage stage;
    private Scene scene;
    private Parent root;

    //initialises to pass info between controller and GUI - mvc
    @FXML
    private TextField ACNameBox;
    @FXML
    private TextField ACDescriptionBox;

    @FXML
    private TextField ACIDBox;

    @FXML
            private Label ACErrorLabel;

    public void confirmCandidateCreate(ActionEvent actionEvent) throws IOException, SQLException {
        //creates staff object from class
        Staff staff = new Staff();
        //redirects if successful
        boolean success = staff.candidateCreationLogic(ACNameBox.getText(),ACDescriptionBox.getText(),ACIDBox.getText(),ACErrorLabel);
        if (success){
            root = FXMLLoader.load(getClass().getResource("admin_portal.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }

    }



    public void ACBackGo(ActionEvent actionEvent) throws IOException {
        //redirects
        root = FXMLLoader.load(getClass().getResource("admin_portal.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
