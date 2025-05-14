/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Animacion;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modelo.Usuario;
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
    private Button botonConfiguracion;
    @FXML
    private Button botonResenas;
    @FXML
    private Button botonCerrarSesion;
    @FXML
    private SplitMenuButton botonGestionViajes;
    @FXML
    private MenuItem menuItinerarios;
    @FXML
    private MenuItem menuDestinos;
    @FXML
    private MenuItem menuActividades;
    @FXML
    private MenuItem menuAlojamientos;
    @FXML
    private MenuItem menuTipoAlojamientos;
    @FXML
    private MenuItem menuCategoria;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    private Usuario usuarioActual;

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarPrincipalConUsuario();
    }

    public void cargarEcena(String escena) {
        try {
            FXMLLoader cargador = new FXMLLoader();
            cargador.setLocation(getClass().getResource(escena));
            Parent nuevaEscena = cargador.load();

            Object controller = cargador.getController();
            if (controller instanceof PrincipalController && usuarioActual != null) {
                ((PrincipalController) controller).inicializarUsuario(usuarioActual);
            }

            panel.setCenter(nuevaEscena);
            Animacion.aparecer2000(nuevaEscena);

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarPrincipalConUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Principal.fxml"));
            Parent nuevaEscena = loader.load();

            PrincipalController controller = loader.getController();
            controller.inicializarUsuario(usuarioActual);

            panel.setCenter(nuevaEscena);
            Animacion.aparecer2000(nuevaEscena);

        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
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
        cargarPrincipalConUsuario();

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
    private void irAConfiguracion(ActionEvent event) {
        cargarEcena("/vistas/Configuracion.fxml");

    }

    @FXML
    private void irAResenas(ActionEvent event) {
        cargarEcena("/vistas/GestionResenas.fxml");
    }

    @FXML
    private void CerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Inicio de Sesión");
            loginStage.setScene(new Scene(root));
            loginStage.show();

            Stage stageActual = (Stage) botonCerrarSesion.getScene().getWindow();
            stageActual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irA_Actividades(ActionEvent event) {
        cargarEcena("/vistas/GestionActividades.fxml");
    }

    @FXML
    private void irA_Alojamiento(ActionEvent event) {
        cargarEcena("/vistas/GestionAlojamiento.fxml");
    }

    @FXML
    private void irATipodeAlojamiento(ActionEvent event) {
        cargarEcena("/vistas/GestionTipoDeAlojamiento.fxml");
    }

    @FXML
    private void irACategoria(ActionEvent event) {
        cargarEcena("/vistas/GestionCategoria.fxml");

    }
}
