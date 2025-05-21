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
/**
 * Controlador para la vista AgregarActividad.fxml. Permite registrar y editar
 * actividades asociadas a destinos turísticos. Aplica validaciones, controla el
 * formulario y notifica los cambios.
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

    /**
     * Inicializa la vista. Carga los destinos en el ComboBox, establece un
     * valor por defecto y configura eventos.
     */
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

    /**
     * Establece el título visible del formulario.
     *
     * @param titulo Título que se mostrará.
     */
    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    /**
     * Asocia este controlador con el controlador de gestión para recargar
     * datos.
     *
     * @param controller Referencia al controlador de gestión de actividades.
     */
    public void setGestionActividadesController(GestionActividadesController controller) {
        this.gestionActividadesController = controller;
    }

    /**
     * Registra o actualiza una actividad según el modo actual. Valida campos
     * obligatorios y registra notificaciones.
     */
    @FXML
    private void RegistrarActividad(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.error("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.error("Campo vacío", "La descripción no puede estar vacía.");
        } else if (destinoSeleccionado == null) {
            Alertas.error("Selección inválida", "Debe seleccionar un destino válido.");
        } else {
            int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

            if (esEdicion && actividadActual != null) {
                actividadActual.setNombre(campoNombre.getText());
                actividadActual.setDescripcion(campoDescripcion.getText());
                actividadActual.setIdDestino(destinoSeleccionado.getId_destino());

                if (ConsultasActividades.actualizarActividad(actividadActual)) {
                    Conexion.conectar();
                    String mensaje = "Ha actualizado la actividad " + campoNombre.getText().trim();

                    ConsultasNotificaciones.registrarMovimiento(
                            mensaje,
                            new java.util.Date(),
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

                    ConsultasNotificaciones.registrarMovimiento(
                            mensaje,
                            new java.util.Date(),
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

    /**
     * Recarga la tabla en el controlador de gestión si está definido.
     */
    private void recargarTabla() {
        if (gestionActividadesController != null) {
            gestionActividadesController.recargarTabla();
        }
    }

    /**
     * Carga los datos de una actividad existente en el formulario para
     * visualización o edición.
     *
     * @param act Actividad a mostrar.
     */
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

    /**
     * Cambia la habilitación de los campos según si está en modo edición o solo
     * vista.
     *
     * @param editable true para edición, false para solo lectura.
     */
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

    /**
     * Limpia los campos del formulario y restablece la selección por defecto.
     */
    private void limpiarFormulario() {
        campoNombre.clear();
        campoDescripcion.clear();
        comboDestino.getSelectionModel().selectFirst();
        destinoSeleccionado = null;
        botonRegistrar.setText("Registrar");

    }

    /**
     * Cierra la ventana actual del formulario.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) botonRegistrar.getScene().getWindow();
        stage.close();
    }
}
