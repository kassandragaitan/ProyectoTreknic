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

/**
 * Controlador de la vista de notificaciones en la aplicación.
 * <p>
 * Se encarga de cargar, mostrar y filtrar las notificaciones almacenadas en el
 * sistema, así como de marcar notificaciones como leídas cuando son
 * seleccionadas. Ofrece filtros por usuario, fecha y estado de lectura.
 * </p>
 *
 * Componentes incluidos:
 * <ul>
 * <li>Tabla de notificaciones con columnas de descripción, usuario, fecha y
 * estado leído.</li>
 * <li>ComboBoxes para aplicar filtros dinámicos.</li>
 * <li>Acciones como eliminar o gestionar la notificación seleccionada.</li>
 * </ul>
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
    private ComboBox<String> comboFiltro1;
    @FXML
    private ComboBox<String> comboFiltro2;
    @FXML
    private ComboBox<String> comboLeido;
    @FXML
    private Button botonFiltrar;

    /**
     * Inicializa la vista de notificaciones.
     * <p>
     * Configura la tabla de notificaciones, carga los datos desde la base de
     * datos, establece los valores iniciales de los ComboBoxes de filtro y
     * define los eventos para marcar notificaciones como leídas.
     * </p>
     *
     * @param url URL de localización (no se utiliza).
     * @param rb Recursos de internacionalización (no se utiliza).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablaNotificaciones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initializeTableView();
        cargarNotificaciones();

        comboFiltro1.setItems(FXCollections.observableArrayList());
        comboFiltro1.getItems().add("Todos los usuarios...");
        comboFiltro1.getItems().addAll(ConsultasNotificaciones.obtenerUsuariosNotificaciones());
        comboFiltro1.getSelectionModel().selectFirst();

        comboFiltro2.setItems(FXCollections.observableArrayList());
        comboFiltro2.getItems().add("Todas las fechas...");
        comboFiltro2.getItems().addAll(ConsultasNotificaciones.obtenerFechasNotificaciones());
        comboFiltro2.getSelectionModel().selectFirst();

        comboLeido.setItems(FXCollections.observableArrayList("Mostrar todas las notificaciones", "Solo leídas", "Solo no leídas"));
        comboLeido.getSelectionModel().selectFirst();

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

    /**
     * Establece las propiedades de mapeo para las columnas de la tabla de
     * notificaciones, permitiendo que se visualicen correctamente los datos de
     * cada notificación.
     */
    private void initializeTableView() {
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaLeido.setCellValueFactory(new PropertyValueFactory<>("leido"));
    }

    /**
     * Carga todas las notificaciones desde la base de datos y las muestra en la
     * tabla. Si no hay registros, se muestra un mensaje de aviso en la tabla.
     */
    private void cargarNotificaciones() {
        ObservableList<Notificacion> listaNotificaciones = FXCollections.observableArrayList();
        ConsultasNotificaciones.cargarDatosNotificaciones(listaNotificaciones);
        tablaNotificaciones.setItems(listaNotificaciones);
        tablaNotificaciones.setPlaceholder(new Label("No hay notificaciones disponibles."));
    }

    /**
     * Aplica filtros a la lista de notificaciones mostradas en la tabla, en
     * función del usuario, la fecha y el estado de lectura seleccionados.
     *
     * @param event Evento generado al hacer clic en el botón "Filtrar".
     */
    @FXML
    private void filtrar(ActionEvent event) {
        String usuario = comboFiltro1.getValue();
        String fecha = comboFiltro2.getValue();
        String leido = comboLeido.getValue();

        if ("Todos los usuarios...".equals(usuario)) {
            usuario = null;
        }
        if ("Todas las fechas...".equals(fecha)) {
            fecha = null;
        }
        if ("Mostrar todas las notificaciones".equals(leido)) {
            leido = null;
        } else if ("Solo leídas".equals(leido)) {
            leido = "Sí";
        } else if ("Solo no leídas".equals(leido)) {
            leido = "No";
        }

        ObservableList<Notificacion> lista = FXCollections.observableArrayList();
        ConsultasNotificaciones.filtrarNotificaciones(lista, usuario, fecha, leido);
        tablaNotificaciones.setItems(lista);
    }

}
