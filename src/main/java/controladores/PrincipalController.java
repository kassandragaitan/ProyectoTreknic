/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Destino;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class PrincipalController implements Initializable {

    @FXML
    private TableColumn<Destino, String> columnaDestino;
    @FXML
    private TableColumn<Destino, Integer> columnaVisitas;
    @FXML
    private TableColumn<Destino, String> columnaValoracion;
    @FXML
    private TableView<Destino> tablaDestinos;
    @FXML
    private Label labelUsuarioReg;
    @FXML
    private Label labelItinerarioActivo;
    @FXML
    private Label labelDestinosPopulares;
    @FXML
    private Label labelActividadesMen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        columnaDestino.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaVisitas.setCellValueFactory(new PropertyValueFactory<>("visitas"));
        columnaValoracion.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(generarEstrellas(cellData.getValue().getValoracion()));
        });

        cargarDatosDestinos();
        cargarDatosPanel();
    }

    private String generarEstrellas(double valoracion) {
        String estrellas = "";
        for (int i = 0; i < 5; i++) {
            estrellas += i < valoracion ? "★" : "☆";
        }
        return estrellas;
    }

    private void cargarDatosDestinos() {
        ObservableList<Destino> listadoDestinos = FXCollections.observableArrayList();
        Conexion.cargarDatosDestinos(listadoDestinos);
        tablaDestinos.setItems(listadoDestinos);
    }

    private void cargarDatosPanel() {
        int totalUsuarios = Conexion.contar("usuarios");
        int totalItinerarios = Conexion.contar("itinerario");
        int totalDestinosP = Conexion.contar("destinos");
        int totalActividadesMen = Conexion.contar("actividades");

        labelUsuarioReg.setText(String.valueOf(totalUsuarios) + " Usuarios");
        labelItinerarioActivo.setText(String.valueOf(totalItinerarios) + " Itinerarios");
        labelDestinosPopulares.setText(String.valueOf(totalDestinosP) + " Destinos");
        labelActividadesMen.setText(String.valueOf(totalActividadesMen) + " Actividades");
    }

}
