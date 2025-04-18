/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasItinerario;
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
import javafx.stage.Stage;
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
    
    private Itinerario itinerarioActual;
    private boolean esEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Conexion.conectar();
        ObservableList<Integer> duraciones = FXCollections.observableArrayList();
        comboDuracion.setItems(duraciones);
        ConsultasItinerario.cargarComboDuracionItinerario(comboDuracion);
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
            if (esEdicion && itinerarioActual != null) {
                // Modo edición
                itinerarioActual.setNombre(campoNombre.getText());
                itinerarioActual.setDescripcion(campoDescripcion.getText());
                itinerarioActual.setDuracion(comboDuracion.getValue());

                if (ConsultasItinerario.actualizarItinerario(itinerarioActual)) {
                    Alertas.informacion("Itinerario actualizado exitosamente.");
                    cerrarVentana();
                } else {
                    Alertas.error("Error al actualizar", "No se pudo actualizar el itinerario.");
                }
            } else {
                // Modo nuevo
                Itinerario nuevo = new Itinerario();
                nuevo.setNombre(campoNombre.getText());
                nuevo.setDescripcion(campoDescripcion.getText());
                nuevo.setDuracion(comboDuracion.getValue());
                nuevo.setFechaCreacion(new Date());
                nuevo.setIdUsuario(1); // Ajusta si estás usando login

                if (ConsultasItinerario.registrarItinerario(nuevo)) {
                    Alertas.informacion("Itinerario registrado exitosamente.");
                    limpiarFormulario();
                } else {
                    Alertas.error("Error en el registro", "Ocurrió un error al registrar el itinerario.");
                }
            }
        }
    }

    public void verItinerario(Itinerario it) {
        this.itinerarioActual = it;
        campoNombre.setText(it.getNombre());
        campoDescripcion.setText(it.getDescripcion());
        comboDuracion.setValue(it.getDuracion());
        this.esEdicion = true;
        botonRegistrar.setText("Actualizar");
    }

    public void setEdicionActiva(boolean editable) {
        campoNombre.setEditable(editable);
        campoDescripcion.setEditable(editable);
        comboDuracion.setDisable(!editable);
        botonRegistrar.setVisible(editable);

        double opacidad = editable ? 1.0 : 0.75;
        campoNombre.setOpacity(opacidad);
        campoDescripcion.setOpacity(opacidad);
        comboDuracion.setOpacity(opacidad);
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoDescripcion.clear();
        comboDuracion.getSelectionModel().clearSelection();
        botonRegistrar.setText("Guardar");
    }

    private void cerrarVentana() {
        Stage stage = (Stage) botonRegistrar.getScene().getWindow();
        stage.close();
    }
}
