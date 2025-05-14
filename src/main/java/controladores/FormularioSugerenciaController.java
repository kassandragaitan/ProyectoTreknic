/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.ConsultasMovimientos;
import bbdd.ConsultasSugerencias;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class FormularioSugerenciaController implements Initializable {

    @FXML
    private TextField campoTitulo;
    @FXML
    private TextArea campoMensaje;
    @FXML
    private Button botonGuardar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void guardarSugerencia(ActionEvent event) {
        String titulo = campoTitulo.getText().trim();
        String mensaje = campoMensaje.getText().trim();

        if (compruebaCampo.compruebaVacio(campoMensaje)) {
            Alertas.aviso("Campo vacío", "El mensaje no puede estar vacío.");
        } else if (mensaje.length() < 5) {
            Alertas.aviso("Mensaje muy corto", "El mensaje debe tener al menos 5 caracteres.");
        } else {
            int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

            boolean exito = ConsultasSugerencias.insertarSugerencia(idUsuario, titulo, mensaje);
            if (exito) {
                ConsultasMovimientos.registrarMovimiento(
                        "El usuario ha enviado una sugerencia: " + (titulo.isEmpty() ? "(Sin título)" : titulo),
                        new Date(),
                        idUsuario
                );

                Alertas.informacion("¡Sugerencia enviada con éxito!");
                limpiarFormulario();
            } else {
                Alertas.error("Error", "No se pudo enviar la sugerencia.");
            }
        }
    }

    private void limpiarFormulario() {
        campoTitulo.clear();
        campoMensaje.clear();
        botonGuardar.setText("Guardar");
    }

}
