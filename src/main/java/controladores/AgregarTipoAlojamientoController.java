/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasNotificaciones;
import bbdd.ConsultasTipoAlojamiento;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.TipoAlojamiento;
import modelo.Usuario;

/**
 * Controlador JavaFX para el formulario de registro y edición de tipos de
 * alojamiento. Permite registrar un nuevo tipo o actualizar uno existente,
 * validando el campo de texto y registrando el movimiento en la base de datos.
 *
 * Autor: k0343
 */
public class AgregarTipoAlojamientoController implements Initializable {

    @FXML
    private TextField campoTipo;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;
    @FXML
    private Label labelTitulo;

    private TipoAlojamiento tipoActual;
    private boolean esEdicion = false;
    private GestionTipoDeAlojamientoController gestionTipoAlojamientoController;

    /**
     * Inicializa la interfaz cargando la imagen de encabezado.
     *
     * @param url URL de inicialización (no utilizada directamente).
     * @param rb ResourceBundle para internacionalización (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    /**
     * Establece el título de la ventana en la etiqueta superior.
     *
     * @param titulo Texto que se mostrará como título del formulario.
     */
    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    /**
     * Asocia el controlador de gestión de tipos de alojamiento para permitir
     * recargar la tabla tras registrar o actualizar un tipo.
     *
     * @param controller Controlador padre que maneja la vista principal.
     */
    public void setGestionTipoAlojamientoController(GestionTipoDeAlojamientoController controller) {
        this.gestionTipoAlojamientoController = controller;
    }

    /**
     * Carga los datos de un tipo de alojamiento existente para su edición.
     *
     * @param tipo Objeto TipoAlojamiento a editar.
     */
    public void verTipoAlojamiento(TipoAlojamiento tipo) {
        this.tipoActual = tipo;
        campoTipo.setText(tipo.getTipo());
        esEdicion = true;
        botonRegistrar.setText("Actualizar");
    }

    /**
     * Activa o desactiva la edición del campo de texto y visibilidad del botón.
     *
     * @param editable true para permitir edición, false para solo
     * visualización.
     */
    public void setEdicionActiva(boolean editable) {
        campoTipo.setEditable(editable);
        botonRegistrar.setVisible(editable);
        campoTipo.setOpacity(editable ? 1.0 : 0.75);
    }

    /**
     * Maneja el evento del botón de registrar o actualizar tipo de alojamiento.
     * Realiza validaciones, registra en la base de datos y notifica el
     * movimiento.
     *
     * @param event Evento disparado por el botón de acción.
     */
    @FXML
    private void RegistrarTipoAlojamiento(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoTipo)) {
            Alertas.error("Campo vacío", "El tipo de alojamiento no puede estar vacío.");
            return;
        }

        String tipoTexto = campoTipo.getText().trim();

        if (!esEdicion && ConsultasTipoAlojamiento.existeTipoAlojamiento(tipoTexto)) {
            Alertas.error("Tipo duplicado", "Ya existe un tipo de alojamiento con ese nombre.");
            campoTipo.clear();
            return;
        }

        TipoAlojamiento tipo = new TipoAlojamiento(tipoTexto);
        boolean exito;

        if (esEdicion && tipoActual != null) {
            tipo.setIdTipo(tipoActual.getIdTipo());
            exito = ConsultasTipoAlojamiento.actualizarTipoAlojamiento(tipo);

            if (exito) {
                Conexion.conectar();
                String mensaje = "Ha actualizado el tipo de alojamiento " + tipoTexto;
                int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

                ConsultasNotificaciones.registrarMovimiento(
                        mensaje,
                        new Date(),
                        idUsuario
                );

                Conexion.cerrarConexion();
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

                String mensaje = "Ha registrado el tipo de alojamiento " + tipoTexto;
                int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

                ConsultasNotificaciones.registrarMovimiento(
                        mensaje,
                        new Date(),
                        idUsuario
                );

                Conexion.cerrarConexion();

                Alertas.informacion("Tipo de Alojamiento registrado exitosamente.");
                campoTipo.clear();
                recargarTabla();
            } else {
                Alertas.error("Error en el registro", "Ocurrió un error al registrar el tipo de alojamiento.");
            }
        }
    }

    /**
     * Recarga la tabla de tipos de alojamiento si el controlador principal está
     * asignado.
     */
    private void recargarTabla() {
        if (gestionTipoAlojamientoController != null) {
            gestionTipoAlojamientoController.recargarTabla();
        }
    }

    /**
     * Cierra la ventana actual del formulario.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) botonRegistrar.getScene().getWindow();
        stage.close();
    }
}
