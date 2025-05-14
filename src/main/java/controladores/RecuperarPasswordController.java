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

    @FXML
    private void enviarCodigo(ActionEvent event) {
        email = campoEmail.getText().trim();

        if (email.isEmpty()) {
            etiquetaError.setText("Por favor, ingresa un correo.");
            etiquetaError.setTextFill(Color.RED);
            return;
        }

        if (!validarEmail.esEmailValido(email)) {
            etiquetaError.setText("El formato del correo no es válido.");
            etiquetaError.setTextFill(Color.RED);
            return;
        }

        if (!ConsultasLogin.existeEmail(email)) {
            etiquetaError.setText("El correo no está registrado.");
            etiquetaError.setTextFill(Color.RED);
            return;
        }

        String codigo = String.valueOf((int) (Math.random() * 900000 + 100000));
        ConsultasLogin.registrarCodigoVerificacion(email, codigo);
        EmailUtil.enviarCodigo(email, codigo);

        etiquetaError.setText("Código enviado. Revisa tu correo.");
        etiquetaError.setTextFill(Color.GREEN);
        campoCodigo.setDisable(false);
    }

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

    @FXML
    private void restablecerContrasena(ActionEvent event) {
        if (!codigoVerificado) {
            etiquetaError.setText("Primero debes verificar el código.");
            etiquetaError.setTextFill(Color.RED);
            return;
        }

        String contrasenaUno = campoNuevaPass.getText();
        String contrasenaDos = campoConfirmarPass.getText();

        if (contrasenaUno.isEmpty() || contrasenaDos.isEmpty()) {
            etiquetaError.setText("Completa ambos campos de contraseña.");
            return;
        }

        if (!contrasenaUno.equals(contrasenaDos)) {
            etiquetaError.setText("Las contraseñas no coinciden.");
            return;
        }

        if (!contraseñaSegura(contrasenaUno)) {
            etiquetaError.setText("La contraseña debe tener al menos 8 caracteres, una letra y un número.");
            return;
        }

        if (ConsultasLogin.actualizarContrasena(email, contrasenaUno)) {
            Alertas.informacion("Tu contraseña ha sido actualizada correctamente.");
        ((Stage) campoEmail.getScene().getWindow()).close();
        } else {
            etiquetaError.setText("Error al actualizar la contraseña.");
            etiquetaError.setTextFill(Color.RED);
        }
    }

    private boolean contraseñaSegura(String password) {
        return password.length() >= 8
                && password.matches(".*[a-zA-Z].*")
                && password.matches(".*\\d.*");
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage) campoEmail.getScene().getWindow()).close();
    }

    @FXML
    private void reenviarCodigo(ActionEvent event) {
        email = campoEmail.getText().trim();

        if (email.isEmpty()) {
            etiquetaError.setText("Primero debes ingresar un correo.");
            etiquetaError.setTextFill(Color.RED);
            return;
        }

        if (!ConsultasLogin.existeEmail(email)) {
            etiquetaError.setText("El correo no está registrado.");
            etiquetaError.setTextFill(Color.RED);
            return;
        }

        String nuevoCodigo = String.valueOf((int) (Math.random() * 900000 + 100000));
        ConsultasLogin.registrarCodigoVerificacion(email, nuevoCodigo);
        EmailUtil.enviarCodigo(email, nuevoCodigo);

        campoCodigo.clear();
        campoCodigo.setDisable(false);

        etiquetaError.setText("El código ha sido reenviado. Revisa tu correo.");
        etiquetaError.setTextFill(Color.GREEN);
        botonReenviar.setVisible(false);
    }

}
