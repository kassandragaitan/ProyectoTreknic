/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import acciones.CeldaAccionesNotificacion;
import bbdd.ConsultasNotificaciones;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Notificacion;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class NotificacionesController implements Initializable {

    @FXML
    private TableColumn<Notificacion, String> columnaDescripcion;
    @FXML
    private TableColumn<Notificacion, String> columnaUsuario;
    @FXML
    private TableColumn<Notificacion, Date> columnaFecha;
    @FXML
    private TableColumn<Notificacion, Void> Acciones;
    @FXML
    private TableView<Notificacion> tablaNotificaciones;
    @FXML
    private TableColumn<Notificacion, Boolean> columnaLeido;
    @FXML
    private ComboBox<?> comboFiltro1;
    @FXML
    private ComboBox<?> comboFiltro2;
    @FXML
    private ComboBox<?> comboLeido;
    @FXML
    private Button botonFiltrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablaNotificaciones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initializeTableView();
        cargarNotificaciones();
        tablaNotificaciones.setOnMouseClicked(event -> {
            Notificacion notificacionSeleccionada = tablaNotificaciones.getSelectionModel().getSelectedItem();
            if (notificacionSeleccionada != null && !notificacionSeleccionada.isLeido()) {
                ConsultasNotificaciones.marcarComoLeido(notificacionSeleccionada.getIdNotificacion());
                notificacionSeleccionada.setLeido(true);
                tablaNotificaciones.refresh();
            }
        });
        Acciones.setCellFactory(col -> new CeldaAccionesNotificacion());

        columnaLeido.setCellValueFactory(new PropertyValueFactory<>("leido"));
        columnaLeido.setCellFactory(col -> new javafx.scene.control.cell.TextFieldTableCell<Notificacion, Boolean>() {
            @Override
            public void updateItem(Boolean leido, boolean empty) {
                super.updateItem(leido, empty);
                if (empty || leido == null) {
                    setText(null);
                } else {
                    setText(leido ? "Sí" : "No");
                }
            }
        });
    }

    private void initializeTableView() {
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaLeido.setCellValueFactory(new PropertyValueFactory<>("leido"));
    }

    private void cargarNotificaciones() {
        ObservableList<Notificacion> listaNotificaciones = FXCollections.observableArrayList();
        ConsultasNotificaciones.cargarDatosNotificaciones(listaNotificaciones);
        tablaNotificaciones.setItems(listaNotificaciones);
        tablaNotificaciones.setPlaceholder(new Label("No hay notificaciones disponibles."));
    }

    @FXML
    private void filtrar(ActionEvent event) {
    }
}
