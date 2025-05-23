package controladores;

import Utilidades.Alertas;
import Utilidades.validarEmail;
import bbdd.ConsultasLogin;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modelo.EmailUtil;

/**
 * Controlador de la vista de recuperación de contraseña.
 * <p>
 * Permite al usuario recuperar su contraseña mediante el envío de un código de
 * verificación por correo electrónico. Incluye validaciones de formato,
 * verificación del código y restablecimiento seguro de la nueva contraseña.
 * </p>
 * Funcionalidades principales:
 * <ul>
 *   <li>Validación del correo electrónico y verificación de existencia en la base
 *       de datos.</li>
 *   <li>Generación y envío de un código de verificación vía email.</li>
 *   <li>Validación del código ingresado por el usuario.</li>
 *   <li>Actualización de la contraseña si el código es válido.</li>
 * </ul>
 */
public class RecuperarPasswordController implements Initializable {

    @FXML
    private TextField campoEmail;
    @FXML
    private TextField campoCodigo;
    @FXML
    private PasswordField campoNuevaPass;
    @FXML
    private PasswordField campoConfirmarPass;
    @FXML
    private Button botonRestablecer;
    @FXML
    private Label etiquetaError;
    @FXML
    private Button botonEnviarCodigo;
    @FXML
    private Button botonReenviar;

    private String email;
    private boolean codigoVerificado = false;

    /**
     * Inicializa el controlador. Configura el comportamiento de los campos y
     * botones según el flujo del proceso de recuperación. Se establecen
     * validaciones y listeners para los campos de texto.
     *
     * @param url URL de inicialización (no utilizado).
     * @param rb ResourceBundle de inicialización (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoNuevaPass.setDisable(true);
        campoConfirmarPass.setDisable(true);
        botonRestablecer.setDisable(true);
        campoCodigo.setDisable(true);
        botonReenviar.setDisable(true);

        campoCodigo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() == 6) {
                verificarCodigo();
            } else {
                campoNuevaPass.setDisable(true);
                campoConfirmarPass.setDisable(true);
                botonRestablecer.setDisable(true);
                etiquetaError.setText("Primero debes verificar el código.");
                etiquetaError.setTextFill(Color.RED);
                codigoVerificado = false;
            }
        });

        campoEmail.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!codigoVerificado && !oldVal.equals(newVal)) {
                campoCodigo.clear();
                campoCodigo.setDisable(true);
                botonReenviar.setDisable(true);
                botonReenviar.setVisible(false);
                etiquetaError.setText("");
            }

            if (!newVal.trim().isEmpty()
                    && Utilidades.validarEmail.esEmailValido(newVal.trim())
                    && ConsultasLogin.existeEmail(newVal.trim())) {
                botonReenviar.setDisable(false);
            } else {
                botonReenviar.setDisable(true);
            }
        });
    }

    /**
     * Verifica si el código ingresado coincide con el registrado para el
     * usuario. Si es correcto, habilita los campos de nueva contraseña. Si es
     * incorrecto, muestra un mensaje de error y permite reenviar el código.
     */
    private void verificarCodigo() {
        String codigoIngresado = campoCodigo.getText().trim();

        if (ConsultasLogin.validarCodigo(email, codigoIngresado)) {
            etiquetaError.setText("Código verificado correctamente.");
            etiquetaError.setTextFill(Color.GREEN);

            campoNuevaPass.setDisable(false);
            campoConfirmarPass.setDisable(false);
            botonRestablecer.setDisable(false);

            campoEmail.setDisable(true);
            campoCodigo.setDisable(true);
            botonEnviarCodigo.setDisable(true);
            botonReenviar.setVisible(false);

            codigoVerificado = true;
        } else {
            etiquetaError.setText("Código incorrecto o expirado.");
            etiquetaError.setTextFill(Color.RED);

            campoNuevaPass.setDisable(true);
            campoConfirmarPass.setDisable(true);
            botonRestablecer.setDisable(true);
            codigoVerificado = false;

            botonReenviar.setVisible(true);
        }
    }

