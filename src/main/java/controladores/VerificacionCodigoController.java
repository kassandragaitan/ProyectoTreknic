/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.ConsultasLogin;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.SolicitarVerificacionCodigo;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class VerificacionCodigoController implements Initializable {

    @FXML
    private TextField campoCodigo;
    @FXML
    private Label etiquetaError;

    /**
     * Initializes the controller class.
     */
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void verificarCodigo(ActionEvent event) {
        String codigoIngresado = campoCodigo.getText().trim();
        if (ConsultasLogin.validarCodigo(email, codigoIngresado)) {
            SolicitarVerificacionCodigo.resultado = true;
            ((Stage) campoCodigo.getScene().getWindow()).close();
        } else {
            etiquetaError.setText("Código incorrecto o expirado.");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        SolicitarVerificacionCodigo.resultado = false;
        ((Stage) campoCodigo.getScene().getWindow()).close();
    }

}
