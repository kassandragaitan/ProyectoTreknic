package controladores;

import Utilidades.EstiloSistema;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Controlador JavaFX para la sección de configuración del sistema. Permite
 * personalizar el color de fondo de la aplicación mediante un selector de
 * color. El color se aplica visualmente en tiempo real y se guarda en el estilo
 * global.
 *
 * Autor: k0343
 */
public class ConfiguracionController implements Initializable {

    @FXML
    private ColorPicker colorPickerFondo;
    @FXML
    private HBox rootHBox;
    @FXML
    private AnchorPane contenedor;

    /**
     * Inicializa el formulario de configuración. Recupera el color de fondo
     * actual del `rootHBox` y lo establece como valor inicial del ColorPicker.
     * Si no se encuentra un color válido, se asigna blanco por defecto.
     *
     * @param url URL de inicialización (no utilizada directamente).
     * @param rb ResourceBundle para internacionalización (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String style = rootHBox.getStyle();
        if (style != null && style.contains("-fx-background-color")) {
            String colorHex = style.substring(style.indexOf("#"), style.indexOf(";"));
            colorPickerFondo.setValue(Color.web(colorHex));
        } else {
            colorPickerFondo.setValue(Color.WHITE);
        }
    }

    /**
     * Evento que se ejecuta cuando el usuario selecciona un nuevo color en el
     * `ColorPicker`. Actualiza el color de fondo del contenedor y lo guarda en
     * el estilo global del sistema.
     *
     * @param event Evento generado por la selección de un color.
     */
    @FXML
    private void seleccionarColorFondo(ActionEvent event) {
        Color colorSeleccionado = colorPickerFondo.getValue();
        EstiloSistema.getInstancia().setColorFondo(colorSeleccionado);
        String colorHex = EstiloSistema.getInstancia().getColorFondoHex();
        rootHBox.setStyle("-fx-background-color: " + colorHex + ";");
    }
}
