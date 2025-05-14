/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import Utilidades.validarEmail;
import Utilidades.validarTelefonoGlobal;
import bbdd.Conexion;
import bbdd.ConsultasConfiguracion;
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
import modelo.ConfiguracionSistema;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        Conexion.conectar();
        checkActivo.setSelected(true);
        checkActivo.setDisable(true);

        ConsultasUsuario.cargarComboIdioma(campoIdioma);
        ConsultasUsuario.cargarComboTipoUsuario(campoTipoUsuario);
        ConsultasUsuario.cargarComboTipoCompania(campoTipoCompania);
        Conexion.cerrarConexion();
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    private Usuario usuarioEnEdicion = null;

    public void VerCamposUsuario(Usuario usuario) {
        this.usuarioEnEdicion = usuario;
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
        botonRegistrar.setDisable(!editable);
    }

    private AdministracionUsuarioController administracionUsuarioController;

    public void setAdministracionUsuarioController(AdministracionUsuarioController controller) {
        this.administracionUsuarioController = controller;
    }
    private boolean abiertoDesdeLogin = false;

    public void setAbiertoDesdeLogin(boolean valor) {
        this.abiertoDesdeLogin = valor;
    }

    @FXML
    private void registrar(ActionEvent event) {
        Conexion.conectar();

        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoTelefono)) {
            Alertas.aviso("Campo vacío", "El teléfono no puede estar vacío.");
        } else if (!validarTelefonoGlobal.esTelefonoValido(campoTelefono.getText())) {
            Alertas.aviso("Teléfono inválido", "Ingrese un número válido (8-15 dígitos, con o sin '+'). En Nicaragua debe comenzar con 2, 5, 7 u 8.");
        } else if (campoTelefono.getText().length() > 20) {
            Alertas.aviso("Teléfono muy largo", "El número de teléfono no debe exceder los 20 caracteres.");
        } else if (compruebaCampo.compruebaVacio(campoEmail)) {
            Alertas.aviso("Campo vacío", "El email no puede estar vacío.");
        } else if (!validarEmail.esEmailValido(campoEmail.getText())) {
            Alertas.aviso("Email inválido", "Ingrese un correo electrónico válido.");
        } else if (compruebaCampo.compruebaVacio(campoContrasena)) {
            Alertas.aviso("Campo vacío", "La contraseña no puede estar vacía.");
        } else if (campoTipoCompania.getValue() == null || campoTipoCompania.getValue().equals("Seleccione")) {
            Alertas.aviso("Combo vacío", "Debe seleccionar un tipo de viajero.");
        } else if (campoTipoUsuario.getValue() == null || campoTipoUsuario.getValue().equals("Seleccione")) {
            Alertas.aviso("Combo vacío", "Debe seleccionar un tipo de usuario.");
        } else if (campoIdioma.getValue() == null || campoIdioma.getValue().equals("Seleccione")) {
            Alertas.aviso("Combo vacío", "Debe seleccionar un idioma.");
        } else {
            String telefonoLimpio = campoTelefono.getText().replaceAll("[^+0-9]", "");

            if (usuarioEnEdicion == null && ConsultasUsuario.existeEmail(campoEmail.getText())) {
                Alertas.aviso("Email duplicado", "El correo ya está registrado. Use otro diferente.");
                Conexion.cerrarConexion();
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(campoNombre.getText());
            usuario.setTelefono(telefonoLimpio);
            usuario.setEmail(campoEmail.getText());
            usuario.setContrasena(campoContrasena.getText());
            usuario.setTipoViajero(campoTipoCompania.getValue());
            usuario.setIdioma(campoIdioma.getValue());
            usuario.setTipoUsuario(campoTipoUsuario.getValue());
            usuario.setActivo(true);

            if (usuarioEnEdicion == null) {
                if (ConsultasUsuario.registrarUsuario(usuario)) {
                    Alertas.informacion("Usuario registrado exitosamente.");
                    limpiarFormulario();
                    recargarTabla();

                    if (abiertoDesdeLogin) {
                        Stage ventanaActual = (Stage) botonRegistrar.getScene().getWindow();
                        ventanaActual.close();
                    }
                } else {
                    Alertas.error("Error en el registro", "Ocurrió un error al registrar el usuario.");
                }
            } else {
                usuario.setIdUsuario(usuarioEnEdicion.getIdUsuario());
                if (ConsultasUsuario.actualizarUsuario(usuario)) {
                    Alertas.informacion("Usuario actualizado correctamente.");
                    recargarTabla();
                    Stage stage = (Stage) botonRegistrar.getScene().getWindow();
                    stage.close();
                } else {
                    Alertas.error("Error al actualizar", "Ocurrió un error al actualizar el usuario.");
                }
            }
        }
    }

    private void recargarTabla() {
        if (administracionUsuarioController != null) {
            administracionUsuarioController.recargarTabla();
        }
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoTelefono.clear();
        campoEmail.clear();
        campoContrasena.clear();
        campoTipoUsuario.getSelectionModel().selectFirst();
        campoTipoCompania.getSelectionModel().selectFirst();
        campoIdioma.getSelectionModel().selectFirst();
    }


}
