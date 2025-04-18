/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasCategoria;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Categoria;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarCategoriaController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private Button botonRegistrar;
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
    private void RegistrarCategoria(ActionEvent event) {
        if (campoNombre.getText().trim().isEmpty()) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (campoDescripcion.getText().trim().isEmpty()) {
            Alertas.aviso("Campo vacío", "La descripción no puede estar vacía.");
        } else {
            Categoria categoria = new Categoria(campoNombre.getText(), campoDescripcion.getText());

            if (ConsultasCategoria.registrarCategoria(categoria)) {
                Alertas.informacion("Categoría registrada exitosamente.");
                campoNombre.clear();
                campoDescripcion.clear();
            } else {
                Alertas.error("Error en el registro", "Ocurrió un error al registrar la categoría.");
            }
        }
    }

}
