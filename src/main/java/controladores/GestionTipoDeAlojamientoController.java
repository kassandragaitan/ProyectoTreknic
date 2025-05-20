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
import modelo.Alojamiento;
import modelo.TipoAlojamiento;

/**
 * FXML Controller class
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
     * Initializes the controller class.
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

    public void recargarTabla() {
        cargarDatosTiposAlojamiento();
    }

    private void buscarTiposAlojamientoEnTiempoReal(String texto) {
        ObservableList<TipoAlojamiento> listaTipos = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasTipoAlojamiento.cargarDatosTiposAlojamientoFiltrados(listaTipos, texto);
        Conexion.cerrarConexion();
        tablaTipoAlojamiento.setItems(listaTipos);
    }

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
