/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import acciones.CeldaAccionesTipoAlojamiento;
import bbdd.Conexion;
import bbdd.ConsultasTipoAlojamiento;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.TipoAlojamiento;

/**
 * Controlador encargado de gestionar la vista de administración de tipos de
 * alojamiento. Permite realizar operaciones de búsqueda, listado y creación de
 * nuevos tipos.
 *
 * @author k0343
 */
public class GestionTipoDeAlojamientoController implements Initializable {

    @FXML
    private TableView<TipoAlojamiento> tablaTipoAlojamiento;
    @FXML
    private TableColumn<TipoAlojamiento, Integer> columnaIdTipo;
    @FXML
    private TableColumn<TipoAlojamiento, String> columnaTipo;
    @FXML
    private TextField campoBuscarTipoAlojamiento;
    @FXML
    private Button botonNuevoTipo;
    @FXML
    private TableColumn<TipoAlojamiento, Void> columnaAcciones;

    /**
     * Inicializa la vista y sus componentes. Carga los tipos de alojamiento
     * existentes y configura los eventos para búsqueda y acciones.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablaTipoAlojamiento.setPlaceholder(new Label(""));

        tablaTipoAlojamiento.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        campoBuscarTipoAlojamiento.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarTiposAlojamientoEnTiempoReal(newValue);
        });

        columnaAcciones.setCellFactory(col -> new CeldaAccionesTipoAlojamiento(this));

        recargarTabla();
    }

    /**
     * Carga todos los tipos de alojamiento desde la base de datos y los muestra
     * en la tabla.
     */
    public void cargarDatosTiposAlojamiento() {
        ObservableList<TipoAlojamiento> listaTipos = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasTipoAlojamiento.cargarDatosTiposAlojamiento(listaTipos);
        Conexion.cerrarConexion();

        tablaTipoAlojamiento.setItems(listaTipos);
        tablaTipoAlojamiento.setPlaceholder(new Label("No hay tipos de alojamientos registrados."));
        columnaIdTipo.setCellValueFactory(new PropertyValueFactory<>("idTipo"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    }

    /**
     * Recarga la tabla de tipos de alojamiento actualizando los datos desde la
     * base de datos.
     */
    public void recargarTabla() {
        cargarDatosTiposAlojamiento();
    }

    /**
     * Busca en tiempo real tipos de alojamiento que coincidan con el texto
     * introducido.
     *
     * @param texto Texto a buscar.
     */
    private void buscarTiposAlojamientoEnTiempoReal(String texto) {
        ObservableList<TipoAlojamiento> listaTipos = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasTipoAlojamiento.cargarDatosTiposAlojamientoFiltrados(listaTipos, texto);
        Conexion.cerrarConexion();
        tablaTipoAlojamiento.setItems(listaTipos);
    }

    /**
     * Abre la ventana para registrar un nuevo tipo de alojamiento.
     *
     * @param event Evento de acción del botón.
     */
    @FXML
    private void nuevoTipoAlojamiento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarTipoAlojamiento.fxml"));
            Parent root = loader.load();
            AgregarTipoAlojamientoController controlador = loader.getController();
            controlador.setTitulo("Agregar Tipo de Alojamiento");
            controlador.setGestionTipoAlojamientoController(this);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.setTitle("Agregar tipo de alojamiento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
