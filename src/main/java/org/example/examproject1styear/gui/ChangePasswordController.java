package org.example.examproject1styear.gui;

import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.example.examproject1styear.dal.DB.MyDatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.io.IOException;

public class ChangePasswordController {
    @FXML
    private PasswordField txtOldPassword;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/examproject1styear/UserDashboard-view.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("User Dashboard");
        stage.show();
    }
    @FXML
    private void handleChangePassword(ActionEvent event) throws IOException {

        // Henter det gamle password, det nye password og bekræftelsen fra GUI'en
        String oldPassword = txtOldPassword.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Kontrollerer om det nye password og bekræftelsen er ens
        if (!newPassword.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fejl");
            alert.setHeaderText("Passwords matcher ikke");
            alert.showAndWait();
            return;
        }
        // SQL-forespørgsel som opdaterer brugerens password i databasen
        String sql = "UPDATE [User] SET Password = ? WHERE Password = ?";


        // Opretter forbindelse til databasen
        MyDatabaseConnector databaseConnector = new MyDatabaseConnector();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Indsætter værdierne i SQL-forespørgslen
            statement.setString(1, newPassword);
            statement.setString(2, oldPassword);

            //Udfører opdateringen i databasen
            int rowsUpdated = statement.executeUpdate();

            // Hvis mindst én række blev opdateret, er passwordet blevet ændret
            if (rowsUpdated > 0) {
                // Viser en besked til brugeren om at passwordet er blevet ændret
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Password ændret");
                alert.showAndWait();

                // Vises hvis det gamle password ikke blev fundet i databasen
                handleCancel(event);
            } else {
                // Vises hvis det gamle password ikke blev fundet i databasen
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fejl");
                alert.setHeaderText("Gammelt password er forkert");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            // Udskriver fejl i konsollen hvis der opstår en databasefejl
            e.printStackTrace();
        }
    }
}
