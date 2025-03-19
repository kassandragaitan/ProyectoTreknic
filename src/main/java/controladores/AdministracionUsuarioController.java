/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.net.URL;
import java.util.ResourceBundle;
import modelo.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdministracionUsuarioController implements Initializable {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> columnUsuario;
    @FXML
    private TableColumn<User, String> columnRol;
    @FXML
    private TableColumn<User, String> columnEstado;
    @FXML
    private TableColumn<User, String> columnUltimoAcceso;
    @FXML
    private TableColumn<User, Boolean> columnAcciones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
  
        // Setup columns
        columnUsuario.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnRol.setCellValueFactory(new PropertyValueFactory<>("role"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnUltimoAcceso.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));
        columnAcciones.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        // Populate the table with dummy data
        userTable.setItems(generateDummyData());

        // Initialize buttons in the actions column
        initializeActionButtons();
    }

    private ObservableList<User> generateDummyData() {
        return FXCollections.observableArrayList(
            new User("Ana Martínez", "ana.martinez@example.com", "Administrador", "Activo", "30/9/2023, 14:32:15", true),
            new User("Carlos Rodríguez", "carlos.rodriguez@example.com", "Editor", "Activo", "1/10/2023, 9:15:22", true),
            new User("María López", "maria.lopez@example.com", "Visualizador", "Inactivo", "15/9/2023, 11:42:08", false),
            new User("Javier Fernández", "javier.fernandez@example.com", "Editor", "Activo", "2/10/2023, 16:05:33", true),
            new User("Laura Gómez", "laura.gomez@example.com", "Visualizador", "Activo", "28/9/2023, 10:24:45", true)
        );
    }

    private void initializeActionButtons() {
        columnAcciones.setCellFactory(col -> new TableCell<User, Boolean>() {
            private final HBox hbox = new HBox(10);
            private final Button viewButton = new Button("Ver");
            private final Button editButton = new Button("Editar");
            private final Button toggleActiveButton = new Button();

            { // initializer block for setting button actions
                viewButton.setOnAction(e -> viewUserDetails(getTableRow().getItem()));
                editButton.setOnAction(e -> editUser(getTableRow().getItem()));
                toggleActiveButton.setOnAction(e -> toggleActiveStatus(getTableRow().getItem()));
                hbox.getChildren().addAll(viewButton, editButton, toggleActiveButton);
                hbox.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Boolean isActive, boolean empty) {
                super.updateItem(isActive, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null || isActive == null) {
                    setGraphic(null);
                } else {
                    toggleActiveButton.setText(isActive ? "Desactivar" : "Activar");
                    setGraphic(hbox);
                }
            }
        });
    }

    private void viewUserDetails(User user) {
        System.out.println("Viewing details for: " + user.getUsername());
    }

    private void editUser(User user) {
        System.out.println("Editing user: " + user.getUsername());
    }

    private void toggleActiveStatus(User user) {
        System.out.println("Toggling active status for: " + user.getUsername());
        user.setIsActive(!user.getIsActive());
        userTable.refresh(); // Refresh the table to show updated status
    }
}