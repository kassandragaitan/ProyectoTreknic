/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import bbdd.ConsultasResenas;
import java.net.URL;
import java.util.List;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Resena;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class GestionResenasController implements Initializable {

    @FXML
    private TableView<Resena> destinosTable;
    @FXML
    private TableColumn<Resena, String> destinoColumn;
    @FXML
    private TableColumn<Resena, String> comentarioDestinoColumn;
    @FXML
    private TableColumn<Resena, Integer> clasificacionDestinoColumn;
    @FXML
    private TableView<Resena> usuariosTable;
    @FXML
    private TableColumn<Resena, String> usuarioColumn;
    @FXML
    private TableColumn<Resena, String> comentarioUsuarioColumn;
    @FXML
    private TableColumn<Resena, Integer> clasificacionUsuarioColumn;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button botonAgregarResena;
    @FXML
    private TextField campoBuscarResenasDestino;
    @FXML
    private TextField campoBuscarResenasUsuario;
    @FXML
    private Button botonAgregarResenaUsuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        destinoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreDestino"));
        comentarioDestinoColumn.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        clasificacionDestinoColumn.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));

        usuarioColumn.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        comentarioUsuarioColumn.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        clasificacionUsuarioColumn.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));

        List<Resena> resenas = ConsultasResenas.obtenerResenas();
        destinosTable.setItems(FXCollections.observableArrayList(resenas));
        usuariosTable.setItems(FXCollections.observableArrayList(resenas));
        destinosTable.setPlaceholder(new Label("No hay reseñas de destinos registradas."));
        usuariosTable.setPlaceholder(new Label("No hay reseñas de usuarios registradas."));
        campoBuscarResenasDestino.textProperty().addListener((obs, oldVal, newVal) -> {
            buscarResenasPorDestino(newVal);
        });

        campoBuscarResenasUsuario.textProperty().addListener((obs, oldVal, newVal) -> {
            buscarResenasPorUsuario(newVal);
        });

    }

    private void buscarResenasPorDestino(String texto) {
        ObservableList<Resena> listaResenas = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasResenas.buscarResenasPorDestino(listaResenas, texto);
        Conexion.cerrarConexion();
        destinosTable.setItems(listaResenas);
    }

    private void buscarResenasPorUsuario(String texto) {
        ObservableList<Resena> listaResenas = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasResenas.buscarResenasPorUsuario(listaResenas, texto);
        Conexion.cerrarConexion();
        usuariosTable.setItems(listaResenas);
    }

    @FXML
    private void abrirFormularioResena(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/FormularioResena.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nueva Reseña");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            List<Resena> resenas = ConsultasResenas.obtenerResenas();
            destinosTable.setItems(FXCollections.observableArrayList(resenas));
            usuariosTable.setItems(FXCollections.observableArrayList(resenas));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
