package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.PreguntaFrecuente;
import modelo.PruebaFuncionalidad;

public class Soporte_ItemController implements Initializable {

    @FXML
    private ImageView iconoImagen;

    @FXML
    private Label etiquetaTitulo;

    @FXML
    private Label etiquetaDescripcion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setItem(Object item) {
        String icono = "/img/documento.png";

        if (item instanceof PreguntaFrecuente) {
            PreguntaFrecuente pregunta = (PreguntaFrecuente) item;
            etiquetaTitulo.setText(pregunta.getPregunta());
            etiquetaDescripcion.setText(pregunta.getRespuesta());
        } else if (item instanceof modelo.PruebaFuncionalidad) {
            PruebaFuncionalidad funcionalidad = (PruebaFuncionalidad) item;
            etiquetaTitulo.setText(funcionalidad.getTitulo());
            etiquetaDescripcion.setText(funcionalidad.getDescripcion());
        } else {
            etiquetaTitulo.setText("Elemento desconocido");
            etiquetaDescripcion.setText("No se pudo mostrar información.");
        }

        iconoImagen.setImage(new Image(getClass().getResourceAsStream(icono)));

    }
}
