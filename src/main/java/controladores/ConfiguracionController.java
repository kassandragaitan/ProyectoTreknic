/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    private ComboBox<?> idiomaComboBox;
    @FXML
    private TextArea descripcionField;
    @FXML
    private ComboBox<?> politicaPasswordComboBox;
    @FXML
    private TextField expiracionSesionField;
    @FXML
    private TextField intentosFallidosField;
    @FXML
    private CheckBox dosFactoresCheckBox;
    @FXML
    private TextField listaBlancaIPsField;
    @FXML
    private ComboBox<?> temaComboBox;
    @FXML
    private TextField colorPrimarioField;
    @FXML
    private TextField urlLogoField;
    @FXML
    private TextField urlFaviconField;
    @FXML
    private TextArea cssPersonalizadoField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
