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

/**
 * Controlador para el menú principal de la aplicación Treknic.
 * <p>
 * Gestiona la navegación entre diferentes módulos del sistema, como usuarios,
 * itinerarios, destinos, actividades, configuraciones, soporte, y más. Utiliza
 * animaciones al cargar nuevas vistas y conserva el usuario activo para su uso
 * en distintas pantallas.
 * </p>
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
     * Inicializa los componentes del menú. Este método es llamado
     * automáticamente tras la carga del archivo FXML asociado.
     *
     * @param url No utilizado.
     * @param rb Recursos internacionalizados (no utilizados).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    private Usuario usuarioActual;

    /**
     * Establece el usuario actual en sesión y carga la vista principal
     * personalizada para ese usuario.
     *
     * @param usuario El objeto {@link Usuario} que ha iniciado sesión.
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarPrincipalConUsuario();
    }

    /**
     * Carga y muestra una nueva escena FXML dentro del panel central del menú.
     * <p>
     * Si la escena es {@code Principal.fxml}, se le pasa el usuario actual para
     * inicializarla. Aplica animación de entrada a la nueva vista.
     *
     * @param escena Ruta del archivo FXML a cargar (ej.
     * "/vistas/GestionUsuario.fxml").
     */
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

    /**
     * Carga la vista principal ({@code Principal.fxml}) y le pasa el usuario
     * actual. La vista se muestra en el panel central con animación.
     */
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

    /**
     * Navega al módulo de gestión de itinerarios.
     *
     * @param event Evento del botón "Itinerarios".
     */
    @FXML
    private void irAItinerarios(ActionEvent event) {
        cargarEcena("/vistas/GestionItinerario.fxml");
    }

    /**
     * Navega al módulo de gestión de destinos.
     *
     * @param event Evento del botón "Destinos".
     */
    @FXML
    private void irADestinos(ActionEvent event) {
        cargarEcena("/vistas/GestionDestinos.fxml");
    }

    /**
     * Navega al módulo de gestión de GestionUsuarios.
     *
     * @param event Evento del botón "GestionUsuarios".
     */
    @FXML
    private void irAGestionUsuarios(ActionEvent event) {
        cargarEcena("/vistas/GestionUsuario.fxml");
    }

    /**
     * Navega al módulo de gestión de reportes.
     *
     * @param event Evento del botón "Reportes".
     */
    @FXML
    private void irAReportes(ActionEvent event) {
        cargarEcena("/vistas/Reportes.fxml");
    }

    /**
     * Navega al módulo de gestión de principal.
     *
     * @param event Evento del botón "Principal".
     */
    @FXML
    private void irAPrincipal(ActionEvent event) {
        cargarPrincipalConUsuario();

    }

    /**
     * Navega al módulo de gestión de notificaciones.
     *
     * @param event Evento del botón "Notificaciones".
     */
    @FXML
    private void irANotificaciones(ActionEvent event) {
        cargarEcena("/vistas/Notificaciones.fxml");

    }

    /**
     * Navega al módulo de gestión de soporte.
     *
     * @param event Evento del botón "Soporte".
     */
    @FXML
    private void irASoporte(ActionEvent event) {
        cargarEcena("/vistas/Soporte.fxml");

    }

    /**
     * Navega al módulo de gestión de configuracion.
     *
     * @param event Evento del botón "Configuracion".
     */
    @FXML
    private void irAConfiguracion(ActionEvent event) {
        cargarEcena("/vistas/Configuracion.fxml");

    }

    /**
     * Navega al módulo de gestión de resenas.
     *
     * @param event Evento del botón "Resenas".
     */
    @FXML
    private void irAResenas(ActionEvent event) {
        cargarEcena("/vistas/GestionResenas.fxml");
    }

    /**
     * Navega al módulo de gestión de actividades.
     *
     * @param event Evento del botón "Actividades".
     */
    @FXML
    private void irA_Actividades(ActionEvent event) {
        cargarEcena("/vistas/GestionActividades.fxml");
    }

    /**
     * Navega al módulo de gestión de alojamiento.
     *
     * @param event Evento del botón "Alojamiento".
     */
    @FXML
    private void irA_Alojamiento(ActionEvent event) {
        cargarEcena("/vistas/GestionAlojamiento.fxml");
    }

    /**
     * Navega al módulo de gestión de tipodeAlojamiento.
     *
     * @param event Evento del botón "TipodeAlojamiento".
     */
    @FXML
    private void irATipodeAlojamiento(ActionEvent event) {
        cargarEcena("/vistas/GestionTipoDeAlojamiento.fxml");
    }

    /**
     * Navega al módulo de gestión de categoria.
     *
     * @param event Evento del botón "Categoria".
     */
    @FXML
    private void irACategoria(ActionEvent event) {
        cargarEcena("/vistas/GestionCategoria.fxml");

    }

    /**
     * Cierra la sesión actual y redirige al usuario a la pantalla de login.
     * <p>
     * Cierra la ventana actual y abre nuevamente {@code Login.fxml}.
     *
     * @param event Evento del botón "Cerrar Sesión".
     */
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

}
