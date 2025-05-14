/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasMovimientos;
import bbdd.ConsultasTipoAlojamiento;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.TipoAlojamiento;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarTipoAlojamientoController implements Initializable {

    @FXML
    private TextField campoTipo;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;

    /**
     * Initializes the controller class.
     */
    private TipoAlojamiento tipoActual;
    private boolean esEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    public void verTipoAlojamiento(TipoAlojamiento tipo) {
        this.tipoActual = tipo;
        campoTipo.setText(tipo.getTipo());
        esEdicion = true;
        botonRegistrar.setText("Actualizar");
    }

    public void setEdicionActiva(boolean editable) {
        campoTipo.setEditable(editable);
        botonRegistrar.setVisible(editable);
        campoTipo.setOpacity(editable ? 1.0 : 0.75);
    }

    private void cerrarVentana() {
        Stage stage = (Stage) botonRegistrar.getScene().getWindow();
        stage.close();
    }
    private GestionTipoDeAlojamientoController gestionTipoAlojamientoController;

    public void setGestionTipoAlojamientoController(GestionTipoDeAlojamientoController controller) {
        this.gestionTipoAlojamientoController = controller;
    }

    @FXML
    private void RegistrarTipoAlojamiento(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoTipo)) {
            Alertas.aviso("Campo vacío", "El tipo de alojamiento no puede estar vacío.");
            return;
        }

        String tipoTexto = campoTipo.getText().trim();

        if (!esEdicion && ConsultasTipoAlojamiento.existeTipoAlojamiento(tipoTexto)) {
            Alertas.aviso("Duplicado", "Ya existe un tipo de alojamiento con ese nombre.");
            return;
        }

        TipoAlojamiento tipo = new TipoAlojamiento(tipoTexto);
        boolean exito;

        if (esEdicion && tipoActual != null) {
            tipo.setIdTipo(tipoActual.getIdTipo());
            exito = ConsultasTipoAlojamiento.actualizarTipoAlojamiento(tipo);

            if (exito) {
                Conexion.conectar();
                ConsultasMovimientos.registrarMovimiento(
                        "Ha actualizado el tipo de alojamiento " + tipoTexto,
                        new Date(),
                        Usuario.getUsuarioActual().getIdUsuario()
                );

                Alertas.informacion("Tipo de Alojamiento actualizado exitosamente.");
                recargarTabla();
                cerrarVentana();
            } else {
                Alertas.error("Error en la actualización", "No se pudo actualizar el tipo de alojamiento.");
            }

        } else {
            exito = ConsultasTipoAlojamiento.registrarTipoAlojamiento(tipo);

            if (exito) {
                Conexion.conectar();
                ConsultasMovimientos.registrarMovimiento(
                        "Ha registrado el tipo de alojamiento " + tipoTexto,
                        new Date(),
                        Usuario.getUsuarioActual().getIdUsuario()
                );

                Alertas.informacion("Tipo de Alojamiento registrado exitosamente.");
                campoTipo.clear();
                recargarTabla();
            } else {
                Alertas.error("Error en el registro", "Ocurrió un error al registrar el tipo de alojamiento.");
            }
        }
    }

    private void recargarTabla() {
        if (gestionTipoAlojamientoController != null) {
            gestionTipoAlojamientoController.recargarTabla();
        }
    }
}
