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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import modelo.UsuarioRegistro;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Conexion.conectar();
        Conexion.cargarComboIdioma(campoIdioma);
        Conexion.cargarComboTipoUsuario(campoTipoUsuario);
        Conexion.cargarComboTipoCompania(campoTipoCompania);
        Conexion.cerrarConexion();
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
        // tipo viajero 
        else if (compruebaCampo.compruebaVacio(campoTelefono)) {
            Alertas.aviso("Campo vacío", "El teléfono no puede estar vacío.");
            //validacion
        } else {
            UsuarioRegistro usuario = new UsuarioRegistro();
            usuario.setNombre(campoNombre.getText());
            usuario.setTelefono(campoTelefono.getText());
            usuario.setEmail(campoEmail.getText());
            usuario.setContrasena(campoContrasena.getText());
            usuario.setTipoViajero(campoTipoCompania.getValue());
            usuario.setIdioma(campoIdioma.getValue());//.getSelectedItem().toString(), 
            usuario.setTipoUsuario(campoTipoUsuario.getValue());

            if (Conexion.registrarUsuario(usuario)) {
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
        // Restablecer también los ComboBox si es necesario
    }
}
