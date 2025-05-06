package controladores;

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

    private String email;
    private boolean codigoVerificado = false;
    @FXML
    private Button botonEnviarCodigo;
    @FXML
    private Button botonReenviar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoNuevaPass.setDisable(true);
        campoConfirmarPass.setDisable(true);
        botonRestablecer.setDisable(true);

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
    }

    @FXML
    private void enviarCodigo(ActionEvent event) {
        email = campoEmail.getText().trim();

        if (email.isEmpty()) {
            etiquetaError.setText("Por favor, ingresa un correo.");
            return;
        }

        if (!ConsultasLogin.existeEmail(email)) {
            etiquetaError.setText("El correo no está registrado.");
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

            // NUEVO: Desactivar email y envío
            campoEmail.setDisable(true);
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

            // NUEVO: Mostrar opción de reenviar
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

        String pass1 = campoNuevaPass.getText();
        String pass2 = campoConfirmarPass.getText();

        if (pass1.isEmpty() || pass2.isEmpty()) {
            etiquetaError.setText("Completa ambos campos de contraseña.");
            return;
        }

        if (!pass1.equals(pass2)) {
            etiquetaError.setText("Las contraseñas no coinciden.");
            return;
        }

        if (!esPasswordSegura(pass1)) {
            etiquetaError.setText("La contraseña debe tener al menos 8 caracteres, una letra y un número.");
            return;
        }

        if (ConsultasLogin.actualizarContrasena(email, pass1)) {
            etiquetaError.setText("Contraseña actualizada correctamente.");
            etiquetaError.setTextFill(Color.GREEN);
        } else {
            etiquetaError.setText("Error al actualizar la contraseña.");
            etiquetaError.setTextFill(Color.RED);
        }
    }

    private boolean esPasswordSegura(String password) {
        return password.length() >= 8
                && password.matches(".*[a-zA-Z].*")
                && password.matches(".*\\d.*");
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage) campoEmail.getScene().getWindow()).close();
    }

    @FXML
    private void reenviarCodigo(ActionEvent eveft) {
        if (email == null || email.isEmpty()) {
            etiquetaError.setText("Primero debes ingresar un correo válido.");
            return;
        }

        String nuevoCodigo = String.valueOf((int) (Math.random() * 900000 + 100000));
        ConsultasLogin.registrarCodigoVerificacion(email, nuevoCodigo);
        EmailUtil.enviarCodigo(email, nuevoCodigo);

        etiquetaError.setText("Nuevo código enviado. Revisa tu correo.");
        etiquetaError.setTextFill(Color.GREEN);
        campoCodigo.clear(); // Limpia el campo del código ingresado

        botonReenviar.setVisible(false);
    }

}
