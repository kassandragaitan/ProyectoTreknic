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
import java.util.Date;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.Itinerario;

/**
 * Controlador para la gestión de itinerarios. Permite registrar, visualizar,
 * filtrar, buscar y editar itinerarios de viaje. Gestiona la tabla principal de
 * itinerarios y sus filtros.
 *
 * Autor: k0343
 */
public class GestionItinerarioController implements Initializable {

    @FXML
    private TableColumn<Itinerario, Void> columnAcciones;
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
    private TableColumn<Itinerario, String> columnaUsuario;
    @FXML
    private ComboBox<String> comboFiltroPor;
    @FXML
    private ComboBox<String> comboValorFiltro;
    @FXML
    private Button botonQuitarFiltro;
    @FXML
    private TableColumn<?, ?> columnaIdItinerario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarItinerarios();
        botonQuitarFiltro.setDisable(true);

        inicializarAccionesColumna();
        tablaItinerario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        columnaIdItinerario.setVisible(false);

        campoBuscarItinerario.textProperty().addListener((observable, oldValue, newValue) -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);
            buscarItinerariosEnTiempoReal(newValue);
            botonQuitarFiltro.setDisable(newValue.trim().isEmpty());
        });

        ObservableList<String> filtros = FXCollections.observableArrayList(
                "Selecciona un tipo de filtro...",
                "Filtrar por duración",
                "Filtrar por fecha"
        );
        comboFiltroPor.setItems(filtros);
        comboFiltroPor.getSelectionModel().selectFirst();

        comboValorFiltro.setDisable(true);

        comboFiltroPor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);

            if (newVal == null || newVal.equals("Selecciona un tipo de filtro...")) {
                campoBuscarItinerario.clear();
                cargarItinerarios();
                botonQuitarFiltro.setDisable(true);
                return;
            }

            ObservableList<String> opciones = FXCollections.observableArrayList();
            String textoPlaceholder = "";

            Conexion.conectar();
            switch (newVal) {
                case "Filtrar por duración":
                    opciones = ConsultasItinerario.cargarTodasLasDuracionesEnum();
                    textoPlaceholder = "Selecciona una duración...";
                    break;
                case "Filtrar por fecha":
                    opciones = ConsultasItinerario.cargarFechasItinerarios();
                    textoPlaceholder = "Selecciona una fecha...";
                    break;
            }
            Conexion.cerrarConexion();

            if (!textoPlaceholder.isEmpty() && !opciones.contains(textoPlaceholder)) {
                opciones.add(0, textoPlaceholder);
            }

            comboValorFiltro.setItems(opciones);
            comboValorFiltro.getSelectionModel().selectFirst();
            comboValorFiltro.setDisable(false);
        });
        comboValorFiltro.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.startsWith("Selecciona")) {
                botonQuitarFiltro.setDisable(true);
                cargarItinerarios();
                return;
            }

            botonQuitarFiltro.setDisable(false);

            ObservableList<Itinerario> lista = FXCollections.observableArrayList();
            String filtro = comboFiltroPor.getValue();

            Conexion.conectar();
            if ("Filtrar por duración".equals(filtro)) {
                ConsultasItinerario.cargarItinerariosPorDuracion(lista, newVal);
            } else if ("Filtrar por fecha".equals(filtro)) {
                ConsultasItinerario.cargarItinerariosPorFecha(lista, newVal);
            }
            Conexion.cerrarConexion();
            tablaItinerario.setItems(lista);

            if (lista.isEmpty()) {
                tablaItinerario.setPlaceholder(new Label("No se encontraron coincidencias con ese filtro."));
            } else {
                tablaItinerario.setPlaceholder(new Label(""));
            }
        });

        botonQuitarFiltro.setOnAction(e -> {
            comboFiltroPor.getSelectionModel().selectFirst();

            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.setDisable(true);

            campoBuscarItinerario.clear();
            cargarItinerarios();

            comboFiltroPor.setVisible(false);
            comboFiltroPor.setVisible(true);
            botonQuitarFiltro.setDisable(true);

        });
    }

    /**
     * Busca itinerarios en tiempo real según el texto proporcionado.
     *
     * @param texto Texto a buscar.
     */
    private void buscarItinerariosEnTiempoReal(String texto) {
        ObservableList<Itinerario> listaItinerarios = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasItinerario.cargarDatosItinerariosFiltrados(listaItinerarios, texto);
        Conexion.cerrarConexion();
        tablaItinerario.setItems(listaItinerarios);
        if (listaItinerarios.isEmpty()) {
            tablaItinerario.setPlaceholder(new Label("No se encontraron coincidencias para la búsqueda."));
        }
    }

    /**
     * Carga todos los itinerarios en la tabla.
     */
    public void cargarItinerarios() {
        ObservableList<Itinerario> listaItinerarios = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasItinerario.cargarDatosItinerarios(listaItinerarios);
        Conexion.cerrarConexion();

        tablaItinerario.setItems(listaItinerarios);
        tablaItinerario.setPlaceholder(new Label("No hay itinerarios registrados."));
        columnaIdItinerario.setCellValueFactory(new PropertyValueFactory<>("idItinerario"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        columnaFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
    }

    /**
     * Recarga la tabla de itinerarios manteniendo el filtro actual si existe.
     */
    public void recargarTablaGestionItinerario() {
        ObservableList<Itinerario> lista = FXCollections.observableArrayList();
        String filtro = comboFiltroPor.getValue();
        String valor = comboValorFiltro.getValue();

        Conexion.conectar();

        if ("Filtrar por fecha".equals(filtro) && valor != null && !valor.startsWith("Selecciona")) {
            ConsultasItinerario.cargarItinerariosPorFecha(lista, valor);
        } else if ("Filtrar por duración".equals(filtro) && valor != null && !valor.startsWith("Selecciona")) {
            ConsultasItinerario.cargarItinerariosPorDuracion(lista, valor);
        } else {
            ConsultasItinerario.cargarDatosItinerarios(lista);
        }

        Conexion.cerrarConexion();
        tablaItinerario.setItems(lista);
    }

    /**
     * Inicializa la columna de acciones (ver/editar).
     */
    private void inicializarAccionesColumna() {
        columnAcciones.setCellFactory(col -> new CeldaAccionesItinerario(this));

    }

    /**
     * Abre la ventana de registro de nuevo itinerario.
     *
     * @param event Evento de acción.
     */
    @FXML
    private void NuevoItinerario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarItinerario.fxml"));
            Parent root = loader.load();

            AgregarItinerarioController controlador = loader.getController();
            controlador.setTitulo("Agregar Itinerario");
            controlador.setGestionItinerarioController(this);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initStyle(StageStyle.DECORATED);

            stage.setTitle("Agregar Itinerario");
            stage.initStyle(StageStyle.DECORATED);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.getScene().setUserData(this);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("/img/montanita.png"));
            stage.showAndWait();
            if (controlador.getModificado()) {
                recargarTablaGestionItinerario();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
