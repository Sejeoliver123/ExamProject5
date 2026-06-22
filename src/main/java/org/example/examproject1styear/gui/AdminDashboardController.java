package org.example.examproject1styear.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Button;

public class AdminDashboardController {
    @FXML
    private Button btnCreateUser;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnLogs;
    @FXML
    private Button btnscanning;

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/examproject1styear/login-view.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("WebLager!");

        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleCreateUser(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/examproject1styear/CreateUser.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("Create User");
        stage.setScene(new Scene(root));
        stage.show();
    }
}

