package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.ConsultasLogin;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import modelo.Usuario;

/**
 * Controlador para la vista de inicio de sesión del sistema Treknic.
 * <p>
 * Permite a los usuarios autenticarse mediante correo electrónico y contraseña,
 * acceder a la interfaz de administración si son administradores, o navegar a
 * las ventanas de registro o recuperación de contraseña.
 * </p>
 * Solo los usuarios con rol "Admin" pueden acceder al sistema.
 *
 * @author k0343
 */
public class LoginController implements Initializable {

    @FXML
    private Button botonAcceder;
    @FXML
    private TextField campoUsuario;
    @FXML
    private PasswordField campoContrasena;
    @FXML
    private Label loginlabel;
    @FXML
    private Button botonOlvidar;
    @FXML
    private Button botonRegistro;

    /**
     * Inicializa los elementos interactivos del formulario de login.
     * <p>
     * Configura el cursor como mano sobre los botones de "¿Olvidaste tu
     * contraseña?" y "Registrarse".
     *
     * @param url Ubicación del archivo FXML.
     * @param rb Recursos internacionalizados (no utilizado en esta
     * implementación).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botonOlvidar.setCursor(Cursor.HAND);
        botonRegistro.setCursor(Cursor.HAND);
    }

    /**
     * Valida las credenciales ingresadas y permite el acceso solo a usuarios
     * con rol "Admin".
     * <p>
     * Si las credenciales son válidas, abre el panel de administración
     * (Menu.fxml) y cierra la ventana actual. En caso de error en el correo,
     * contraseña o credenciales inválidas, muestra la alerta correspondiente.
     *
     * @param event Evento de acción asociado al botón de acceder.
     */
    @FXML
    private void Acceder(ActionEvent event) {
        String email = campoUsuario.getText();
        String password = campoContrasena.getText();

        if (compruebaCampo.compruebaVacio(campoUsuario)) {
            Alertas.error("Campo vacío", "El campo de correo no puede estar vacío.");
        } else if (!compruebaCampo.emailValido(campoUsuario)) {
            Alertas.error("Formato inválido", "El correo electrónico no es válido.");
            campoUsuario.clear();
        } else if (compruebaCampo.compruebaVacio(campoContrasena)) {
            Alertas.aviso("Campo vacío", "El campo de contraseña no puede estar vacío.");
        } else {
            int resultado = ConsultasLogin.validarLogin(email, password);

            switch (resultado) {
                case 0:
                    Usuario usuario = ConsultasLogin.obtenerUsuarioPorEmail(email);
                    Usuario.setUsuarioActual(usuario);

                    if (!"Admin".equalsIgnoreCase(usuario.getTipoUsuario())) {
                        Alertas.error("Acceso denegado", "Solo el administrador puede acceder.");
                        campoUsuario.clear();
                        campoContrasena.clear();
                        return;
                    }

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Menu.fxml"));
                        Parent root = loader.load();
                        MenuController controller = loader.getController();
                        controller.setUsuarioActual(usuario);

                        Stage stage = new Stage();
                        stage.setTitle("Treknic");
                        stage.setScene(new Scene(root));
                        stage.setMinWidth(1400);
                        stage.setMinHeight(700);
                        stage.setMaximized(true);
                        stage.getIcons().add(new Image("/img/montanita.png"));
                        stage.show();

                        Stage loginStage = (Stage) campoUsuario.getScene().getWindow();
                        loginStage.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 1:
                    Alertas.error("Error", "El correo electrónico no está registrado.");
                    campoUsuario.clear();
                    campoContrasena.clear();
                    break;

                case 2:
                    Alertas.error("Error", "La contraseña es incorrecta.");
                    campoContrasena.clear();
                    break;

                default:
                    Alertas.error("Error", "Ha ocurrido un error inesperado.");
                    campoUsuario.clear();
                    campoContrasena.clear();
                    break;
            }

        }
    }

    /**
     * Abre una ventana modal para permitir al usuario recuperar su contraseña.
     * Carga la vista {@code RecuperarPassword.fxml} en una nueva ventana.
     *
     * @param event Evento de acción asociado al botón "¿Olvidaste tu
     * contraseña?".
     */
    @FXML
    private void abrirVentanaRecuperar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/RecuperarPassword.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Recuperar Contraseña");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(javafx.stage.StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.centerOnScreen();
            stage.getIcons().add(new Image("/img/montanita.png"));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre una ventana modal para registrar un nuevo usuario.
     * <p>
     * Carga la vista {@code AgregarUsuario.fxml} y la configura para estar
     * vinculada con este controlador de login. La lógica posterior al registro
     * devuelve al usuario a esta pantalla.
     *
     * @param event Evento de acción asociado al botón "Registrarse".
     */
    @FXML
    private void abrirVentanaRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarUsuario.fxml"));
            Parent root = loader.load();
            AgregarUsuarioController controller = loader.getController();
            controller.setAbiertoDesdeLogin(true);
            controller.setLoginController(this);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.initStyle(javafx.stage.StageStyle.DECORATED);
            stage.centerOnScreen();
            stage.setTitle("Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("/img/montanita.png"));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Limpia los campos de correo y contraseña del formulario de login.
     * <p>
     * Se utiliza para reiniciar el estado del formulario tras errores o cambios
     * de pantalla.
     */
    public void limpiarCamposLogin() {
        campoUsuario.clear();
        campoContrasena.clear();
    }
}
