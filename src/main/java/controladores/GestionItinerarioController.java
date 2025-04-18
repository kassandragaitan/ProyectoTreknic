/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import acciones.CeldaAccionesItinerario;
import bbdd.Conexion;
import bbdd.ConsultasItinerario;
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
import javafx.scene.control.ComboBox;
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
    private TableColumn<Itinerario, Void> columnAcciones;
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
    @FXML
    private ComboBox<String> comboDuraciones;
    @FXML
    private TableColumn<Itinerario, String> columnaUsuario;

  @Override
public void initialize(URL url, ResourceBundle rb) {
     cargarItinerarios();
        inicializarAccionesColumna();
        tablaItinerario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        columnaIdItinerario.setVisible(false);

        campoBuscarItinerario.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarItinerariosEnTiempoReal(newValue);
        });

        Conexion.conectar();
        ObservableList<String> duraciones = ConsultasItinerario.cargarDuracionesItinerarios();
        Conexion.cerrarConexion();

        comboDuraciones.setItems(duraciones);
        comboDuraciones.getSelectionModel().selectFirst();

        comboDuraciones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarItinerariosPorDuracion(newVal);
        });
    }

    private void filtrarItinerariosPorDuracion(String duracionSeleccionada) {
        ObservableList<Itinerario> lista = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasItinerario.cargarItinerariosPorDuracion(lista, duracionSeleccionada);
        Conexion.cerrarConexion();
        tablaItinerario.setItems(lista);
    }

    private void buscarItinerariosEnTiempoReal(String texto) {
        ObservableList<Itinerario> listaItinerarios = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasItinerario.cargarDatosItinerariosFiltrados(listaItinerarios, texto);
        Conexion.cerrarConexion();
        tablaItinerario.setItems(listaItinerarios);
    }

    public void cargarItinerarios() {
        ObservableList<Itinerario> listaItinerarios = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasItinerario.cargarDatosItinerarios(listaItinerarios);
        Conexion.cerrarConexion();

        tablaItinerario.setItems(listaItinerarios);
        columnaIdItinerario.setCellValueFactory(new PropertyValueFactory<>("idItinerario"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        columnaFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
    }

    private void inicializarAccionesColumna() {
        columnAcciones.setCellFactory(col -> new CeldaAccionesItinerario());
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
