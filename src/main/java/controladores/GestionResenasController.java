/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        destinoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreDestino"));
        comentarioDestinoColumn.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        clasificacionDestinoColumn.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));

        usuarioColumn.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        comentarioUsuarioColumn.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        clasificacionUsuarioColumn.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));

        List<Resena> resenas = Conexion.obtenerResenas();
        destinosTable.setItems(FXCollections.observableArrayList(resenas));
        usuariosTable.setItems(FXCollections.observableArrayList(resenas));
    }
}