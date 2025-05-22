package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import Utilidades.validarEmail;
import bbdd.Conexion;
import bbdd.ConsultasNotificaciones;
import bbdd.ConsultasUsuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 * Controlador JavaFX para el formulario de registro y edición de usuarios.
 * Permite registrar nuevos usuarios o actualizar existentes, validando todos
 * los campos, evitando duplicados y registrando las acciones en el sistema de
 * notificaciones.
 *
 * Autor: k0343
 */
public class AgregarUsuarioController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoTelefono;
    @FXML
    private TextField campoEmail;
    @FXML
    private TextField campoContrasena;
    @FXML
    private ComboBox<String> campoIdioma;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ComboBox<String> campoTipoUsuario;
    @FXML
    private ComboBox<String> campoTipoCompania;
    @FXML
    private ImageView imagenTrekNic;
    @FXML
    private Label labelTitulo;

    private boolean edicionActiva = false;
    private Usuario usuarioActual;
    private Usuario usuarioEnEdicion = null;
    private GestionUsuarioController administracionUsuarioController;
    private boolean abiertoDesdeLogin = false;
    private boolean modificado = false;
    private LoginController loginController;

    /**
     * Inicializa el formulario cargando las listas de idiomas, tipos de usuario
     * y tipos de viajero. También carga la imagen del encabezado institucional.
     *
     * @param url URL de inicialización (no usada).
     * @param rb ResourceBundle para localización (no usado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Conexion.conectar();
        ConsultasUsuario.cargarComboIdioma(campoIdioma);
        ConsultasUsuario.cargarComboTipoUsuario(campoTipoUsuario);
        ConsultasUsuario.cargarComboTipoCompania(campoTipoCompania);
        Conexion.cerrarConexion();
        campoTipoUsuario.getItems().remove("Admin");
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    /**
     * Establece el título mostrado en el formulario.
     *
     * @param titulo Texto del título.
     */
    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    /**
     * Asocia el controlador principal de administración de usuarios para
     * recargar la tabla.
     *
     * @param controller Controlador de gestión de usuarios.
     */
    public void setAdministracionUsuarioController(GestionUsuarioController controller) {
        this.administracionUsuarioController = controller;
    }

    /**
     * Asocia el controlador de login para cerrar el formulario tras un registro
     * desde el login.
     *
     * @param controller Controlador de login.
     */
    public void setLoginController(LoginController controller) {
        this.loginController = controller;
    }

    /**
     * Indica si el formulario se ha abierto desde el login.
     *
     * @param valor true si se abrió desde el login.
     */
    public void setAbiertoDesdeLogin(boolean valor) {
        this.abiertoDesdeLogin = valor;
    }

    /**
     * Activa o desactiva el modo edición.
     *
     * @param activo true para editar un usuario, false para registrar.
     */
    public void setEdicionActiva(boolean activo) {
        this.edicionActiva = activo;
        botonRegistrar.setText(activo ? "Actualizar" : "Registrar");
    }

    /**
     * Carga los datos de un usuario existente para su visualización o edición.
     *
     * @param usuario Usuario a mostrar en el formulario.
     */
    public void verUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        this.usuarioEnEdicion = usuario;

        campoNombre.setText(usuario.getNombre());
        campoEmail.setText(usuario.getEmail());
        campoTelefono.setText(usuario.getTelefono());
        campoContrasena.setText(usuario.getContrasena() != null ? usuario.getContrasena() : "");
        campoTipoUsuario.setValue(usuario.getTipoUsuario());
        campoIdioma.setValue(usuario.getIdioma());
        campoTipoCompania.setValue(usuario.getTipoViajero());
    }

    /**
     * Habilita o deshabilita los campos del formulario según el contexto.
     *
     * @param editable true para permitir edición, false para solo
     * visualización.
     */
    public void EditarCamposUsuario(boolean editable) {
        campoNombre.setDisable(!editable);
        campoTelefono.setDisable(!editable);
        campoEmail.setDisable(!editable);
        campoContrasena.setDisable(!editable);
        campoIdioma.setDisable(!editable);
        campoTipoUsuario.setDisable(!editable);
        campoTipoCompania.setDisable(!editable);
        botonRegistrar.setDisable(!editable);
    }

    /**
     * Maneja el evento del botón Registrar o Actualizar. Valida todos los
     * campos, verifica si el correo ya existe y guarda los datos. También
     * registra la acción como notificación y actualiza la vista de usuarios.
     *
     * @param event Evento generado al presionar el botón.
     */
    @FXML
    private void registrar(ActionEvent event) {
        Conexion.conectar();

        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.error("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoTelefono)) {
            Alertas.error("Campo vacío", "El teléfono no puede estar vacío.");
        } else if (!Utilidades.validarTelefono.esSoloNumeros(campoTelefono.getText().trim())) {
            Alertas.error("Formato inválido", "El teléfono solo debe contener números.");
            campoTelefono.clear();
        } else if (!Utilidades.validarTelefono.esTelefonoNicaraguenseValido(campoTelefono.getText().trim())) {
            Alertas.error("Formato inválido", "El teléfono debe tener 8 dígitos y empezar por 2, 5, 7 u 8 (formato nicaragüense).");
            campoTelefono.clear();
        } else if (compruebaCampo.compruebaVacio(campoEmail)) {
            Alertas.error("Campo vacío", "El email no puede estar vacío.");
        } else if (!validarEmail.esEmailValido(campoEmail.getText())) {
            Alertas.error("Email inválido", "Ingrese un correo electrónico válido.");
            campoEmail.clear();
        } else if (compruebaCampo.compruebaVacio(campoContrasena)) {
            Alertas.error("Campo vacío", "La contraseña no puede estar vacía.");
        } else if (campoTipoCompania.getValue() == null || campoTipoCompania.getValue().equals("Seleccione")) {
            Alertas.error("Selección inválida", "Debe seleccionar un tipo de viajero.");
        } else if (campoTipoUsuario.getValue() == null || campoTipoUsuario.getValue().equals("Seleccione")) {
            Alertas.error("Selección inválida", "Debe seleccionar un tipo de usuario.");
        } else if (campoIdioma.getValue() == null || campoIdioma.getValue().equals("Seleccione")) {
            Alertas.error("Selección inválida", "Debe seleccionar un idioma.");
        } else {
            String emailLimpio = campoEmail.getText().trim().toLowerCase();
            String telefonoLimpio = campoTelefono.getText().replaceAll("[^0-9]", "");

            if (usuarioEnEdicion == null) {
                if (ConsultasUsuario.existeEmail(emailLimpio)) {
                    Alertas.error("Email duplicado", "El correo ya está registrado. Use otro diferente.");
                    campoEmail.clear();
                    Conexion.cerrarConexion();
                    return;
                }
            } else {
                if (!emailLimpio.equalsIgnoreCase(usuarioEnEdicion.getEmail())
                        && ConsultasUsuario.existeEmail(emailLimpio)) {
                    Alertas.error("Email duplicado", "Este correo ya está registrado.");
                    campoEmail.clear();
                    Conexion.cerrarConexion();
                    return;
                }
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(campoNombre.getText());
            usuario.setTelefono(telefonoLimpio);
            usuario.setEmail(campoEmail.getText());
            usuario.setContrasena(campoContrasena.getText());
            usuario.setTipoViajero(campoTipoCompania.getValue());
            usuario.setIdioma(campoIdioma.getValue());
            usuario.setTipoUsuario(campoTipoUsuario.getValue());

            boolean resultado;
            if (usuarioEnEdicion == null) {
                resultado = ConsultasUsuario.registrarUsuario(usuario);
                if (resultado) {
                    String mensaje = "Ha registrado el usuario \"" + usuario.getNombre() + "\"";
                    int idUsuario = Usuario.getUsuarioActual() != null ? Usuario.getUsuarioActual().getIdUsuario() : 0;

                    ConsultasNotificaciones.registrarMovimiento(
                            mensaje,
                            new java.sql.Date(System.currentTimeMillis()),
                            idUsuario
                    );
                    Alertas.informacion("Usuario registrado exitosamente.");
                } else {
                    Alertas.error("Error en el registro", "Ocurrió un error al registrar el usuario.");
                }
            } else {
                usuario.setIdUsuario(usuarioEnEdicion.getIdUsuario());
                resultado = ConsultasUsuario.actualizarUsuario(usuario);
                if (resultado) {
                    String mensaje = "Ha actualizado el usuario \"" + usuario.getNombre() + "\"";
                    int idUsuario = Usuario.getUsuarioActual() != null ? Usuario.getUsuarioActual().getIdUsuario() : 0;

                    ConsultasNotificaciones.registrarMovimiento(
                            mensaje,
                            new java.sql.Date(System.currentTimeMillis()),
                            idUsuario
                    );
                    Alertas.informacion("Usuario actualizado correctamente.");
                } else {
                    Alertas.error("Error al actualizar", "Ocurrió un error al actualizar el usuario.");
                }
            }

            if (resultado) {
                modificado = true;
                if (administracionUsuarioController != null) {
                    administracionUsuarioController.recargarTabla();
                }

                if (usuarioEnEdicion == null) {
                    limpiarFormulario();
                    if (abiertoDesdeLogin) {
                        if (loginController != null) {
                            loginController.limpiarCamposLogin();
                        }
                        Stage ventanaActual = (Stage) botonRegistrar.getScene().getWindow();
                        ventanaActual.close();
                    }
                } else {
                    Stage stage = (Stage) botonRegistrar.getScene().getWindow();
                    stage.close();
                }
            }

            Conexion.cerrarConexion();
        }
    }

    /**
     * Devuelve si se ha realizado una modificación (registro o actualización).
     *
     * @return true si se modificó, false en caso contrario.
     */
    public boolean getModificado() {
        return modificado;
    }

    /**
     * Limpia todos los campos del formulario y restablece los ComboBox a sus
     * valores por defecto.
     */
    private void limpiarFormulario() {
        campoNombre.clear();
        campoTelefono.clear();
        campoEmail.clear();
        campoContrasena.clear();
        campoTipoUsuario.getSelectionModel().selectFirst();
        campoTipoCompania.getSelectionModel().selectFirst();
        campoIdioma.getSelectionModel().selectFirst();
    }
}
