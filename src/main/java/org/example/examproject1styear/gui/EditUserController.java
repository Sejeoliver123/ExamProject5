package org.example.examproject1styear.gui;
import org.example.examproject1styear.be.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.examproject1styear.dal.DB.MyDatabaseConnector;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class EditUserController {
    @FXML
    private TextField txtName;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    public void initialize() {
        cmbRole.getItems().addAll("Admin", "User");
    }
    private User selectedUser;

    public void setUser(User user) {
        this.selectedUser = user;

        txtName.setText(user.getName());
        cmbRole.setValue(user.getRole());
    }


    @FXML
    private void handleSave(ActionEvent event) throws IOException {
        // Henter de nye værdier fra felterne
        String name = txtName.getText();
        String role = cmbRole.getValue();

        // SQL-kommando til at opdatere brugeren
        String sql = "UPDATE [User] SET Name = ?, Role = ? WHERE Id = ?";

        // Opretter forbindelse til databasen
        MyDatabaseConnector databaseConnector = new MyDatabaseConnector();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Indsætter de nye værdier i SQL-kommandoen
            statement.setString(1, name);
            statement.setString(2, role);
            statement.setInt(3, selectedUser.getId());

            // Udfører opdateringen
            statement.executeUpdate();

            // Returnerer til Admin Dashboard
            handleCancel(event);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/examproject1styear/Admindashboard-View.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("AdminDashboard");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
