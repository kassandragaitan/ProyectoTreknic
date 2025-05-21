/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasNotificaciones;
import bbdd.ConsultasPreguntasFrecuentes;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class FormularioPreguntaController implements Initializable {

    @FXML
    private TextField txtPregunta;
    @FXML
    private TextArea txtRespuesta;
    @FXML
    private Button btnGuardar;
    @FXML
    private ImageView imagenTrekNic;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    @FXML
    private void guardarPregunta(ActionEvent event) {
        String pregunta = txtPregunta.getText().trim();
        String respuesta = txtRespuesta.getText().trim();

        if (compruebaCampo.compruebaVacio(txtPregunta)) {
            Alertas.error("Campo vacío", "La pregunta no puede estar vacía.");
        } else if (compruebaCampo.compruebaVacio(txtRespuesta)) {
            Alertas.error("Campo vacío", "La respuesta no puede estar vacía.");
        } else {
            boolean exito = ConsultasPreguntasFrecuentes.insertarPregunta(pregunta, respuesta);
            if (exito) {
                Conexion.conectar();

                String mensaje = "Ha registrado una nueva pregunta frecuente: " + pregunta;
                int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

                ConsultasNotificaciones.registrarMovimiento(
                        mensaje,
                        new Date(),
                        idUsuario
                );
                Conexion.cerrarConexion();

                Alertas.informacion("Pregunta guardada correctamente");
                limpiarFormulario();
            } else {
                Alertas.error("Error", "No se pudo guardar la pregunta.");
            }
        }

    }

    private void limpiarFormulario() {
        txtPregunta.clear();
        txtRespuesta.clear();
        btnGuardar.setText("Guardar");
    }

}
