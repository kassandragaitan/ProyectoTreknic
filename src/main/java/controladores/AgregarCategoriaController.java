package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasCategoria;
import bbdd.ConsultasMovimientos;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Categoria;
import modelo.Usuario;

public class AgregarCategoriaController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;

    private GestionCategoriaController gestionCategoriaController;
    private Categoria categoriaActual;
    private boolean esEdicion = false;
    private boolean modificado = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    public void verCategoria(Categoria cat) {
        this.categoriaActual = cat;
        this.esEdicion = true;
        campoNombre.setText(cat.getNombre());
        campoDescripcion.setText(cat.getDescripcion());
        botonRegistrar.setText("Actualizar");
    }

    public void setEdicionActiva(boolean editable) {
        campoNombre.setEditable(editable);
        campoDescripcion.setEditable(editable);
        botonRegistrar.setVisible(editable);

        double op = editable ? 1.0 : 0.75;
        campoNombre.setOpacity(op);
        campoDescripcion.setOpacity(op);
    }

    public boolean getModificado() {
        return modificado;
    }

    public void setGestionCategoriaController(GestionCategoriaController controller) {
        this.gestionCategoriaController = controller;
    }

    @FXML
    private void RegistrarCategoria(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
            return;
        }else  if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.aviso("Campo vacío", "La descripción no puede estar vacía.");
            return;
        }
        String nombre = campoNombre.getText().trim();
        String descripcion = campoDescripcion.getText().trim();

        if (!esEdicion && ConsultasCategoria.existeCategoria(nombre)) {
            Alertas.aviso("Duplicado", "Ya existe una categoría con ese nombre.");
            return;
        }

        boolean exito;
        if (esEdicion) {
            categoriaActual.setNombre(nombre);
            categoriaActual.setDescripcion(descripcion);
            exito = ConsultasCategoria.actualizarCategoria(categoriaActual);
        } else {
            Categoria nueva = new Categoria(nombre, descripcion);
            exito = ConsultasCategoria.registrarCategoria(nueva);
        }

        if (exito) {
            Conexion.conectar();
            String accion = esEdicion
                    ? "Ha actualizado la categoría " + nombre
                    : "Ha registrado la categoría " + nombre;
            ConsultasMovimientos.registrarMovimiento(
                    accion,
                    Date.valueOf(LocalDate.now()),
                    Usuario.getUsuarioActual().getIdUsuario()
            );
            Conexion.cerrarConexion();

            Alertas.informacion(esEdicion
                    ? "Categoría actualizada correctamente."
                    : "Categoría registrada exitosamente."
            );

            modificado = true;

            if (gestionCategoriaController != null) {
                gestionCategoriaController.recargarTabla();
            }

            if (esEdicion) {
                ((Stage) botonRegistrar.getScene().getWindow()).close(); 
            } else {
                campoNombre.clear();
                campoDescripcion.clear();
            }

        } else {
            Alertas.error("Error", esEdicion
                    ? "No se pudo actualizar la categoría."
                    : "No se pudo registrar la categoría."
            );
        }

    }
}
