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
import javafx.scene.control.Label;
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
    @FXML
    private Button botonSeleccionar;
    @FXML
    private Label labelTitulo;

    /**
     * Initializes the controller class.
     */
    private Alojamiento alojamientoActual;
    private boolean esEdicion = false;
    private File archivoImagenSeleccionado;
    private TipoAlojamiento tipoSeleccionado;
    private Destino destinoSeleccionado;
    private GestionAlojamientoController gestionAlojamientoController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoImagen.setEditable(false);

        Conexion.conectar();
        ConsultasTipoAlojamiento.cargarDatosTiposAlojamientoRegistrar(comboTipo);
        ConsultasDestinos.cargarComboDestino(comboDestino);
        Conexion.cerrarConexion();

        imagenTrekNic.setImage(new Image(getClass().getResourceAsStream("/img/Encabezado.png")));

        TipoAlojamiento phTipo = new TipoAlojamiento(-1, "Seleccione");
        comboTipo.getItems().add(0, phTipo);
        comboTipo.getSelectionModel().selectFirst();
        tipoSeleccionado = null;
        comboTipo.setOnAction(e -> {
            TipoAlojamiento sel = comboTipo.getSelectionModel().getSelectedItem();
            if (sel != null && sel.getIdTipo() != -1) {
                tipoSeleccionado = sel;
            } else {
                tipoSeleccionado = null;
            }
        });

        Destino phDest = new Destino();
        phDest.setId_destino(-1);
        phDest.setNombre("Seleccione");
        comboDestino.getItems().add(0, phDest);
        comboDestino.getSelectionModel().selectFirst();
        destinoSeleccionado = null;
        comboDestino.setOnAction(e -> {
            Destino sel = comboDestino.getSelectionModel().getSelectedItem();
            if (sel != null && sel.getId_destino() != -1) {
                destinoSeleccionado = sel;
            } else {
                destinoSeleccionado = null;
            }
        });
    }

    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    public void setGestionAlojamientoController(GestionAlojamientoController controller) {
        this.gestionAlojamientoController = controller;
    }

    @FXML
    private void RegistrarAlojamiento(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (!esEdicion && ConsultasAlojamientos.existeNombreAlojamiento(campoNombre.getText().trim())) {
            Alertas.aviso("Nombre duplicado", "Ya existe un alojamiento con ese nombre.");
        } else if (tipoSeleccionado == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar el tipo de alojamiento.");
        } else if (compruebaCampo.compruebaVacio(campoContacto)) {
            Alertas.aviso("Campo vacío", "El contacto no puede estar vacío.");
        } else if (!Utilidades.validarTelefonoGlobal.esTelefonoNicaraguenseValido(campoContacto.getText().trim())) {
            Alertas.aviso("Teléfono inválido", "El contacto debe tener 8 dígitos y empezar por 2, 5, 7 u 8 (formato nicaragüense).");
            campoContacto.clear();
        } else if (compruebaCampo.compruebaVacio(campoImagen)) {
            Alertas.aviso("Campo vacío", "Debe seleccionar una imagen.");
        } else if (destinoSeleccionado == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar un destino.");
        } else {
            String nombreImagenParaBD;
            if (esEdicion && archivoImagenSeleccionado != null) {
                String ext = archivoImagenSeleccionado.getName()
                        .substring(archivoImagenSeleccionado.getName().lastIndexOf('.'));
                String remotoNuevo = System.currentTimeMillis() + ext;
                if (!ConexionFtp.conectar()
                        || !ConexionFtp.subirArchivo(archivoImagenSeleccionado, remotoNuevo)) {
                    Alertas.error("Error FTP", "No se pudo subir la nueva imagen.");
                    return;
                }
                ConexionFtp.desconectar();
                String remotoAntiguo = alojamientoActual.getImagen();
                if (remotoAntiguo != null && !remotoAntiguo.isBlank()) {
                    ConexionFtp.eliminarArchivo(remotoAntiguo);
                }
                nombreImagenParaBD = remotoNuevo;

            } else if (!esEdicion) {
                String ext = archivoImagenSeleccionado.getName()
                        .substring(archivoImagenSeleccionado.getName().lastIndexOf('.'));
                String remoto = System.currentTimeMillis() + ext;
                if (!ConexionFtp.conectar()
                        || !ConexionFtp.subirArchivo(archivoImagenSeleccionado, remoto)) {
                    Alertas.error("Error FTP", "No se pudo subir la imagen.");
                    return;
                }
                ConexionFtp.desconectar();
                nombreImagenParaBD = remoto;

            } else {
                nombreImagenParaBD = alojamientoActual.getImagen();
            }

            Alojamiento dto = new Alojamiento(
                    esEdicion ? alojamientoActual.getIdAlojamiento() : 0,
                    campoNombre.getText().trim(),
                    comboTipo.getValue().getIdTipo(),
                    campoContacto.getText().trim(),
                    nombreImagenParaBD,
                    comboDestino.getValue().getId_destino()
            );
            boolean exito = esEdicion
                    ? ConsultasAlojamientos.actualizarAlojamiento(dto)
                    : ConsultasAlojamientos.registrarAlojamiento(dto);
            if (!exito) {
                Alertas.error("Error", esEdicion
                        ? "No se pudo actualizar el alojamiento."
                        : "No se pudo registrar el alojamiento."
                );
                return;
            }

            Conexion.conectar();
            String accion = esEdicion
                    ? "Ha actualizado el alojamiento "
                    : "Ha registrado el alojamiento ";
            ConsultasMovimientos.registrarMovimiento(
                    accion + campoNombre.getText().trim(),
                    new java.util.Date(),
                    Usuario.getUsuarioActual().getIdUsuario()
            );
            Conexion.cerrarConexion();

            Alertas.informacion(esEdicion
                    ? "Alojamiento actualizado correctamente."
                    : "Alojamiento registrado correctamente."
            );

            if (esEdicion) {
                cerrarVentana();
                if (gestionAlojamientoController != null) {
                    gestionAlojamientoController.cargarAlojamientos();
                    gestionAlojamientoController.cargarAlojamientosFavoritos();
                }
            } else {
                if (gestionAlojamientoController != null) {
                    gestionAlojamientoController.cargarAlojamientos();
                    gestionAlojamientoController.cargarAlojamientosFavoritos();
                }
                campoNombre.clear();
                comboTipo.getSelectionModel().selectFirst();
                tipoSeleccionado = null;

                comboDestino.getSelectionModel().selectFirst();
                destinoSeleccionado = null;

                campoContacto.clear();
                campoImagen.clear();
                archivoImagenSeleccionado = null;

                imagenTrekNic.setImage(
                        new Image(getClass().getResourceAsStream("/img/Encabezado.png"))
                );
            }
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
                tipoSeleccionado = tipo;
                break;
            }
        }

        for (Destino dest : comboDestino.getItems()) {
            if (dest.getId_destino() == a.getIdDestino()) {
                comboDestino.setValue(dest);
                destinoSeleccionado = dest;
                break;
            }
        }

        botonRegistrar.setText("Actualizar");
        esEdicion = true;
    }

    public void setEdicionActiva(boolean editable) {
        campoNombre.setEditable(editable);
        campoContacto.setEditable(editable);
        campoImagen.setEditable(false);

        comboDestino.setDisable(!editable);
        comboTipo.setDisable(!editable);

        botonSeleccionar.setDisable(!editable);

        botonRegistrar.setVisible(editable);

        double opacidad = editable ? 1.0 : 0.75;
        campoNombre.setOpacity(opacidad);
        campoContacto.setOpacity(opacidad);
        campoImagen.setOpacity(0.75);
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
            campoImagen.setText(fichero.getName());
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
