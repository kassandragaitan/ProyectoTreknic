/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

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
import modelo.Itinerario;


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
    private TableColumn<Itinerario, Boolean> columnAcciones;
    @FXML
    private TableColumn<Itinerario, Integer> columnaIdItinerario;
    @FXML
    private TableColumn<Itinerario, String> columnaNombre;
    @FXML
    private TableColumn<Itinerario, String> columaDescripcion;
    @FXML
    private TableColumn<Itinerario, Integer> columnaDuracion;
    @FXML
    private TableColumn<Itinerario, Date> columnaFechaCreacion;
    @FXML
    private TextField campoBuscarItinerario;
    @FXML
    private Button botonNuevoItinerario;
    @FXML
    private TableView<Itinerario> tablaItinerario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       columnaIdItinerario.setCellValueFactory(new PropertyValueFactory<>("id_itinerario"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        columnaFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fecha_creacion"));
        columnAcciones.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        tablaItinerario.setItems(generateDummyData());
        initializeActionButtons();
    }

    private ObservableList<Itinerario> generateDummyData() {
        ObservableList<Itinerario> itinerarios = FXCollections.observableArrayList(
            new Itinerario(1, "Ruta de Montaña", "Explorar las alturas", 5, new Date(), true),
            new Itinerario(2, "Paseo por la Costa", "Disfrute de la playa", 3, new Date(), false)
        );
        return itinerarios;
    }


    private void initializeActionButtons() {
        columnAcciones.setCellFactory(col -> new TableCell<Itinerario, Boolean>() {
            private final HBox hbox = new HBox(10);
            private final Button viewButton = new Button("Ver");
            private final Button editButton = new Button("Editar");
            private final Button toggleActiveButton = new Button();

            { // initializer block for setting button actions
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

    private void viewItinerarioDetails(Itinerario itinerario) {
    }

    private void editItinerario(Itinerario itinerario) {
    }

    private void toggleActiveStatus(Itinerario itinerario) {
        itinerario.setIsActive(!itinerario.getIsActive());
        tablaItinerario.refresh(); // Refresh the table to show updated status
    }

    @FXML
    private void NuevoItinerario(ActionEvent event) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = new Date();
        Itinerario nuevoItinerario = new Itinerario(0, "Nuevo Itinerario", "Descripción breve", 3, fechaActual, true);
        
        // Agregar a la tabla (simulación de inserción en BD)
        tablaItinerario.getItems().add(nuevoItinerario);
    }
}
