package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import Utilidades.validarEmail;
import Utilidades.validarTelefono;
import bbdd.Conexion;
import bbdd.ConsultasMovimientos;
import bbdd.ConsultasNotificaciones;
import bbdd.ConsultasUsuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Usuario;

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

    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    public void setAdministracionUsuarioController(GestionUsuarioController controller) {
        this.administracionUsuarioController = controller;
    }

    public void setLoginController(LoginController controller) {
        this.loginController = controller;
    }

    public void setAbiertoDesdeLogin(boolean valor) {
        this.abiertoDesdeLogin = valor;
    }

    public void setEdicionActiva(boolean activo) {
        this.edicionActiva = activo;
        botonRegistrar.setText(activo ? "Actualizar" : "Registrar");
    }

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

    @FXML
    private void registrar(ActionEvent event) {
        Conexion.conectar();

        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoTelefono)) {
            Alertas.aviso("Campo vacío", "El teléfono no puede estar vacío.");
        } else if (!Utilidades.validarTelefono.esSoloNumeros(campoTelefono.getText().trim())) {
            Alertas.aviso("Teléfono inválido", "El teléfono solo debe contener números.");
            campoTelefono.clear();
        } else if (!Utilidades.validarTelefono.esTelefonoNicaraguenseValido(campoTelefono.getText().trim())) {
            Alertas.aviso("Teléfono inválido", "El teléfono debe tener 8 dígitos y empezar por 2, 5, 7 u 8 (formato nicaragüense).");
            campoTelefono.clear();
        } else if (compruebaCampo.compruebaVacio(campoEmail)) {
            Alertas.aviso("Campo vacío", "El email no puede estar vacío.");
        } else if (!validarEmail.esEmailValido(campoEmail.getText())) {
            Alertas.aviso("Email inválido", "Ingrese un correo electrónico válido.");
            campoEmail.clear();
        } else if (compruebaCampo.compruebaVacio(campoContrasena)) {
            Alertas.aviso("Campo vacío", "La contraseña no puede estar vacía.");
        } else if (campoTipoCompania.getValue() == null || campoTipoCompania.getValue().equals("Seleccione")) {
            Alertas.aviso("Combo vacío", "Debe seleccionar un tipo de viajero.");
        } else if (campoTipoUsuario.getValue() == null || campoTipoUsuario.getValue().equals("Seleccione")) {
            Alertas.aviso("Combo vacío", "Debe seleccionar un tipo de usuario.");
        } else if (campoIdioma.getValue() == null || campoIdioma.getValue().equals("Seleccione")) {
            Alertas.aviso("Combo vacío", "Debe seleccionar un idioma.");
        } else {
            String emailLimpio = campoEmail.getText().trim().toLowerCase();
            String telefonoLimpio = campoTelefono.getText().replaceAll("[^0-9]", "");

            if (usuarioEnEdicion == null) {
                if (ConsultasUsuario.existeEmail(emailLimpio)) {
                    Alertas.aviso("Email duplicado", "El correo ya está registrado. Use otro diferente.");
                    campoEmail.clear();
                    Conexion.cerrarConexion();
                    return;
                }
            } else {
                if (!emailLimpio.equalsIgnoreCase(usuarioEnEdicion.getEmail())
                        && ConsultasUsuario.existeEmail(emailLimpio)) {
                    Alertas.aviso("Email duplicado", "Este correo ya está registrado.");
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

                    ConsultasMovimientos.registrarMovimiento(
                            mensaje,
                            new java.sql.Date(System.currentTimeMillis()),
                            idUsuario
                    );

                    ConsultasNotificaciones.registrarNotificacion(
                            mensaje,
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

                    ConsultasMovimientos.registrarMovimiento(
                            mensaje,
                            new java.sql.Date(System.currentTimeMillis()),
                            idUsuario
                    );

                    ConsultasNotificaciones.registrarNotificacion(
                            mensaje,
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

    public boolean getModificado() {
        return modificado;
    }

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
