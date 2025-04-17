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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Actividad;
import modelo.Alojamiento;
import modelo.Destino;
import modelo.TipoAlojamiento;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarAlojamientoController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private ComboBox<TipoAlojamiento> comboTipo;
    @FXML
    private TextField campoContacto;
    @FXML
    private TextField campoImagen;
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
        Conexion.cargarDatosTiposAlojamientoRegistrar(comboTipo);
        Conexion.cargarComboDestino(comboDestino);
        Conexion.cerrarConexion();

        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    @FXML
    private void RegistrarAlojamiento(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoContacto)) {
            Alertas.aviso("Campo vacío", "El contacto no puede estar vacía.");
        } else if (comboDestino.getValue() == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar un destino.");
        } else {
            TipoAlojamiento tipoAlojamientoSeleccionado = comboTipo.getValue();
            Destino destinoSeleccionado = comboDestino.getValue();
            Alojamiento alojamiento = new Alojamiento(
                    0,
                    campoNombre.getText(),
                    tipoAlojamientoSeleccionado.getIdTipo(),
                    campoContacto.getText(),
                    campoImagen.getText(),
                    destinoSeleccionado.getId_destino()
            );

            if (Conexion.registrarAlojamiento(alojamiento)) {
                Alertas.informacion("Alojamiento registrado exitosamente.");
                limpiarFormulario();
            } else {
                Alertas.error("Error en el registro", "No se pudo registrar el alojamiento.");
            }
        }
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoContacto.clear();
        campoImagen.clear();
        comboTipo.getSelectionModel().clearSelection();
        comboDestino.getSelectionModel().clearSelection();
    }
}
