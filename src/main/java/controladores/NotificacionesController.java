/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Animacion;
import bbdd.Conexion;
import bbdd.ConsultasMovimientos;
import bbdd.ConsultasNotificaciones;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Notificacion;
import modelo.PreferenciasNotificacion;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class NotificacionesController implements Initializable {

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
    @FXML
    private TextField campoHoraInicio;
    @FXML
    private TextField campoHoraFin;
    @FXML
    private CheckBox diaLunes;
    @FXML
    private CheckBox diaMartes;
    @FXML
    private CheckBox diaMiercoles;
    @FXML
    private CheckBox diaJueves;
    @FXML
    private CheckBox diaViernes;
    @FXML
    private CheckBox diaSabado;
    @FXML
    private CheckBox diaDomingo;
    @FXML
    private TableColumn<Notificacion, String> columnaTipo;
    @FXML
    private TableColumn<Notificacion, String> columnaPrioridad;
    @FXML
    private TableColumn<Notificacion, Boolean> columnaLeido;
    @FXML
    private CheckBox switchCorreo;
    @FXML
    private CheckBox switchSistema;
    @FXML
    private CheckBox switchResumen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableView();
        cargarNotificaciones();
        cargarPreferencias();
        tablaNotificaciones.setOnMouseClicked(event -> {
            Notificacion notificacionSeleccionada = tablaNotificaciones.getSelectionModel().getSelectedItem();
            if (notificacionSeleccionada != null && !notificacionSeleccionada.isLeido()) {
                ConsultasNotificaciones.marcarComoLeido(notificacionSeleccionada.getIdNotificacion());
                notificacionSeleccionada.setLeido(true);
                tablaNotificaciones.refresh();
            }
        });

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

        Animacion.aplicarAnimaciones(switchCorreo);
        Animacion.aplicarAnimaciones(switchSistema);
        Animacion.aplicarAnimaciones(switchResumen);
        Animacion.aplicarAnimaciones(diaLunes);
        Animacion.aplicarAnimaciones(diaMartes);
        Animacion.aplicarAnimaciones(diaMiercoles);
        Animacion.aplicarAnimaciones(diaJueves);
        Animacion.aplicarAnimaciones(diaViernes);
        Animacion.aplicarAnimaciones(diaSabado);
        Animacion.aplicarAnimaciones(diaDomingo);

    }

    private void initializeTableView() {
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("notificacion"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaDestinatario.setCellValueFactory(new PropertyValueFactory<>("nombreDestinatario"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoNotificacion"));
        columnaPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        columnaLeido.setCellValueFactory(new PropertyValueFactory<>("leido"));
    }

    private void cargarNotificaciones() {
        ObservableList<Notificacion> listaNotificaciones = FXCollections.observableArrayList();
        ConsultasNotificaciones.cargarDatosNotificaciones(listaNotificaciones);
        tablaNotificaciones.setItems(listaNotificaciones);
    }

    private int idUsuarioActivo = Usuario.getUsuarioActual().getIdUsuario();

    private void cargarPreferencias() {
        PreferenciasNotificacion preferencias = ConsultasNotificaciones.cargarPreferencias(idUsuarioActivo);

        switchCorreo.setSelected(preferencias.isCorreo());
        switchSistema.setSelected(preferencias.isSistema());
        switchResumen.setSelected(preferencias.isResumen());

        campoHoraInicio.setText(preferencias.getHoraInicio());
        campoHoraFin.setText(preferencias.getHoraFin());

        if (preferencias.getDias() != null) {
            for (char dia : preferencias.getDias().toCharArray()) {
                switch (dia) {
                    case 'L':
                        diaLunes.setSelected(true);
                        break;
                    case 'M':
                        diaMartes.setSelected(true);
                        break;
                    case 'X':
                        diaMiercoles.setSelected(true);
                        break;
                    case 'J':
                        diaJueves.setSelected(true);
                        break;
                    case 'V':
                        diaViernes.setSelected(true);
                        break;
                    case 'S':
                        diaSabado.setSelected(true);
                        break;
                    case 'D':
                        diaDomingo.setSelected(true);
                        break;
                }
            }
        }

    }

    @FXML
    private void guardarPreferencias() {

        PreferenciasNotificacion preferencias = new PreferenciasNotificacion();

        preferencias.setCorreo(switchCorreo.isSelected());
        preferencias.setSistema(switchSistema.isSelected());
        preferencias.setResumen(switchResumen.isSelected());
        preferencias.setHoraInicio(campoHoraInicio.getText());
        preferencias.setHoraFin(campoHoraFin.getText());

        StringBuilder diasActivos = new StringBuilder();
        if (diaLunes.isSelected()) {
            diasActivos.append("L");
        }
        if (diaMartes.isSelected()) {
            diasActivos.append("M");
        }
        if (diaMiercoles.isSelected()) {
            diasActivos.append("X");
        }
        if (diaJueves.isSelected()) {
            diasActivos.append("J");
        }
        if (diaViernes.isSelected()) {
            diasActivos.append("V");
        }
        if (diaSabado.isSelected()) {
            diasActivos.append("S");
        }
        if (diaDomingo.isSelected()) {
            diasActivos.append("D");
        }

        preferencias.setDias(diasActivos.toString());

        ConsultasNotificaciones.guardarPreferencias(idUsuarioActivo, preferencias);

        // ✅ Registrar el movimiento
        ConsultasMovimientos.registrarMovimiento("Actualizó sus preferencias de notificación", java.sql.Date.valueOf(LocalDate.now()), idUsuarioActivo);
    }

}
