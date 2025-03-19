/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
//

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class MenuController implements Initializable {

    @FXML
    private HBox menuHBox;
    @FXML
    private Button botonItinerarios;
    @FXML
    private Button botonDestinos;
    @FXML
    private Button botonUsuarios;
    @FXML
    private Button botonReportes;
    @FXML
    private BorderPane panel;
    @FXML
    private Button botonPrincipal;
    @FXML
    private Button botonNotificaciones;
    @FXML
    private Button botonSoporte;
    @FXML
    private Button botonContenido;
    @FXML
    private Button botonConfiguracion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEcena("/vistas/Principal.fxml");
    }

    public void cargarEcena(String escena) {
        try {
            FXMLLoader cargador = new FXMLLoader();
            cargador.setLocation(getClass().getResource(escena));
            Parent nuevaEscena = (Parent) cargador.load();
            panel.setCenter(nuevaEscena);
//           Animation.animacionEscena(contenedor);

        } catch (java.lang.IllegalAccessError | IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void irAItinerarios(ActionEvent event) {
        cargarEcena("/vistas/GestionItinerario.fxml");

    }

    @FXML
    private void irADestinos(ActionEvent event) {
        cargarEcena("/vistas/GestionDestinos.fxml");
    }

    @FXML
    private void irAGestionUsuarios(ActionEvent event) {
        cargarEcena("/vistas/AdministracionUsuario.fxml");
    }

    @FXML
    private void irAReportes(ActionEvent event) {
        cargarEcena("/vistas/Reportes.fxml");
    }

    @FXML
    private void irAPrincipal(ActionEvent event) {
        cargarEcena("/vistas/Principal.fxml");

    }

    @FXML
    private void irANotificaciones(ActionEvent event) {
        cargarEcena("/vistas/Notificaciones.fxml");

    }

    @FXML
    private void irASoporte(ActionEvent event) {
        cargarEcena("/vistas/Soporte.fxml");

    }

    @FXML
    private void irAContenido(ActionEvent event) {
    }

    @FXML
    private void irAConfiguracion(ActionEvent event) {
        cargarEcena("/vistas/Configuracion.fxml");

    }
}
