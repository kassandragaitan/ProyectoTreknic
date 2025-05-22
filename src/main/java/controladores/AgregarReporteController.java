/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.ConsultasNotificaciones;
import bbdd.ConsultasReportes;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Reporte;
import modelo.Usuario;

/**
 * Controlador JavaFX para el formulario de registro de reportes del sistema.
 * Permite registrar un nuevo reporte con tipo y descripción, asociando la
 * acción al usuario actual y registrando la notificación correspondiente.
 *
 * Autor: k0343
 */
public class AgregarReporteController implements Initializable {

    @FXML
    private ComboBox<String> comboTipoReporte;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;
    private boolean esEdicion = false;
    private String tipoReporteOriginal;
    private String tipoSeleccionado;

    /**
     * Inicializa el controlador cargando los tipos de reporte en el ComboBox y
     * asignando la imagen del encabezado. Configura el evento de selección del
     * tipo de reporte.
     *
     * @param url URL de inicialización (no utilizada directamente).
     * @param rb ResourceBundle para internacionalización (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imagenTrekNic.setImage(new Image(
                getClass().getResourceAsStream("/img/Encabezado.png")
        ));

        comboTipoReporte.getItems().add("Seleccione");
        comboTipoReporte.getItems().addAll(
                "Usuarios registrados",
                "Tipos de alojamiento"
        );
        comboTipoReporte.getSelectionModel().selectFirst();
        tipoSeleccionado = null;
        comboTipoReporte.setOnAction(e -> {
            String sel = comboTipoReporte.getSelectionModel().getSelectedItem();
            if (sel != null && !sel.equals("Seleccione")) {
                tipoSeleccionado = sel;
            } else {
                tipoSeleccionado = null;
            }
        });
    }

    /**
     * Registra un nuevo reporte en el sistema si los campos son válidos.
     * También se registra una notificación con el movimiento realizado.
     *
     * @param event Evento de acción generado por el botón de registro.
     */
    @FXML
    private void RegistrarReporte(ActionEvent event) {
        if (tipoSeleccionado == null) {
            Alertas.error("Selección inválida", "Debe seleccionar un tipo de reporte.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.error("Campo vacío", "La descripción no puede estar vacía.");
        } else {
            Reporte nuevo = new Reporte();
            nuevo.setTipo(tipoSeleccionado);
            nuevo.setDescripcion(campoDescripcion.getText().trim());
            nuevo.setFecha(new java.util.Date());
            nuevo.setIdUsuario(Usuario.getUsuarioActual().getIdUsuario());

            if (ConsultasReportes.registrarReporte(nuevo)) {
                String mensaje = "Se ha registrado un reporte de tipo: " + tipoSeleccionado;
                int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

                ConsultasNotificaciones.registrarMovimiento(
                        mensaje,
                        new java.sql.Date(System.currentTimeMillis()),
                        idUsuario
                );

                Alertas.informacion("Reporte registrado exitosamente.");
                limpiarFormulario();
            } else {
                Alertas.error("Error", "No se pudo registrar el reporte.");
            }
        }
    }

    /**
     * Limpia los campos del formulario, restableciendo los valores por defecto
     * del ComboBox y vaciando la descripción.
     */
    private void limpiarFormulario() {
        comboTipoReporte.getSelectionModel().selectFirst();
        tipoSeleccionado = null;
        campoDescripcion.clear();
    }
}
