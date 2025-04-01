/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Alojamiento;
import modelo.ItinerarioTabla;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class GestionAlojamientoController implements Initializable {

    @FXML
    private TableView<Alojamiento> tablaAlojamientos;
    @FXML
    private TableColumn<Alojamiento, Integer> columnaIdAlojamiento;
    @FXML
    private TableColumn<Alojamiento, String> columnaNombre;
    @FXML
    private TableColumn<Alojamiento, Integer> columnaTipo;
    @FXML
    private TableColumn<Alojamiento, String> columnaContacto;
    @FXML
    private TableColumn<Alojamiento, String> columnaImagen;
    @FXML
    private TableColumn<Alojamiento, Integer> columnaIdDestino;
    @FXML
    private TableColumn<Alojamiento, Boolean> columnAcciones;
    @FXML
    private TextField campoBuscarAlojamiento;
    @FXML
    private Button botonNuevoAlojamiento;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarAlojamientos();
    }

    private void cargarAlojamientos() {
        ObservableList<Alojamiento> listaAlojamientos = FXCollections.observableArrayList();
        Conexion.conectar();
        Conexion.cargarDatosAlojamientos(listaAlojamientos);
        Conexion.cerrarConexion();

        tablaAlojamientos.setItems(listaAlojamientos);
        columnaIdAlojamiento.setCellValueFactory(new PropertyValueFactory<>("idAlojamiento"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("idTipo"));
        columnaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        columnaImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
        columnaIdDestino.setCellValueFactory(new PropertyValueFactory<>("idDestino"));
    }

    @FXML
    private void nuevoAlojamiento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarAlojamiento.fxml"));
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
