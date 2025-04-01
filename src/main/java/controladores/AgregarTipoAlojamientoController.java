/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import modelo.TipoAlojamiento;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarTipoAlojamientoController implements Initializable {

    @FXML
    private TextField campoTipo;
    @FXML
    private Button botonRegistrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void RegistrarTipoAlojamiento(ActionEvent event) {
         if (compruebaCampo.compruebaVacio(campoTipo)) {
            Alertas.aviso("Campo vacío", "El tipo de alojamiento no puede estar vacío.");
        } else {
            TipoAlojamiento tipo = new TipoAlojamiento(campoTipo.getText());
            
            if (Conexion.registrarTipoAlojamiento(tipo)) {
                Alertas.informacion("Tipo de Alojamiento registrado exitosamente.");
                campoTipo.clear();
            } else {
                Alertas.error("Error en el registro", "Ocurrió un error al registrar el tipo de alojamiento.");
            }
        }
        
    }
    
}
