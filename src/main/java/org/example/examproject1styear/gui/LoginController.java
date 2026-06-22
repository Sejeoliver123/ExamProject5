package org.example.examproject1styear.gui;

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
import java.io.IOException;
import org.example.examproject1styear.dal.DB.MyDatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Label;

public class LoginController {


    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtpassword;
    @FXML
    private Label lblLoginError;




    @FXML
    private void handleLogin(ActionEvent event) throws IOException {

        // Henter brugernavn og adgangskode fra loginfelterne
        String username = txtUsername.getText();
        String password = txtpassword.getText();

        // SQL-forespørgsel som undersøger om brugernavn og adgangskode findes i databasen
        String sql = "SELECT * FROM [User] WHERE Username = ? AND Password = ?";

        // Opretter forbindelse til databasen
        MyDatabaseConnector databaseConnector = new MyDatabaseConnector();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Indsætter brugerens input i SQL-forespørgslen
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            // Kontrollerer om der blev fundet en bruger
            if (resultSet.next()) {
                // Henter brugerens rolle fra databasen
                String role = resultSet.getString("Role");

                // Sender administratorer til Admin Dashboard
                if (role.equalsIgnoreCase("admin")) {
                    openDashboard(event, "/org/example/examproject1styear/Admindashboard-View.fxml", "AdminDashboard");
                    // Alle andre brugere sendes til User Dashboard
                } else {
                    openDashboard(event, "/org/example/examproject1styear/UserDashboard-view.fxml", "UserDashboard");
                }
                // Alle andre brugere sendes til User Dashboard
            } else {
                // Vises hvis brugernavn eller adgangskode ikke findes i databasen
                lblLoginError.setText("Forkert brugernavn eller adgangskode");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void openDashboard(ActionEvent event, String fxmlPath, String title) throws IOException {
        // Indlæser den ønskede FXML-fil
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Finder det nuværende vindue
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Opdaterer vinduets titel
        stage.setTitle(title);

        // Skifter scene til det valgte dashboard
        stage.setScene(new Scene(root));
        stage.show();
    }
}
