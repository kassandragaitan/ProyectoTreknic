/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Actividad;
import modelo.Destino;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarActividadController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoDescripcion;
    @FXML
    private ComboBox<Destino> comboDestino;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Conexion.conectar();
        Conexion.cargarComboDestino(comboDestino);
        Conexion.cerrarConexion();

        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    @FXML
    private void RegistrarActividad(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.aviso("Campo vacío", "La descripción no puede estar vacía.");
        } else if (comboDestino.getValue() == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar un destino.");
        } else {
            Destino destinoSeleccionado = comboDestino.getValue();
            Actividad actividad = new Actividad(
                    0,
                    campoNombre.getText(),
                    campoDescripcion.getText(),
                    destinoSeleccionado.getId_destino()
            );

            if (Conexion.registrarActividad(actividad)) {
                Alertas.informacion("Actividad registrada exitosamente.");
                limpiarFormulario();
            } else {
                Alertas.error("Error en el registro", "No se pudo registrar la actividad.");
            }
        }
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoDescripcion.clear();
        comboDestino.getSelectionModel().clearSelection();
    }
}
