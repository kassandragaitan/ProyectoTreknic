package controladores;

import acciones.CeldaAccionesCategoria;
import bbdd.Conexion;
import bbdd.ConsultasCategoria;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.Categoria;

/**
 * Controlador JavaFX para la gestión de categorías turísticas. Permite
 * registrar, visualizar, buscar y administrar las categorías disponibles en el
 * sistema. Incluye un sistema de búsqueda en tiempo real y una columna de
 * acciones.
 *
 * Autor: k0343
 */
public class GestionCategoriaController implements Initializable {

    @FXML
    private TableView<Categoria> tablaCategoria;
    @FXML
    private TableColumn<Categoria, Integer> columnaIdCategoria;
    @FXML
    private TableColumn<Categoria, String> columnaNombre;
    @FXML
    private TableColumn<Categoria, String> columnaDescripcion;
    @FXML
    private TableColumn<Categoria, Void> columnaAcciones;
    @FXML
    private TableColumn<Categoria, Void> columnaInvisible;
    @FXML
    private TextField campoBuscarCategoria;
    @FXML
    private Button botonNuevaCategoria;

    /**
     * Inicializa la vista cargando las categorías y configurando la tabla,
     * acciones y búsqueda.
     *
     * @param url URL de inicialización.
     * @param rb ResourceBundle de recursos de localización (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablaCategoria.setPlaceholder(new Label(""));
        tablaCategoria.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        campoBuscarCategoria.textProperty().addListener((obs, oldV, newV) -> buscarCategoriasEnTiempoReal(newV));
        columnaAcciones.setCellFactory(col -> new CeldaAccionesCategoria(this));
        recargarTabla();
    }

    /**
     * Recarga los datos de la tabla de categorías desde la base de datos. Se
     * utiliza tanto al iniciar como tras registrar/editar/eliminar una
     * categoría.
     */
    public void recargarTabla() {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasCategoria.cargarDatosCategorias(lista);
        Conexion.cerrarConexion();

        tablaCategoria.setItems(lista);
        tablaCategoria.setPlaceholder(new Label("No hay categorias registradas."));
        columnaIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    /**
     * Filtra en tiempo real las categorías mostradas en la tabla según el texto
     * introducido en el campo de búsqueda.
     *
     * @param texto Texto a buscar por nombre o descripción.
     */
    private void buscarCategoriasEnTiempoReal(String texto) {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasCategoria.cargarDatosCategoriasFiltradas(lista, texto);
        Conexion.cerrarConexion();
        tablaCategoria.setItems(lista);
    }

    /**
     * Abre el formulario de registro de nueva categoría en una ventana modal.
     * Al cerrarse, actualiza la tabla con los posibles cambios.
     *
     * @param event Evento generado al presionar el botón "Nueva Categoría".
     */
    @FXML
    private void nuevaCategoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/vistas/AgregarCategoria.fxml"));
            Parent root = loader.load();
            AgregarCategoriaController ctrl = loader.getController();
            ctrl.setTitulo("Agregar Categoría");
            ctrl.setGestionCategoriaController(this);

            Stage st = new Stage(StageStyle.DECORATED);
            st.setTitle("Agregar Categoría");
            st.setScene(new Scene(root));
            st.initModality(Modality.APPLICATION_MODAL);
            st.setResizable(false);
            st.getIcons().add(new Image("/img/montanita.png"));
            st.showAndWait();
            recargarTabla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
