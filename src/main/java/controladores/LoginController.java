package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.ConsultasLogin;
import controladores.MenuController;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import modelo.Usuario;

public class LoginController implements Initializable {

    @FXML
    private Button botonAcceder;
    @FXML
    private TextField campoUsuario;
    @FXML
    private PasswordField campoContrasena;
    @FXML
    private Label loginlabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void Acceder(ActionEvent event) {
        String email = campoUsuario.getText();
        String password = campoContrasena.getText();

        if (compruebaCampo.compruebaVacio(campoUsuario)) {
            Alertas.aviso("Campo vacío", "El campo de correo no puede estar vacío.");
        } else if (!compruebaCampo.emailValido(campoUsuario)) {
            Alertas.aviso("Formato inválido", "El correo electrónico no es válido.");
        } else if (compruebaCampo.compruebaVacio(campoContrasena)) {
            Alertas.aviso("Campo vacío", "El campo de contraseña no puede estar vacío.");
        } else {
            int resultado = ConsultasLogin.validarLogin(email, password);

            switch (resultado) {
                case 0:
                    Usuario usuario = ConsultasLogin.obtenerUsuarioPorEmail(email);
                    Usuario.setUsuarioActual(usuario);

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Menu.fxml"));
                        Parent root = loader.load();
                        MenuController controller = loader.getController();
                        controller.setUsuarioActual(usuario);

                        Stage stage = new Stage();
                        stage.setTitle("Treknic");
                        stage.setScene(new Scene(root));
                        stage.setMinWidth(1000);
                        stage.setMinHeight(700);
                        stage.setMaximized(true);
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
                    ConsultasLogin.registrarIntentoFallido(email);
                    campoContrasena.clear();
                    break;

                case 3:
                    Alertas.error("Error", "Tu cuenta está inactiva.");
                    campoUsuario.clear();
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

    @FXML
    private void abrirVentanaRecuperar(ActionEvent event
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/RecuperarPassword.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Recuperar Contraseña");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
