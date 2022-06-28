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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;

public class AdminLoginController {

    //initialises for SQL query

    String sqlStatement = "";
    //initialises for redirection
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private TextField ALINameBox;
    @FXML
    private PasswordField ALIPasswordBox;

    @FXML private Label ALIErrorLabel;


    public void adminLoginOK(ActionEvent actionEvent) throws IOException, SQLException {
        //redirects if successful
        boolean success = adminLoginLogic();
        if (success){
            root = FXMLLoader.load(getClass().getResource("admin_portal.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    public boolean adminLoginLogic() throws FileNotFoundException, SQLException {
        //establishes connection to database
        Connection sqlconn = dbConnection.getConnection();
        List<Staff> adminList = new ArrayList<>();
        Staff user;
        sqlStatement = "SELECT * FROM Admins";
        ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
        while (rs.next()) {
            user= new Staff(rs.getString(1),rs.getString(2));
            adminList.add(user);//Populates list staff(held in user)
        }


        for (int i = 0; i < adminList.size(); i++) {
            //iterates through list of admins checking to see if credentials are valid
            if (adminList.get(i).getUsername().equals(ALINameBox.getText()) && adminList.get(i).getPassword().equals(ALIPasswordBox.getText())) {
                return true;
            }
            else{
                ALIErrorLabel.setText("Incorrect credentials");//validation
            }
        }
        return false;
    }

    public void ALGoBack(ActionEvent actionEvent) throws IOException {
        //Page redirect
        root = FXMLLoader.load(getClass().getResource("voting_portal_landing.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
