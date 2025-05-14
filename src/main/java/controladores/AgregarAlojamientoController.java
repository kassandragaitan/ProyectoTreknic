/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasAlojamientos;
import bbdd.ConsultasDestinos;
import bbdd.ConsultasMovimientos;
import bbdd.ConsultasTipoAlojamiento;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.Alojamiento;
import modelo.ConexionFtp;
import modelo.Destino;
import modelo.TipoAlojamiento;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarAlojamientoController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private ComboBox<TipoAlojamiento> comboTipo;
    @FXML
    private TextField campoContacto;
    @FXML
    private TextField campoImagen;
    @FXML
    private ComboBox<Destino> comboDestino;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;

    /**
     * Initializes the controller class.
     */
    private Alojamiento alojamientoActual;
    private boolean esEdicion = false;
    private File archivoImagenSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoImagen.setEditable(false);
        Conexion.conectar();
        ConsultasTipoAlojamiento.cargarDatosTiposAlojamientoRegistrar(comboTipo);
        ConsultasDestinos.cargarComboDestino(comboDestino);
        Conexion.cerrarConexion();

        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    private GestionAlojamientoController gestionAlojamientoController;

    public void setGestionAlojamientoController(GestionAlojamientoController controller) {
        this.gestionAlojamientoController = controller;
    }

    @FXML
    private void RegistrarAlojamiento(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (comboTipo.getValue() == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar el tipo de alojamiento.");
        } else if (compruebaCampo.compruebaVacio(campoContacto)) {
            Alertas.aviso("Campo vacío", "El contacto no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoImagen)) {
            Alertas.aviso("Campo vacío", "Debe seleccionar una imagen.");
        } else if (comboDestino.getValue() == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar un destino.");
        } else {
            String nombreImagenRemota;
            if (!esEdicion) {
                // obtener extensión
                String ext = archivoImagenSeleccionado.getName()
                        .substring(archivoImagenSeleccionado.getName().lastIndexOf('.'));
                // generar nombre único
                nombreImagenRemota = System.currentTimeMillis() + ext;
                // conectar y subir
                if (!ConexionFtp.conectar() || !ConexionFtp.subirArchivo(archivoImagenSeleccionado, nombreImagenRemota)) {
                    Alertas.error("Error FTP", "No se pudo subir la imagen al servidor.");
                    return;
                }
                ConexionFtp.desconectar();
            } else {
                // en edición mantenemos la imagen existente
                nombreImagenRemota = alojamientoActual.getImagen();
            }

            // 3) Crear objeto Alojamiento con el nombre remoto de la imagen
            Alojamiento dto = new Alojamiento(
                    esEdicion ? alojamientoActual.getIdAlojamiento() : 0,
                    campoNombre.getText().trim(),
                    comboTipo.getValue().getIdTipo(),
                    campoContacto.getText().trim(),
                    nombreImagenRemota,
                    comboDestino.getValue().getId_destino()
            );

            // 4) Guardar en base de datos (alta o edición)
            boolean exito = esEdicion
                    ? ConsultasAlojamientos.actualizarAlojamiento(dto)
                    : ConsultasAlojamientos.registrarAlojamiento(dto);

            if (!exito) {
                Alertas.error("Error", "No se pudo guardar el alojamiento.");
                return;
            }

            // 5) Registrar movimiento
            Conexion.conectar();
            String mensaje = (esEdicion ? "Ha actualizado el alojamiento " : "Ha registrado el alojamiento ")
                    + campoNombre.getText().trim();
            ConsultasMovimientos.registrarMovimiento(
                    mensaje,
                    new java.util.Date(),
                    Usuario.getUsuarioActual().getIdUsuario()
            );
            Conexion.cerrarConexion();

            // 6) Feedback al usuario y refrescar vista
            Alertas.informacion(esEdicion
                    ? "Alojamiento actualizado correctamente."
                    : "Alojamiento registrado correctamente.");
            gestionAlojamientoController.cargarAlojamientos();
            cerrarVentana();
        }
    }

    private void recargarTabla() {
        if (gestionAlojamientoController != null) {
            gestionAlojamientoController.cargarAlojamientos();
        }
    }

    public void verAlojamiento(Alojamiento a) {
        this.alojamientoActual = a;
        campoNombre.setText(a.getNombre());
        campoContacto.setText(a.getContacto());
        campoImagen.setText(a.getImagen());

        for (TipoAlojamiento tipo : comboTipo.getItems()) {
            if (tipo.getIdTipo() == a.getIdTipo()) {
                comboTipo.setValue(tipo);
                break;
            }
        }

        for (Destino dest : comboDestino.getItems()) {
            if (dest.getId_destino() == a.getIdDestino()) {
                comboDestino.setValue(dest);
                break;
            }
        }

        botonRegistrar.setText("Actualizar");
        esEdicion = true;
    }

    public void setEdicionActiva(boolean editable) {
        campoNombre.setEditable(editable);
        campoContacto.setEditable(editable);
        campoImagen.setEditable(editable);
        comboDestino.setDisable(!editable);
        comboTipo.setDisable(!editable);
        botonRegistrar.setVisible(editable);

        double opacidad = editable ? 1.0 : 0.75;
        campoNombre.setOpacity(opacidad);
        campoContacto.setOpacity(opacidad);
        campoImagen.setOpacity(opacidad);
        comboDestino.setOpacity(opacidad);
        comboTipo.setOpacity(opacidad);
    }

    private void cerrarVentana() {
        Stage stage = (Stage) botonRegistrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File fichero = chooser.showOpenDialog(null);
        if (fichero != null) {
            archivoImagenSeleccionado = fichero;
            campoImagen.setText(fichero.getName()); // provisional
        }
    }

    private String subirImagenAlFTP(File localFile) {
        String ext = localFile.getName().substring(localFile.getName().lastIndexOf('.'));
        String remoteName = System.currentTimeMillis() + ext;
        if (!ConexionFtp.conectar() || !ConexionFtp.subirArchivo(localFile, remoteName)) {
            return null;
        }
        ConexionFtp.desconectar();
        return remoteName;
    }
}
