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
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import static java.util.Arrays.asList;


public class ViewResultsController implements Initializable {

    //to show and receive info in GUI - mvc design pattern
    @FXML
    private ListView<String> VRElectionListview;

    @FXML
    private Label VRWinnerLabel;

    //for redirection
    private Stage stage;
    private Scene scene;
    private Parent root;
    String currentElection;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {//code runs whe page launches
        try{
            //calls method to view election result data
        viewResultsLogic();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean viewResultsLogic() throws SQLException {
        //establish connection to database using connection object
        Connection sqlconn = dbConnection.getConnection();
        String sqlStatement = "SELECT * FROM Elections";
        List<Election> electionList = new ArrayList<>();
        Election election;


        ResultSet rs = sqlconn.createStatement().executeQuery(sqlStatement);
        while (rs.next()) {//creates a list of all elections in database
            election = new Election(rs.getString(1), rs.getString(2),
                    Boolean.parseBoolean(rs.getString(3)),
                    rs.getString(4));
            electionList.add(election);
        }

        for (int i = 0; i < electionList.size(); i++) {
            VRElectionListview.getItems().add(electionList.get(i).getElectionName());//populates list view with data
        }

        //adds on-click functionality to the List View
        VRElectionListview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

                currentElection = VRElectionListview.getSelectionModel().getSelectedItem();//gets what election the user is clicking on
                for (int i = 0; i < electionList.size(); i++) {
                    if (currentElection.equals(electionList.get(i).getElectionName())){
                        //sets label to be the name of the winner of the selected election
                        VRWinnerLabel.setText(electionList.get(i).getElectionWinner());
                    }
                }


            }
        });
        return true;
    }

    public void VRBackButton(ActionEvent actionEvent) throws IOException {
        //redirection
        root = FXMLLoader.load(getClass().getResource("admin_portal.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
