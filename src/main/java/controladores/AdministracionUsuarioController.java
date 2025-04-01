/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.UsuarioRegistro;

public class AdministracionUsuarioController implements Initializable {

    @FXML
    private TableView<UsuarioRegistro> userTable;
    @FXML
    private TableColumn<UsuarioRegistro, String> columnUsuario;
    @FXML
    private TableColumn<UsuarioRegistro, String> columnRol;
    @FXML
    private TableColumn<UsuarioRegistro, String> columnEstado;
    @FXML
    private TableColumn<UsuarioRegistro, String> columnUltimoAcceso;
    @FXML
    private TableColumn<UsuarioRegistro, Boolean> columnAcciones;
    @FXML
    private TextField searchField1;
    @FXML
    private Button botonNuevoUsuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnRol.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnUltimoAcceso.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        columnAcciones.setCellValueFactory(new PropertyValueFactory<>("activo"));

        userTable.setItems(generateUserData());
        initializeActionButtons();
    }

    private ObservableList<UsuarioRegistro> generateUserData() {
        ObservableList<UsuarioRegistro> users = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            String query = "SELECT id_usuario, nombre, email, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro FROM usuarios";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String tipoUsuario = rs.getString("tipo_usuario");
                String idioma = rs.getString("idioma_preferido");
                String tipoViajero = rs.getString("tipo_viajero");
                String telefono = rs.getString("telefono");
                Date fechaRegistro = rs.getDate("fecha_registro");

                users.add(new UsuarioRegistro(idUsuario, nombre, email, null, tipoUsuario, fechaRegistro, tipoViajero, idioma, telefono));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    private void initializeActionButtons() {
        columnAcciones.setCellFactory(col -> new TableCell<UsuarioRegistro, Boolean>() {
            private final HBox hbox = new HBox(10);
            private final Button viewButton = new Button("Ver");
            private final Button editButton = new Button("Editar");
            private final Button toggleActiveButton = new Button("Activar/Desactivar");

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
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        });
    }

    private void viewUserDetails(UsuarioRegistro user) {
        System.out.println("Viewing details for: " + user.getNombre());
    }

    private void editUser(UsuarioRegistro user) {
        System.out.println("Editing user: " + user.getNombre());
    }

    private void toggleActiveStatus(UsuarioRegistro user) {
        System.out.println("Toggling active status for: " + user.getNombre());
        // Aquí deberías tener lógica para cambiar el estado activo/inactivo del usuario
    }

    @FXML
    private void irAnuevoUsuario(ActionEvent event) {
        try {
            // Cargar el FXML de la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/GestionUsuarios.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Agregar usuario");
            stage.setScene(new Scene(root));

            // Hacer que la ventana sea modal (bloquea la principal hasta que se cierre)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Mostrar la ventana y esperar hasta que se cierre
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
