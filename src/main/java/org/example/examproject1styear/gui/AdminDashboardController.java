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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import org.example.examproject1styear.dal.DB.MyDatabaseConnector;
import org.example.examproject1styear.be.User;
import javafx.scene.control.Alert;

public class AdminDashboardController {
    @FXML
    private TableView<User> tblUsers;

    @FXML
    private TableColumn<User, Integer> colId;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colUsername;

    @FXML
    private TableColumn<User, String> colRole;
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
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadUsers();
    }

    private void loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();

        String sql = "SELECT Id, Name, Username, Role FROM [User]";

        MyDatabaseConnector databaseConnector = new MyDatabaseConnector();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("Id"),
                        resultSet.getString("Name"),
                        resultSet.getString("Username"),
                        resultSet.getString("Role")
                ));
            }

            tblUsers.setItems(users);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDeleteUser() {
        // Henter den bruger, som er markeret i TableView
        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();

        // Hvis der ikke er valgt en bruger, vises en advarsel
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ingen bruger valgt");
            alert.setHeaderText("Vælg en bruger først");
            alert.showAndWait();
            return;
        }

        // SQL-kommando til at slette brugeren ud fra brugerens Id
        String sql = "DELETE FROM [User] WHERE Id = ?";

        // Opretter forbindelse til databasen
        MyDatabaseConnector databaseConnector = new MyDatabaseConnector();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Indsætter den valgte brugers Id i SQL-kommandoen
            statement.setInt(1, selectedUser.getId());

            // Udfører sletningen i databasen
            statement.executeUpdate();

            // Genindlæser tabellen, så den slettede bruger forsvinder fra GUI'en
            loadUsers();

        } catch (SQLException e) {
            // Udskriver databasefejl i konsollen
            e.printStackTrace();
        }
    }
    @FXML
    private void handleEditUser(ActionEvent event) throws IOException {
        // Henter den bruger der er markeret i tabellen
        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();

        // Hvis ingen bruger er valgt vises en advarsel
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ingen bruger valgt");
            alert.setHeaderText("Vælg en bruger først");
            alert.showAndWait();
            return;
        }
        // Åbner Edit User-vinduet
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/examproject1styear/EditUser-view.fxml"));

        Parent root = loader.load();

        // Sender den valgte bruger til EditUserController
        EditUserController controller = loader.getController();
        controller.setUser(selectedUser);

        // Skifter scene til Edit User
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Edit User");
        stage.setScene(new Scene(root));
        stage.show();
    }
}

