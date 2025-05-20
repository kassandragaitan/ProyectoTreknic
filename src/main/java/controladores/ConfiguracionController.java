package controladores;

import Utilidades.EstiloSistema;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ConfiguracionController implements Initializable {

    @FXML private ColorPicker colorPickerFondo;
    @FXML private HBox rootHBox;
    @FXML private AnchorPane contenedor;

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

    @FXML
    private void seleccionarColorFondo(ActionEvent event) {
        Color colorSeleccionado = colorPickerFondo.getValue();
        EstiloSistema.getInstancia().setColorFondo(colorSeleccionado);
        String colorHex = EstiloSistema.getInstancia().getColorFondoHex();
        rootHBox.setStyle("-fx-background-color: " + colorHex + ";");
    }
}
