/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.EstiloSistema;
import bbdd.ConsultasConfiguracion;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import modelo.ConfiguracionSistema;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class ConfiguracionController implements Initializable {

    @FXML
    private TabPane tabPane;
    @FXML
    private TextField nombreSitioField;
    @FXML
    private TextField emailContactoField;
    @FXML
    private TextField telefonoSoporteField;
    @FXML
    private ComboBox<String> idiomaComboBox;
    @FXML
    private TextArea descripcionField;
    @FXML
    private ComboBox<String> politicaPasswordComboBox;
    @FXML
    private TextField expiracionSesionField;
    @FXML
    private TextField intentosFallidosField;
    @FXML
    private CheckBox dosFactoresCheckBox;
    @FXML
    private TextField listaBlancaIPsField;
    @FXML
    private TextField colorPrimarioField;
    @FXML
    private TextField urlLogoField;
    @FXML
    private TextField urlFaviconField;
    @FXML
    private TextArea cssPersonalizadoField;
    @FXML
    private HBox rootHBox;
    @FXML
    private ColorPicker colorPickerFondo;
    @FXML
    private Button botonGuardarCambio;
    @FXML
    private AnchorPane contenedor;

    /**
     * Initializes the controller class.
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

        ConfiguracionSistema config = ConsultasConfiguracion.obtenerConfiguracion();

        nombreSitioField.setText(config.getNombreSitio());
        emailContactoField.setText(config.getEmailContacto());
        telefonoSoporteField.setText(config.getTelefonoSoporte());
        idiomaComboBox.getSelectionModel().select(config.getIdioma());
        descripcionField.setText(config.getDescripcion());

        politicaPasswordComboBox.getSelectionModel().select(config.getPoliticaContrasena());
        expiracionSesionField.setText(String.valueOf(config.getExpiracionSesionMin()));
        intentosFallidosField.setText(String.valueOf(config.getIntentosFallidosMax()));
        dosFactoresCheckBox.setSelected(config.isAutenticacionDosFactores());
        listaBlancaIPsField.setText(config.getListaIPs());

        idiomaComboBox.getItems().addAll("Español", "Inglés");
        politicaPasswordComboBox.getItems().addAll("Débil", "Media", "Fuerte");

        idiomaComboBox.getSelectionModel().select(config.getIdioma());
        politicaPasswordComboBox.getSelectionModel().select(config.getPoliticaContrasena());

    }

    @FXML
    private void seleccionarColorFondo(ActionEvent event) {
        Color colorSeleccionado = colorPickerFondo.getValue();

        EstiloSistema.getInstancia().setColorFondo(colorSeleccionado);

        String colorHex = EstiloSistema.getInstancia().getColorFondoHex();
        rootHBox.setStyle("-fx-background-color: " + colorHex + ";");
    }

    @FXML
    private void subirCssPersonalizado(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo CSS");

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Archivos CSS (*.css)", "*.css");
        fileChooser.getExtensionFilters().add(filter);

        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            String rutaCss = archivoSeleccionado.toURI().toString();
            ConfiguracionSistema.getInstancia().setCssPersonalizado(rutaCss);

            Platform.runLater(() -> {
                if (contenedor.getScene() != null) {
                    contenedor.getScene().getStylesheets().clear();
                    contenedor.getScene().getStylesheets().add(rutaCss);
                }
            });

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("CSS Aplicado");
            alerta.setHeaderText(null);
            alerta.setContentText("¡El archivo CSS se aplicó exitosamente!");
            alerta.showAndWait();
        }
    }

    @FXML
    private void guardarConfiguracionApariencia() {
        ConfiguracionSistema config = new ConfiguracionSistema();

        config.setNombreSitio(nombreSitioField.getText());
        config.setEmailContacto(emailContactoField.getText());
        config.setTelefonoSoporte(telefonoSoporteField.getText());
        config.setIdioma((String) idiomaComboBox.getValue());
        config.setDescripcion(descripcionField.getText());

        config.setPoliticaContrasena((String) politicaPasswordComboBox.getValue());
        config.setExpiracionSesionMin(Integer.parseInt(expiracionSesionField.getText()));
        config.setIntentosFallidosMax(Integer.parseInt(intentosFallidosField.getText()));
        config.setAutenticacionDosFactores(dosFactoresCheckBox.isSelected());
        config.setListaIPs(listaBlancaIPsField.getText());

        ConsultasConfiguracion.guardarConfiguracion(config);
        Alertas.informacion("Configuración guardada con éxito.");
    }

}
