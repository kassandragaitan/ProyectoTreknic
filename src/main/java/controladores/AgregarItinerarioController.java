/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasItinerario;
import bbdd.ConsultasNotificaciones;
import java.net.URL;
import java.util.Date;
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
import modelo.Itinerario;
import modelo.Usuario;

/**
 * Controlador JavaFX para el formulario de registro y edición de itinerarios
 * turísticos. Permite crear o modificar itinerarios con nombre, descripción y
 * duración. Registra movimientos en el sistema y actualiza la vista principal
 * tras los cambios.
 *
 * Autor: k0343
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
     * Inicializa la vista cargando las duraciones en el ComboBox y la imagen
     * del encabezado.
     *
     * @param url URL de inicialización (no usada).
     * @param rb ResourceBundle para localización (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Conexion.conectar();
        ConsultasItinerario.cargarComboDuracionItinerario(comboDuracion);
        Conexion.cerrarConexion();

        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    /**
     * Establece el título del formulario en la interfaz.
     *
     * @param titulo Texto a mostrar como título.
     */
    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    /**
     * Asocia el controlador de gestión de itinerarios para permitir la recarga
     * de tabla.
     *
     * @param controller Controlador de la vista principal de itinerarios.
     */
    public void setGestionItinerarioController(GestionItinerarioController controller) {
        this.gestionItinerarioController = controller;
    }

    /**
     * Indica si el formulario produjo una modificación (registro o
     * actualización).
     *
     * @return true si hubo cambios, false en caso contrario.
     */
    public boolean getModificado() {
        return modificado;
    }

    /**
     * Maneja el evento del botón "Registrar" o "Actualizar". Valida los campos,
     * inserta o actualiza el itinerario en la base de datos, registra la
     * notificación y actualiza la tabla de itinerarios si corresponde.
     *
     * @param event Evento disparado por la acción del botón.
     */
    @FXML
    private void RegistrarItinerario(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.error("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.error("Campo vacío", "La descripción no puede estar vacía.");
        } else if (comboDuracion.getValue() == null || comboDuracion.getValue().equals("Seleccione")) {
            Alertas.error("Selección inválida", "Debe seleccionar una duración válida.");
        } else if (!esEdicion && ConsultasItinerario.existeItinerarioConNombre(campoNombre.getText())) {
            Alertas.error("Itinerario duplicado", "Ya existe un itinerario con ese nombre.");
            campoNombre.clear();
        } else {
            Conexion.conectar();
            String duracionSeleccionada = comboDuracion.getValue();
            boolean exito;
            int idUsuario = Usuario.getUsuarioActual().getIdUsuario();
            String mensaje;

            if (esEdicion && itinerarioActual != null) {
                itinerarioActual.setNombre(campoNombre.getText());
                itinerarioActual.setDescripcion(campoDescripcion.getText());
                itinerarioActual.setDuracion(duracionSeleccionada);

                exito = ConsultasItinerario.actualizarItinerario(itinerarioActual);
                mensaje = "Ha actualizado el itinerario " + campoNombre.getText().trim();
            } else {
                Itinerario nuevo = new Itinerario();
                nuevo.setNombre(campoNombre.getText());
                nuevo.setDescripcion(campoDescripcion.getText());
                nuevo.setDuracion(duracionSeleccionada);
                nuevo.setFechaCreacion(new Date());
                nuevo.setIdUsuario(idUsuario);

                exito = ConsultasItinerario.registrarItinerario(nuevo);
                mensaje = "Ha registrado el itinerario " + campoNombre.getText().trim();
            }

            if (exito) {
                ConsultasNotificaciones.registrarMovimiento(
                        mensaje,
                        new java.sql.Date(System.currentTimeMillis()),
                        idUsuario
                );
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

    /**
     * Carga los datos de un itinerario existente en los campos del formulario
     * para su visualización o edición.
     *
     * @param it Objeto Itinerario que se desea visualizar o editar.
     */
    public void verItinerario(Itinerario it) {
        this.itinerarioActual = it;
        campoNombre.setText(it.getNombre());
        campoDescripcion.setText(it.getDescripcion());
        comboDuracion.setValue(String.valueOf(it.getDuracion()));
        this.esEdicion = true;
        botonRegistrar.setText("Actualizar");
    }

    /**
     * Activa o desactiva la edición de campos del formulario según el contexto.
     *
     * @param editable true para permitir edición, false para solo
     * visualización.
     */
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

    /**
     * Limpia todos los campos del formulario y restablece el botón a
     * "Registrar".
     */
    private void limpiarFormulario() {
        campoNombre.clear();
        campoDescripcion.clear();
        comboDuracion.getSelectionModel().selectFirst();
        botonRegistrar.setText("Registrar");
    }

}
