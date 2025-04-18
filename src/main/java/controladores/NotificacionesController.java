/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import bbdd.ConsultasNotificaciones;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Notificacion;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class NotificacionesController implements Initializable {

    @FXML
    private DatePicker campoFechaInicio;
    @FXML
    private DatePicker campoFechaFin;
    @FXML
    private Button botonGuardarCambios;
    @FXML
    private TableColumn<Notificacion, String> columnaNombre;
    @FXML
    private TableColumn<Notificacion, String> columnaDescripcion;
    @FXML
    private TableColumn<Notificacion, String> columnaDestinatario;
    @FXML
    private TableColumn<Notificacion, Date> columnaFecha;
    @FXML
    private TableColumn<Notificacion, String> Acciones;
    @FXML
    private TableView<Notificacion> tablaNotificaciones;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableView();
        cargarNotificaciones();
    }

    private void initializeTableView() {
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("notificacion"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaDestinatario.setCellValueFactory(new PropertyValueFactory<>("nombreDestinatario"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        // Asume que columnaAcciones será configurada si se decide añadir acciones como botones
    }

    private void cargarNotificaciones() {
        ObservableList<Notificacion> listaNotificaciones = FXCollections.observableArrayList();
        ConsultasNotificaciones.cargarDatosNotificaciones(listaNotificaciones);
        tablaNotificaciones.setItems(listaNotificaciones);
    }
}
