/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import bbdd.ConsultasCategoria;
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
import modelo.Categoria;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class GestionCategoriaController implements Initializable {

    @FXML
    private TableView<Categoria> tablaCategoria;
    @FXML
    private TableColumn<Categoria, Integer> columnaIdCategoria;
    @FXML
    private TableColumn<Categoria, String> columnaNombre;
    @FXML
    private TableColumn<Categoria, String> columnaDescripcion;
    @FXML
    private TextField campoBuscarCategoria;
    @FXML
    private Button botonNuevaCategoria;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        campoBuscarCategoria.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarCategoriasEnTiempoReal(newValue);
        });
        recargarTabla();
    }

    public void cargarDatosCategorias() {  // Cambié 'private' a 'public'
        ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasCategoria.cargarDatosCategorias(listaCategorias);
        Conexion.cerrarConexion();
        tablaCategoria.setItems(listaCategorias);
        columnaIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

// Método para recargar la tabla
    public void recargarTabla() {
        cargarDatosCategorias();  // Recargar la tabla con nuevos datos
    }

    private void buscarCategoriasEnTiempoReal(String texto) {
        ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasCategoria.cargarDatosCategoriasFiltradas(listaCategorias, texto);
        Conexion.cerrarConexion();
        tablaCategoria.setItems(listaCategorias);
    }

    @FXML
    private void nuevaCategoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarCategoria.fxml"));
            Parent root = loader.load();

            // Pasar la referencia del controlador principal
            AgregarCategoriaController controlador = loader.getController();
            controlador.setGestionCategoriaController(this);  // Aquí se pasa la referencia

            Stage stage = new Stage();
            stage.setTitle("Agregar Categoría");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
