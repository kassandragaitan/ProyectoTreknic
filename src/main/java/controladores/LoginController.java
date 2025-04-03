/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import bbdd.Conexion;
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
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void Acceder(ActionEvent event) {

         String miUsuario = campoUsuario.getText();
        String mipass = campoContrasena.getText();  

        Conexion.conectar();

        if (Conexion.acceder(miUsuario, mipass)) {
            abrirPantallaPrincipal();
        } else {
            Alertas.error("Error", "Usuario o contraseña incorrectos.");
            campoUsuario.clear();
            campoContrasena.clear();
        }
        Conexion.cerrarConexion();
    }

    private void abrirPantallaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Menu.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Treknic");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            // Cerrar la ventana de login
            Stage loginStage = (Stage) campoUsuario.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
