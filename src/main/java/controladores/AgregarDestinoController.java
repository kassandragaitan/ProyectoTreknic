/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarDestinoController implements Initializable {

    @FXML
    private TextField campoRutaArchivo;
    @FXML
    private Button botonSelccionarImagen;
    @FXML
    private ImageView imagenTrekNic;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private DatePicker campoFecha;
    @FXML
    private Button botonRegistrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }    

    @FXML
    private void seleccionarImagen(ActionEvent event) {
    }

    @FXML
    private void RegistrarDestino(ActionEvent event) {
    }
    
}