    /**
     * Envía un código de verificación al correo ingresado, si este es válido y
     * está registrado en la base de datos.
     *
     * @param event Evento de clic sobre el botón "Enviar código".
     */
    @FXML
    private void enviarCodigo(ActionEvent event) {
        email = campoEmail.getText().trim();

        if (email.isEmpty()) {
            etiquetaError.setText("Por favor, ingresa un correo.");
            etiquetaError.setTextFill(Color.RED);
        } else if (!validarEmail.esEmailValido(email)) {
            etiquetaError.setText("El formato del correo no es válido.");
            etiquetaError.setTextFill(Color.RED);
        } else if (!ConsultasLogin.existeEmail(email)) {
            etiquetaError.setText("El correo no está registrado.");
            etiquetaError.setTextFill(Color.RED);
        } else {
            String codigo = String.valueOf((int) (Math.random() * 900000 + 100000));
            ConsultasLogin.registrarCodigoVerificacion(email, codigo);
            EmailUtil.enviarCodigo(email, codigo);

            etiquetaError.setText("Código enviado. Revisa tu correo.");
            etiquetaError.setTextFill(Color.GREEN);
            campoCodigo.setDisable(false);

            botonEnviarCodigo.setVisible(false);
            botonReenviar.setVisible(true);
            botonReenviar.setDisable(false);
        }
    }

    /**
     * Reenvía el código de verificación al correo si este fue ingresado
     * correctamente.
     *
     * @param event Evento de clic sobre el botón "Reenviar código".
     */
    @FXML
    private void reenviarCodigo(ActionEvent event) {
        email = campoEmail.getText().trim();

        if (email.isEmpty()) {
            etiquetaError.setText("Primero debes ingresar un correo.");
            etiquetaError.setTextFill(Color.RED);
        } else if (!ConsultasLogin.existeEmail(email)) {
            etiquetaError.setText("El correo no está registrado.");
            etiquetaError.setTextFill(Color.RED);
        } else {
            String nuevoCodigo = String.valueOf((int) (Math.random() * 900000 + 100000));
            ConsultasLogin.registrarCodigoVerificacion(email, nuevoCodigo);
            EmailUtil.enviarCodigo(email, nuevoCodigo);

            campoCodigo.clear();
            campoCodigo.setDisable(false);

            etiquetaError.setText("El código ha sido reenviado. Revisa tu correo.");
            etiquetaError.setTextFill(Color.GREEN);

        }
    }

    /**
     * Ejecuta el cambio de contraseña si el código fue verificado y las
     * contraseñas ingresadas coinciden y no están vacías.
     *
     * @param event Evento de clic sobre el botón "Restablecer".
     */
    @FXML
    private void restablecerContrasena(ActionEvent event) {
        String contrasenaUno = campoNuevaPass.getText();
        String contrasenaDos = campoConfirmarPass.getText();

        if (!codigoVerificado) {
            etiquetaError.setText("Primero debes verificar el código.");
            etiquetaError.setTextFill(Color.RED);
        } else if (contrasenaUno.isEmpty() || contrasenaDos.isEmpty()) {
            etiquetaError.setText("Completa ambos campos de contraseña.");
            etiquetaError.setTextFill(Color.RED);
        } else if (!contrasenaUno.equals(contrasenaDos)) {
            etiquetaError.setText("Las contraseñas no coinciden.");
            etiquetaError.setTextFill(Color.RED);
        } else if (ConsultasLogin.actualizarContrasena(email, contrasenaUno)) {
            Alertas.informacion("Tu contraseña ha sido actualizada correctamente.");
            ((Stage) campoEmail.getScene().getWindow()).close();
        } else {
            etiquetaError.setText("Error al actualizar la contraseña.");
            etiquetaError.setTextFill(Color.RED);
        }
    }

    /**
     * Cierra la ventana actual sin realizar cambios.
     *
     * @param event Evento de clic sobre el botón "Cancelar".
     */
    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage) campoEmail.getScene().getWindow()).close();
    }

}
