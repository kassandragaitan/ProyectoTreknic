/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.PreguntaFrecuente;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class Soporte_ItemController implements Initializable {

    @FXML
    private ImageView iconoImagen;
    @FXML
    private Label etiquetaTitulo;
    @FXML
    private Label etiquetaDescripcion;

    /**
     * Initializes the controller class.
     */
    public void setItem(Object item) {
        if (item instanceof PreguntaFrecuente) {
            PreguntaFrecuente pregunta = (PreguntaFrecuente) item;
            etiquetaTitulo.setText(pregunta.getPregunta());
            etiquetaDescripcion.setText(pregunta.getRespuesta());
            iconoImagen.setImage(new Image(getClass().getResourceAsStream("/img/documento.png")));
        } else if (item instanceof String) {
            etiquetaTitulo.setText((String) item);
            etiquetaDescripcion.setText("");
            iconoImagen.setImage(new Image(getClass().getResourceAsStream("/img/documento.png")));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
