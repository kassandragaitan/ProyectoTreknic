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
import bbdd.ConsultasNotificaciones;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    @FXML
    private Label labelTitulo;
    /**
     * Initializes the controller class.
     */
    private Actividad actividadActual;
    private boolean esEdicion = false;
    private Destino destinoSeleccionado;
    private GestionActividadesController gestionActividadesController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imagenTrekNic.setImage(new Image(getClass().getResourceAsStream("/img/Encabezado.png")));

        Conexion.conectar();
        ConsultasDestinos.cargarComboDestino(comboDestino);
        Conexion.cerrarConexion();

        Destino placeholder = new Destino();
        placeholder.setId_destino(-1);
        placeholder.setNombre("Seleccione");
        comboDestino.getItems().add(0, placeholder);

        comboDestino.getSelectionModel().selectFirst();
        destinoSeleccionado = null;

        comboDestino.setOnAction(e -> {
            Destino sel = comboDestino.getSelectionModel().getSelectedItem();
            if (sel != null && sel.getId_destino() != -1) {
                destinoSeleccionado = sel;
            } else {
                destinoSeleccionado = null;
            }
        });
    }

    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    public void setGestionActividadesController(GestionActividadesController controller) {
        this.gestionActividadesController = controller;
    }

    @FXML
    private void RegistrarActividad(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.error("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.error("Campo vacío", "La descripción no puede estar vacía.");
        } else if (destinoSeleccionado == null) {
            Alertas.error("Selección inválida", "Debe seleccionar un destino válido.");
        } else {
            Destino destinoSeleccionado = comboDestino.getValue();
            int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

            if (esEdicion && actividadActual != null) {
                actividadActual.setNombre(campoNombre.getText());
                actividadActual.setDescripcion(campoDescripcion.getText());
                actividadActual.setIdDestino(destinoSeleccionado.getId_destino());

                if (ConsultasActividades.actualizarActividad(actividadActual)) {
                    Conexion.conectar();
                    String mensaje = "Ha actualizado la actividad " + campoNombre.getText().trim();

                    ConsultasMovimientos.registrarMovimiento(
                            mensaje,
                            new java.util.Date(),
                            idUsuario
                    );

                    ConsultasNotificaciones.registrarNotificacion(
                            mensaje,
                            idUsuario
                    );

                    Conexion.cerrarConexion();

                    Alertas.informacion("Actividad actualizada exitosamente.");
                    recargarTabla();
                    cerrarVentana();
                } else {
                    Alertas.error("Error", "No se pudo actualizar la actividad.");
                }
            } else {
                if (ConsultasActividades.existeActividadConNombreYDestino(
                        campoNombre.getText().trim(),
                        destinoSeleccionado.getId_destino()
                )) {
                    Alertas.error("Actividad duplicada", "Ya existe una actividad con ese nombre para el destino seleccionado.");
                    campoNombre.clear();
                    return;
                }

                Actividad actividad = new Actividad(
                        0,
                        campoNombre.getText().trim(),
                        campoDescripcion.getText().trim(),
                        destinoSeleccionado.getId_destino()
                );

                if (ConsultasActividades.registrarActividad(actividad)) {
                    Conexion.conectar();
                    String mensaje = "Ha registrado la actividad " + campoNombre.getText().trim();

                    ConsultasMovimientos.registrarMovimiento(
                            mensaje,
                            new java.util.Date(),
                            idUsuario
                    );

                    ConsultasNotificaciones.registrarNotificacion(
                            mensaje,
                            idUsuario
                    );

                    Conexion.cerrarConexion();

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
        for (Destino dest : comboDestino.getItems()) {
            if (dest.getId_destino() == act.getIdDestino()) {
                comboDestino.getSelectionModel().select(dest);
                destinoSeleccionado = dest;
                break;
            }
        }
        esEdicion = true;
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
        comboDestino.getSelectionModel().selectFirst();
        destinoSeleccionado = null;
        botonRegistrar.setText("Registrar");

    }

    private void cerrarVentana() {
        Stage stage = (Stage) botonRegistrar.getScene().getWindow();
        stage.close();
    }
}
