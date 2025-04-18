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


/**
 * FXML Controller class
 *
 * @author k0343
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
    private Button botonNuevaActividad;
    @FXML
    private ComboBox<?> combo1;
    @FXML
    private ComboBox<?> combo2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarActividades();
        columnaIdActividad.setVisible(false);

        tablaActividades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        campoBuscarActividad.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarActividadesEnTiempoReal(newValue);
        });
        inicializarAccionesColumna();

    }
private void inicializarAccionesColumna() {
    columnAcciones.setCellFactory(col -> new CeldaAccionesActividad());
}

    private void buscarActividadesEnTiempoReal(String texto) {
        ObservableList<Actividad> listaActividades = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasActividades.cargarDatosActividadesFiltradas(listaActividades, texto);
        Conexion.cerrarConexion();
        tablaActividades.setItems(listaActividades);
    }

    private void cargarActividades() {
        ObservableList<Actividad> listaActividades = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasActividades.cargarDatosActividades(listaActividades);
        Conexion.cerrarConexion();

        tablaActividades.setItems(listaActividades);
        columnaIdActividad.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaIdDestino.setCellValueFactory(new PropertyValueFactory<>("nombreDestino"));

    }

    @FXML
    private void RegistrarNuevaActividad(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarActividad.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar itinerario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
