/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import bbdd.ConsultasMovimientos;
import bbdd.ConsultasNotificaciones;
import bbdd.ConsultasSugerencias;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
            int idUsuario = Usuario.getUsuarioActual() != null
                    ? Usuario.getUsuarioActual().getIdUsuario()
                    : -1;

            if (idUsuario < 0) {
                Alertas.error("Sin sesión", "Debes iniciar sesión para enviar sugerencias.");
            }  else if (titulo.isEmpty()) {
                Alertas.error("Título vacío", "El título no puede quedar vacío.");
                campoTitulo.requestFocus();
            }else if (titulo.length() < 3) {
                Alertas.error("Título muy corto", "Debe tener al menos 3 caracteres.");
                campoTitulo.requestFocus();
            }else if (titulo.length() > 100) {
                Alertas.error("Título demasiado largo", "Máximo 100 caracteres permitidos.");
                campoTitulo.requestFocus();
            }else if (ConsultasSugerencias.existeTitulo(idUsuario, titulo)) {
                Alertas.error("Duplicado", "Ya enviaste una sugerencia con ese título.");
                campoTitulo.clear();
            }else if (mensaje.isEmpty()) {
                Alertas.error("Mensaje vacío", "El mensaje no puede quedar vacío.");
                campoMensaje.requestFocus();
            }else if (mensaje.length() < 10) {
                Alertas.error("Mensaje muy corto", "Debe tener al menos 10 caracteres.");
                campoMensaje.requestFocus();
            }else if (mensaje.length() > 500) {
                Alertas.error("Mensaje demasiado largo", "Máximo 500 caracteres permitidos.");
                campoMensaje.requestFocus();
            } 
            else {
                boolean exito = ConsultasSugerencias.insertarSugerencia(idUsuario, titulo, mensaje);
                if (exito) {
                    String detalle = "El usuario envió sugerencia: \"" + titulo + "\"";
                    ConsultasMovimientos.registrarMovimiento(detalle, new java.util.Date(), idUsuario);
                    ConsultasNotificaciones.registrarNotificacion(detalle, idUsuario);

                    Alertas.informacion("Sugerencia enviada con éxito");
                    limpiarFormulario();
                } else {
                    Alertas.error("Error al enviar", "No se pudo guardar la sugerencia. Intenta más tarde.");
                }
            }
        }

    private void limpiarFormulario() {
        campoTitulo.clear();
        campoMensaje.clear();
        botonGuardar.setText("Guardar");
    }

}
