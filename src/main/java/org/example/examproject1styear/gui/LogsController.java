package org.example.examproject1styear.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import org.example.examproject1styear.be.Log;
import org.example.examproject1styear.dal.DB.MyDatabaseConnector;

public class LogsController {
    @FXML
    private TableView<Log> tblLogs;
    @FXML
    private TableColumn<Log, Integer> colLogId;
    @FXML
    private TableColumn<Log, String> colAction;
    @FXML
    private TableColumn<Log, String> colUsername;
    @FXML
    private TableColumn<Log, String> colCreatedAt;
    @FXML
    public void initialize() {

        // Binder kolonnerne i TableView til felterne i Log-klassen
        colLogId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Henter alle logs fra databasen når siden åbnes
        loadLogs();
    }
    private void loadLogs() {

        // Opretter en liste som skal indeholde logs fra databasen
        ObservableList<Log> logs = FXCollections.observableArrayList();

        // SQL-forespørgsel som henter alle logs
        String sql = "SELECT * FROM Logs";

        MyDatabaseConnector databaseConnector = new MyDatabaseConnector();

        // Opretter forbindelse til databasen og udfører SQL-forespørgslen
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            // Gennemgår alle rækker som databasen returnerer
            while (rs.next()) {

                // Opretter et Log-objekt ud fra dataene i databasen
                Log log = new Log(
                        rs.getInt("Id"),
                        rs.getString("Action"),
                        rs.getString("Username"),
                        rs.getString("CreatedAt")
                );

                //Tilføjer loggen til ObservableList
                logs.add(log);
            }

            //Viser alle logs i TableView
            tblLogs.setItems(logs);

        } catch (SQLException e) {
            //Udskriver fejl hvis der opstår problemer med databasen
            e.printStackTrace();
        }
    }

    //Går tilbage til Admin Dashboard
    @FXML
    private void handleBack(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/examproject1styear/Admindashboard-View.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Admin Dashboard");
        stage.show();
    }


}
