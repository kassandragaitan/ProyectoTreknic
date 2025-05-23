/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import bbdd.ConsultasActividades;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Actividad;
import acciones.CeldaAccionesActividad;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

/**
 * Controlador JavaFX para la gestión de actividades turísticas. Permite
 * visualizar, buscar, filtrar, registrar y administrar las actividades
 * disponibles en el sistema. Incluye funcionalidad para abrir el formulario de
 * registro y para aplicar filtros dinámicos por nombre o destino.
 *
 * Autor: k0343
 */
public class GestionActividadesController implements Initializable {

    @FXML
    private TableView<Actividad> tablaActividades;
    @FXML
    private TableColumn<Actividad, Integer> columnaIdActividad;
    @FXML
    private TableColumn<Actividad, String> columnaNombre;
    @FXML
    private TableColumn<Actividad, String> columnaDescripcion;
    @FXML
    private TableColumn<Actividad, String> columnaIdDestino;
    @FXML
    private TableColumn<Actividad, Void> columnAcciones;
    @FXML
    private TextField campoBuscarActividad;
    @FXML
    private ComboBox<String> comboFiltroPor;
    @FXML
    private ComboBox<String> comboValorFiltro;
    @FXML
    private Button botonQuitarFiltro;
    @FXML
    private Button botonNuevaActividad;

    /**
     * Inicializa el controlador configurando la tabla, los filtros y la
     * búsqueda en tiempo real.
     *
     * @param url URL de inicialización.
     * @param rb ResourceBundle para localización.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarActividades();
        botonQuitarFiltro.setDisable(true);

        columnaIdActividad.setVisible(false);

        tablaActividades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        campoBuscarActividad.textProperty().addListener((observable, oldValue, newValue) -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);
            buscarActividadesEnTiempoReal(newValue);
            botonQuitarFiltro.setDisable(newValue.trim().isEmpty());
        });

        inicializarAccionesColumna();

        ObservableList<String> filtros = FXCollections.observableArrayList(
                "Selecciona un tipo de filtro...",
                "Filtrar por destino"
        );
        comboFiltroPor.setItems(filtros);
        comboFiltroPor.getSelectionModel().selectFirst();

        comboValorFiltro.setDisable(true);

        comboFiltroPor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);
            botonQuitarFiltro.setDisable(true);

            if (newVal == null || newVal.equals("Selecciona un tipo de filtro...")) {
                cargarActividades();
                return;
            }

            ObservableList<String> opciones = FXCollections.observableArrayList();
            Conexion.conectar();

            if (newVal.equals("Filtrar por destino")) {
                opciones = ConsultasActividades.cargarNombresDestinos();
            }

            Conexion.cerrarConexion();

            String placeholder = "Selecciona un destino...";
            if (!opciones.contains(placeholder)) {
                opciones.add(0, placeholder);
            }

            comboValorFiltro.setItems(opciones);
            comboValorFiltro.getSelectionModel().selectFirst();
            comboValorFiltro.setDisable(false);
        });

        comboValorFiltro.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.startsWith("Selecciona")) {
                cargarActividades();
                botonQuitarFiltro.setDisable(true);
                return;
            }

            botonQuitarFiltro.setDisable(false);

            ObservableList<Actividad> lista = FXCollections.observableArrayList();
            Conexion.conectar();
            ConsultasActividades.cargarActividadesPorDestino(lista, newVal);
            Conexion.cerrarConexion();
            tablaActividades.setItems(lista);
        });

    }

    /**
     * Inicializa la columna de acciones con botones personalizados para cada
     * fila.
     */
    private void inicializarAccionesColumna() {
        columnAcciones.setCellFactory(col -> new CeldaAccionesActividad(this));
    }

    /**
     * Realiza una búsqueda en tiempo real por texto ingresado en el campo de
     * búsqueda.
     *
     * @param texto Texto a buscar en nombre o descripción de actividades.
     */
    private void buscarActividadesEnTiempoReal(String texto) {
        ObservableList<Actividad> listaActividades = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasActividades.cargarDatosActividadesFiltradas(listaActividades, texto);
        Conexion.cerrarConexion();
        tablaActividades.setItems(listaActividades);
    }

    /**
     * Carga todas las actividades desde la base de datos y las muestra en la
     * tabla.
     */
    public void cargarActividades() {
        ObservableList<Actividad> listaActividades = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasActividades.cargarDatosActividades(listaActividades);
        Conexion.cerrarConexion();

        tablaActividades.setItems(listaActividades);
        tablaActividades.setPlaceholder(new Label("No hay actividades registradas."));
        columnaIdActividad.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaIdDestino.setCellValueFactory(new PropertyValueFactory<>("nombreDestino"));
    }

    /**
     * Recarga la tabla de actividades. Útil tras registrar, editar o eliminar
     * una actividad.
     */
    public void recargarTabla() {
        cargarActividades();
    }

    /**
     * Abre el formulario para registrar una nueva actividad.
     *
     * @param event Evento generado al presionar el botón "Nueva Actividad".
     */
    @FXML
    private void RegistrarNuevaActividad(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarActividad.fxml"));
            Parent root = loader.load();

            AgregarActividadController controlador = loader.getController();
            controlador.setTitulo("Agregar Actividad");
            controlador.setGestionActividadesController(this);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.setTitle("Agregar Actividad");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("/img/montanita.png"));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Restaura la tabla a su estado original quitando todos los filtros
     * aplicados.
     *
     * @param event Evento del botón "Quitar Filtro".
     */
    @FXML
    private void quitarFiltroActividad(ActionEvent event) {
        comboFiltroPor.getSelectionModel().selectFirst();
        comboValorFiltro.getItems().clear();
        comboValorFiltro.getItems().add("Selecciona un destino...");
        comboValorFiltro.getSelectionModel().selectFirst();
        comboValorFiltro.setDisable(true);

        campoBuscarActividad.clear();

        cargarActividades();

        botonQuitarFiltro.setDisable(true);
    }

}
