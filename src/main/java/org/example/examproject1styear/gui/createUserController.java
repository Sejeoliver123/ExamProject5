package org.example.examproject1styear.gui;

import org.example.examproject1styear.dal.DB.MyDatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;


public class createUserController {



    @FXML
    private Button btnCreateUser;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtName;


    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    public void initialize() {
        cmbRole.getItems().addAll("Admin", "User");
        cmbRole.setValue("User");
    }

    @FXML
    private void handlecancel(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/examproject1styear/Admindashboard-View.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("AdminDashboard");
        stage.show();
    }


    @FXML
    public void handleCreateUser(ActionEvent event) throws IOException {
        // Henter data fra GUI
        String name = txtName.getText();
        String role = cmbRole.getValue();

// Genererer username og midlertidigt password
        String username = name.toLowerCase().replace(" ", ".");
        String password = "Temp1234";

// SQL til at indsætte en ny bruger i databasen
        String sql = "INSERT INTO [User] (Name, Role, Username, Password) VALUES (?, ?, ?, ?)";

        // Opretter forbindelse til databasen
        MyDatabaseConnector databaseConnector = new MyDatabaseConnector();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Indsætter værdierne i SQL-forespørgslen
            statement.setString(1, name);
            statement.setString(2, role);
            statement.setString(3, username);
            statement.setString(4, password);

            // Gemmer brugeren i databasen
            statement.executeUpdate();

            // Opretter en log i Logs-tabellen
            String logSql = "INSERT INTO Logs (Action, Username) VALUES (?, ?)";

            try (PreparedStatement logStatement = connection.prepareStatement(logSql)) {

                logStatement.setString(1, "Created User");
                logStatement.setString(2, username);

                logStatement.executeUpdate();
            }

            // Viser loginoplysninger til den oprettede bruger
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bruger oprettet");
            alert.setHeaderText("Bruger oprettet");
            alert.setContentText("Username: " + username + "\nPassword: " + password);
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/examproject1styear/Admindashboard-View.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Går tilbage til AdminDashboard efter oprettelse
            stage.setScene(new Scene(root));
            stage.setTitle("AdminDashboard");
            stage.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
