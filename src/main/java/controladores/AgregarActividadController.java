/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasActividades;
import bbdd.ConsultasDestinos;
import bbdd.ConsultasMovimientos;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Actividad;
import modelo.Destino;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarActividadController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private ComboBox<Destino> comboDestino;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;

    /**
     * Initializes the controller class.
     */
    private Actividad actividadActual;
    private boolean esEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Conexion.conectar();
        ConsultasDestinos.cargarComboDestino(comboDestino);
        Conexion.cerrarConexion();

        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    private GestionActividadesController gestionActividadesController;

    public void setGestionActividadesController(GestionActividadesController controller) {
        this.gestionActividadesController = controller;
    }

    @FXML
    private void RegistrarActividad(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.aviso("Campo vacío", "La descripción no puede estar vacía.");
        } else if (comboDestino.getValue() == null) {
            Alertas.aviso("Combo vacío", "Debe seleccionar un destino.");
        } else {
            Destino destinoSeleccionado = comboDestino.getValue();

            if (esEdicion && actividadActual != null) {
                actividadActual.setNombre(campoNombre.getText());
                actividadActual.setDescripcion(campoDescripcion.getText());
                actividadActual.setIdDestino(destinoSeleccionado.getId_destino());

                if (ConsultasActividades.actualizarActividad(actividadActual)) {
                    Conexion.conectar();
                    ConsultasMovimientos.registrarMovimiento(
                            "Ha actualizado la actividad " + campoNombre.getText(),
                            new java.util.Date(),
                            Usuario.getUsuarioActual().getIdUsuario()
                    );
                    Alertas.informacion("Actividad actualizada exitosamente.");
                    recargarTabla();
                    cerrarVentana();
                } else {
                    Alertas.error("Error", "No se pudo actualizar la actividad.");
                }
            } else {
                Actividad actividad = new Actividad(
                        0,
                        campoNombre.getText(),
                        campoDescripcion.getText(),
                        destinoSeleccionado.getId_destino()
                );

                if (ConsultasActividades.registrarActividad(actividad)) {
                    Conexion.conectar();
                    ConsultasMovimientos.registrarMovimiento(
                            "Ha registrado la actividad " + campoNombre.getText(),
                            new java.util.Date(),
                            Usuario.getUsuarioActual().getIdUsuario()
                    );
                    Alertas.informacion("Actividad registrada exitosamente.");
                    recargarTabla();
                    limpiarFormulario();
                } else {
                    Alertas.error("Error en el registro", "No se pudo registrar la actividad.");
                }
            }
        }
    }

    private void recargarTabla() {
        if (gestionActividadesController != null) {
            gestionActividadesController.recargarTabla();
        }
    }

    public void verActividad(Actividad act) {
        this.actividadActual = act;
        campoNombre.setText(act.getNombre());
        campoDescripcion.setText(act.getDescripcion());

        for (Destino destino : comboDestino.getItems()) {
            if (destino.getId_destino() == act.getIdDestino()) {
                comboDestino.getSelectionModel().select(destino);
                break;
            }
        }

        this.esEdicion = true;
        botonRegistrar.setText("Actualizar");
    }

    public void setEdicionActiva(boolean editable) {
        campoNombre.setEditable(editable);
        campoDescripcion.setEditable(editable);
        comboDestino.setDisable(!editable);
        botonRegistrar.setVisible(editable);

        double opacidad = editable ? 1.0 : 0.75;
        campoNombre.setOpacity(opacidad);
        campoDescripcion.setOpacity(opacidad);
        comboDestino.setOpacity(opacidad);
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoDescripcion.clear();
        comboDestino.getSelectionModel().clearSelection();
        botonRegistrar.setText("Guardar");
    }

    private void cerrarVentana() {
        Stage stage = (Stage) botonRegistrar.getScene().getWindow();
        stage.close();
    }
}
