/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import java.net.URL;
import java.util.Date;
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
import modelo.Itinerario;


/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarItinerarioController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoDescripcion;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ComboBox<Integer> comboDuracion;
    @FXML
    private ImageView imagenTrekNic;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Conexion.conectar();
        ObservableList<Integer> duraciones = FXCollections.observableArrayList();
        comboDuracion.setItems(duraciones);
        Conexion.cargarComboDuracionItinerario(comboDuracion);
        Conexion.cerrarConexion();
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    @FXML
    private void RegistrarItinerario(ActionEvent event) {
        Conexion.conectar();

        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.aviso("Campo vacío", "La descripción no puede estar vacía.");
        } else if (comboDuracion.getValue() == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar una duración.");
        } else {
            Itinerario itinerario = new Itinerario();
            itinerario.setNombre(campoNombre.getText());
            itinerario.setDescripcion(campoDescripcion.getText());
            itinerario.setDuracion(comboDuracion.getValue());
            itinerario.setFechaCreacion(new java.util.Date()); // Asegúrate de que la fecha sea correcta según tu contexto
            itinerario.setIdUsuario(1); // Ajusta según el ID de usuario adecuado o cómo lo manejes

            if (Conexion.registrarItinerario(itinerario)) {
                Alertas.informacion("Itinerario registrado exitosamente.");
                limpiarFormulario();
            } else {
                Alertas.error("Error en el registro", "Ocurrió un error al registrar el itinerario.");
            }
        }
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoDescripcion.clear();
        comboDuracion.getSelectionModel().clearSelection();
    }
}
