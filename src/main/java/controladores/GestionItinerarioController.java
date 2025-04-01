/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import modelo.ItinerarioTabla;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class GestionItinerarioController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableColumn<ItinerarioTabla, Boolean> columnAcciones;
    @FXML
    private TableColumn<ItinerarioTabla, Integer> columnaIdItinerario;
    @FXML
    private TableColumn<ItinerarioTabla, String> columnaNombre;
    @FXML
    private TableColumn<ItinerarioTabla, String> columaDescripcion;
    @FXML
    private TableColumn<ItinerarioTabla, Integer> columnaDuracion;
    @FXML
    private TableColumn<ItinerarioTabla, java.util.Date> columnaFechaCreacion; // o Date sin mas 
    @FXML
    private TextField campoBuscarItinerario;
    @FXML
    private Button botonNuevoItinerario;
    @FXML
    private TableView<ItinerarioTabla> tablaItinerario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarItinerarios();
        inicializarAccionesColumna();
    }

    public void cargarItinerarios() {
        ObservableList<ItinerarioTabla> listaItinerarios = FXCollections.observableArrayList();
        Conexion.conectar();
        Conexion.cargarDatosItinerarios(listaItinerarios);
        Conexion.cerrarConexion();

        tablaItinerario.setItems(listaItinerarios);
        columnaIdItinerario.setCellValueFactory(new PropertyValueFactory<>("idItinerario"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        columnaFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
    }

    private void inicializarAccionesColumna() {
        columnAcciones.setCellFactory(col -> new TableCell<ItinerarioTabla, Boolean>() {
            private final HBox hbox = new HBox(10);
            private final Button viewButton = new Button("Ver");
            private final Button editButton = new Button("Editar");
            private final Button toggleActiveButton = new Button();

            {
                viewButton.setOnAction(e -> viewItinerarioDetails(getTableRow().getItem()));
                editButton.setOnAction(e -> editItinerario(getTableRow().getItem()));
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

    private void viewItinerarioDetails(ItinerarioTabla itinerario) {
        // Implementación de la visualización de detalles
    }

    private void editItinerario(ItinerarioTabla itinerario) {
        // Implementación de la edición de itinerarios
    }

    private void toggleActiveStatus(ItinerarioTabla itinerario) {
        itinerario.setIsActive(!itinerario.getIsActive());
        tablaItinerario.refresh(); // Refresh the table to show updated status
    }

    @FXML
    private void NuevoItinerario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarItinerario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar itinerario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
