/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasItinerario;
import bbdd.ConsultasMovimientos;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Itinerario;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarItinerarioController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ComboBox<String> comboDuracion;
    @FXML
    private ImageView imagenTrekNic;
    @FXML
    private Label labelTitulo;

    private GestionItinerarioController gestionItinerarioController;
    private boolean modificado = false;
    private Itinerario itinerarioActual;
    private boolean esEdicion = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Conexion.conectar();
        Conexion.conectar();
        ConsultasItinerario.cargarComboDuracionItinerario(comboDuracion);
        Conexion.cerrarConexion();

        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    public void setGestionItinerarioController(GestionItinerarioController controller) {
        this.gestionItinerarioController = controller;
    }

    public boolean getModificado() {
        return modificado;
    }

    @FXML
    private void RegistrarItinerario(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.aviso("Campo vacío", "La descripción no puede estar vacía.");
        } else if (comboDuracion.getValue() == null || comboDuracion.getValue().equals("Seleccione")) {
            Alertas.aviso("Campo vacío", "Debe seleccionar una duración válida.");
        } else if (!esEdicion && ConsultasItinerario.existeItinerarioConNombre(campoNombre.getText())) {
            Alertas.aviso("Nombre duplicado", "Ya existe un itinerario con ese nombre.");
        } else {
            Conexion.conectar();
            String duracionSeleccionada = comboDuracion.getValue();
            boolean exito;

            if (esEdicion && itinerarioActual != null) {
                itinerarioActual.setNombre(campoNombre.getText());
                itinerarioActual.setDescripcion(campoDescripcion.getText());
                itinerarioActual.setDuracion(duracionSeleccionada);

                exito = ConsultasItinerario.actualizarItinerario(itinerarioActual);
            } else {
                Itinerario nuevo = new Itinerario();
                nuevo.setNombre(campoNombre.getText());
                nuevo.setDescripcion(campoDescripcion.getText());
                nuevo.setDuracion(duracionSeleccionada);
                nuevo.setFechaCreacion(new Date());
                nuevo.setIdUsuario(Usuario.getUsuarioActual().getIdUsuario());

                exito = ConsultasItinerario.registrarItinerario(nuevo);

                if (exito) {
                    ConsultasMovimientos.registrarMovimiento(
                            "Ha registrado el itinerario " + campoNombre.getText(),
                            new java.sql.Date(System.currentTimeMillis()),
                            Usuario.getUsuarioActual().getIdUsuario()
                    );
                }
            }

            Conexion.cerrarConexion();

            if (exito) {
                Alertas.informacion(esEdicion
                        ? "Itinerario actualizado correctamente."
                        : "Itinerario registrado exitosamente.");

                if (gestionItinerarioController != null) {
                    gestionItinerarioController.recargarTablaGestionItinerario();
                }

                if (esEdicion) {
                    botonRegistrar.getScene().getWindow().hide();
                } else {
                    limpiarFormulario();
                }
            } else {
                Alertas.error("Error", esEdicion
                        ? "No se pudo actualizar el itinerario."
                        : "No se pudo registrar el itinerario.");
            }
        }
    }

    private void recargarTabla() {
        if (gestionItinerarioController != null) {
            gestionItinerarioController.recargarTablaGestionItinerario();
        }
    }

    public void verItinerario(Itinerario it) {
        this.itinerarioActual = it;
        campoNombre.setText(it.getNombre());
        campoDescripcion.setText(it.getDescripcion());
        comboDuracion.setValue(String.valueOf(it.getDuracion()));
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
        comboDuracion.getSelectionModel().selectFirst();

        botonRegistrar.setText("Guardar");
    }

}
