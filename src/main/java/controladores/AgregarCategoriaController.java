package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasCategoria;
import bbdd.ConsultasNotificaciones;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Categoria;
import modelo.Usuario;

/**
 * Controlador JavaFX para el formulario de creación y edición de categorías.
 * Permite registrar o actualizar categorías turísticas, validando los campos y
 * registrando movimientos en el sistema.
 *
 * Autor: k0343
 */
public class AgregarCategoriaController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;
    @FXML
    private Label labelTitulo;

    private GestionCategoriaController gestionCategoriaController;
    private Categoria categoriaActual;
    private boolean esEdicion = false;
    private boolean modificado = false;

    /**
     * Inicializa la vista cargando la imagen de encabezado.
     *
     * @param url URL utilizada para inicialización (no usada directamente).
     * @param rb ResourceBundle para internacionalización (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    /**
     * Establece el título del formulario.
     *
     * @param titulo Título que se mostrará en el encabezado.
     */
    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    /**
     * Indica si se ha modificado (creado o editado) una categoría.
     *
     * @return true si hubo cambios, false si no.
     */
    public boolean getModificado() {
        return modificado;
    }

    /**
     * Establece el controlador padre para permitir recargar la tabla al guardar
     * cambios.
     *
     * @param controller Controlador de gestión de categorías.
     */
    public void setGestionCategoriaController(GestionCategoriaController controller) {
        this.gestionCategoriaController = controller;
    }

    /**
     * Carga una categoría existente en el formulario para visualizar o editar.
     *
     * @param cat Categoría que se desea visualizar o actualizar.
     */
    public void verCategoria(Categoria cat) {
        this.categoriaActual = cat;
        this.esEdicion = true;
        campoNombre.setText(cat.getNombre());
        campoDescripcion.setText(cat.getDescripcion());
        botonRegistrar.setText("Actualizar");
    }

    /**
     * Activa o desactiva la edición de campos del formulario.
     *
     * @param editable true para habilitar campos, false para solo
     * visualización.
     */
    public void setEdicionActiva(boolean editable) {
        campoNombre.setEditable(editable);
        campoDescripcion.setEditable(editable);
        botonRegistrar.setVisible(editable);

        double op = editable ? 1.0 : 0.75;
        campoNombre.setOpacity(op);
        campoDescripcion.setOpacity(op);
    }

    /**
     * Registra o actualiza una categoría, validando los campos. Guarda el
     * registro en la base de datos y crea una notificación del movimiento.
     *
     * @param event Evento de acción del botón de registro o actualización.
     */
    @FXML
    private void RegistrarCategoria(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.error("Campo vacío", "El nombre no puede estar vacío.");
            return;
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.error("Campo vacío", "La descripción no puede estar vacía.");
            return;
        }
        String nombre = campoNombre.getText().trim();
        String descripcion = campoDescripcion.getText().trim();

        if (!esEdicion && ConsultasCategoria.existeCategoria(nombre)) {
            Alertas.error("Categoría duplicada", "Ya existe una categoría con ese nombre.");
            campoNombre.clear();
            return;
        }

        boolean exito;
        if (esEdicion) {
            categoriaActual.setNombre(nombre);
            categoriaActual.setDescripcion(descripcion);
            exito = ConsultasCategoria.actualizarCategoria(categoriaActual);
        } else {
            Categoria nueva = new Categoria(nombre, descripcion);
            exito = ConsultasCategoria.registrarCategoria(nueva);
        }

        if (exito) {
            Conexion.conectar();
            String accion = esEdicion
                    ? "Ha actualizado la categoría " + nombre
                    : "Ha registrado la categoría " + nombre;
            ConsultasNotificaciones.registrarMovimiento(
                    accion,
                    Date.valueOf(LocalDate.now()),
                    Usuario.getUsuarioActual().getIdUsuario()
            );
            Conexion.cerrarConexion();

            Alertas.informacion(esEdicion
                    ? "Categoría actualizada correctamente."
                    : "Categoría registrada exitosamente."
            );

            modificado = true;

            if (gestionCategoriaController != null) {
                gestionCategoriaController.recargarTabla();
            }

            if (esEdicion) {
                ((Stage) botonRegistrar.getScene().getWindow()).close();
            } else {
                campoNombre.clear();
                campoDescripcion.clear();
            }

        } else {
            Alertas.error("Error", esEdicion
                    ? "No se pudo actualizar la categoría."
                    : "No se pudo registrar la categoría."
            );
        }

    }
}
