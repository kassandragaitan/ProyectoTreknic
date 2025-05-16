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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.Categoria;

public class GestionCategoriaController implements Initializable {

    @FXML private TableView<Categoria> tablaCategoria;
    @FXML private TableColumn<Categoria, Integer> columnaIdCategoria;
    @FXML private TableColumn<Categoria, String> columnaNombre;
    @FXML private TableColumn<Categoria, String> columnaDescripcion;
    @FXML private TableColumn<Categoria, Void> columnaAcciones;
    @FXML private TableColumn<Categoria, Void> columnaInvisible;
    @FXML private TextField campoBuscarCategoria;
    @FXML private Button botonNuevaCategoria;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1) placeholder vacío y estirado
        tablaCategoria.setPlaceholder(new Label(""));
        tablaCategoria.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 2) listener buscar en tiempo real
        campoBuscarCategoria.textProperty().addListener((obs, oldV, newV) ->
            buscarCategoriasEnTiempoReal(newV)
        );

        // 3) fábrica de la columna de acciones
        columnaAcciones.setCellFactory(col -> new CeldaAccionesCategoria(this));

        // 4) carga inicial
        recargarTabla();
    }

    public void recargarTabla() {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasCategoria.cargarDatosCategorias(lista);
        Conexion.cerrarConexion();

        tablaCategoria.setItems(lista);
        columnaIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        columnaNombre    .setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion
                         .setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    private void buscarCategoriasEnTiempoReal(String texto) {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasCategoria.cargarDatosCategoriasFiltradas(lista, texto);
        Conexion.cerrarConexion();
        tablaCategoria.setItems(lista);
    }

    @FXML
    private void nuevaCategoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vistas/AgregarCategoria.fxml"));
            Parent root = loader.load();
            AgregarCategoriaController ctrl = loader.getController();
            ctrl.setGestionCategoriaController(this);

            Stage st = new Stage(StageStyle.DECORATED);
            st.setTitle("Agregar Categoría");
            st.setScene(new Scene(root));
            st.initModality(Modality.APPLICATION_MODAL);
            st.setResizable(false);
            st.showAndWait();
            recargarTabla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
