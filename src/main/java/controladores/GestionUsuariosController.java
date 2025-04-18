/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasUsuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class GestionUsuariosController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoTelefono;
    @FXML
    private TextField campoEmail;
    @FXML
    private TextField campoContrasena;
    @FXML
    private ComboBox<String> campoIdioma;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ComboBox<String> campoTipoUsuario;
    @FXML
    private ComboBox<String> campoTipoCompania;
    @FXML
    private CheckBox checkActivo;
    @FXML
    private ImageView imagenTrekNic;
    @FXML
    private Label linkLogin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        linkLogin.setCursor(Cursor.HAND);
        Conexion.conectar();
        checkActivo.setSelected(true);

        ConsultasUsuario.cargarComboIdioma(campoIdioma);
        ConsultasUsuario.cargarComboTipoUsuario(campoTipoUsuario);
        ConsultasUsuario.cargarComboTipoCompania(campoTipoCompania);
        Conexion.cerrarConexion();

        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }
   public void VerCamposUsuario(Usuario usuario) {
        campoNombre.setText(usuario.getNombre());
        campoTelefono.setText(usuario.getTelefono());
        campoEmail.setText(usuario.getEmail());
        campoContrasena.setText(usuario.getContrasena() != null ? usuario.getContrasena() : "");
        campoIdioma.setValue(usuario.getIdioma());
        campoTipoUsuario.setValue(usuario.getTipoUsuario());
        campoTipoCompania.setValue(usuario.getTipoViajero());
        checkActivo.setSelected(usuario.getActivo());
    }

    public void EditarCamposUsuario(boolean editable) {
        campoNombre.setDisable(!editable);
        campoTelefono.setDisable(!editable);
        campoEmail.setDisable(!editable);
        campoContrasena.setDisable(!editable);
        campoIdioma.setDisable(!editable);
        campoTipoUsuario.setDisable(!editable);
        campoTipoCompania.setDisable(!editable);
        checkActivo.setDisable(!editable);
        botonRegistrar.setDisable(!editable); // desactiva el botón de guardar si solo estás viendo
    }


    @FXML
    private void registrar(ActionEvent event) {
        Conexion.conectar();
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoEmail)) {
            Alertas.aviso("Campo vacío", "El email no puede estar vacío.");
            //validacion
        } else if (compruebaCampo.compruebaVacio(campoContrasena)) {
            Alertas.aviso("Campo vacío", "La contraseña no puede estar vacía.");
        } //tipo usuario 
        //fecha registro
        //idioma preferido 
        // tipo viajero \
        //estado activo o inactivo 
        else if (compruebaCampo.compruebaVacio(campoTelefono)) {
            Alertas.aviso("Campo vacío", "El teléfono no puede estar vacío.");
            //validacion
        } else {
            Usuario usuario = new Usuario();
            usuario.setNombre(campoNombre.getText());
            usuario.setTelefono(campoTelefono.getText());
            usuario.setEmail(campoEmail.getText());
            usuario.setContrasena(campoContrasena.getText());
            usuario.setTipoViajero(campoTipoCompania.getValue());
            usuario.setIdioma(campoIdioma.getValue());//.getSelectedItem().toString(), 
            usuario.setTipoUsuario(campoTipoUsuario.getValue());
            usuario.setActivo(checkActivo.isSelected());

            if (ConsultasUsuario.registrarUsuario(usuario)) {
                Alertas.informacion("Usuario registrado exitosamente.");
                limpiarFormulario();
            } else {

                Alertas.error("Error en el registro", "Ocurrió un error al registrar el usuario.");
            }
        }
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoTelefono.clear();
        campoEmail.clear();
        campoContrasena.clear();
        campoTipoUsuario.getSelectionModel().clearSelection();
        campoTipoCompania.getSelectionModel().clearSelection();
        campoIdioma.getSelectionModel().clearSelection();

    }

    @FXML
    private void irAlLogin(MouseEvent event) {
        try {
            // Cargar la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Login.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Iniciar Sesión");
            stage.show();

            // Cerrar la ventana actual
            Stage ventanaActual = (Stage) linkLogin.getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
