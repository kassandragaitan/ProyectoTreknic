/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import acciones.CeldaAccionesAlojamiento;
import bbdd.Conexion;
import bbdd.ConsultasAlojamientos;
import java.net.URL;
import java.util.List;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Alojamiento;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class GestionAlojamientoController implements Initializable {

    @FXML
    private TableView<Alojamiento> tablaAlojamientos;
    @FXML
    private TableColumn<Alojamiento, Integer> columnaIdAlojamiento;
    @FXML
    private TableColumn<Alojamiento, String> columnaNombre;
    @FXML
    private TableColumn<Alojamiento, String> columnaTipo;
    @FXML
    private TableColumn<Alojamiento, String> columnaContacto;
    @FXML
    private TableColumn<Alojamiento, String> columnaImagen;
    @FXML
    private TableColumn<Alojamiento, String> columnaIdDestino;
    @FXML
    private TableColumn<Alojamiento, Void> columnAcciones;
    @FXML
    private TextField campoBuscarAlojamiento;
    @FXML
    private Button botonNuevoAlojamiento;
    @FXML
    private Button botonQuitarFiltro;
    @FXML
    private ComboBox<String> comboFiltroPor;
    @FXML
    private ComboBox<String> comboValorFiltro;
    @FXML
    private FlowPane contenedorFavoritos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarAlojamientos();

        columnaIdAlojamiento.setVisible(false);
        tablaAlojamientos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        campoBuscarAlojamiento.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarAlojamientosEnTiempoReal(newValue);
        });

        columnAcciones.setCellFactory(col -> new CeldaAccionesAlojamiento(this));
        ObservableList<String> opcionesFiltro = FXCollections.observableArrayList("Filtrar por tipo de alojamiento", "Filtrar por destino");
        comboFiltroPor.setItems(opcionesFiltro);
        comboFiltroPor.setPromptText("Selecciona un tipo de filtro...");
        comboFiltroPor.getSelectionModel().clearSelection();
        comboValorFiltro.setDisable(true);

        comboFiltroPor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);

            if (newVal == null) {
                cargarAlojamientos();
                return;
            }

            ObservableList<String> opciones = FXCollections.observableArrayList();
            Conexion.conectar();

            if ("Filtrar por destino".equals(newVal)) {
                opciones = ConsultasAlojamientos.cargarDestinosAlojamiento();
            } else if ("Filtrar por tipo de alojamiento".equals(newVal)) {
                opciones = ConsultasAlojamientos.cargarTiposAlojamiento();
            }

            Conexion.cerrarConexion();
            comboValorFiltro.setItems(opciones);
            comboValorFiltro.setDisable(false);
            cargarAlojamientos();
        });

        comboValorFiltro.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ObservableList<Alojamiento> lista = FXCollections.observableArrayList();
                String filtro = comboFiltroPor.getValue();

                Conexion.conectar();
                if ("Filtrar por tipo de alojamiento".equals(filtro)) {
                    ConsultasAlojamientos.cargarAlojamientosPorTipo(lista, newVal);
                } else if ("Filtrar por destino".equals(filtro)) {
                    ConsultasAlojamientos.cargarAlojamientosPorDestino(lista, newVal);
                }
                Conexion.cerrarConexion();
                tablaAlojamientos.setItems(lista);
            }
        });

        botonQuitarFiltro.setOnAction(e -> {
            comboFiltroPor.getSelectionModel().clearSelection();
            comboFiltroPor.setValue(null);
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.setDisable(true);
            campoBuscarAlojamiento.clear();
            cargarAlojamientos();
            comboFiltroPor.setVisible(false);
            comboFiltroPor.setVisible(true);
        });

        cargarAlojamientosFavoritos();

    }

    private void cargarAlojamientosFavoritos() {
        contenedorFavoritos.getChildren().clear();
        List<Alojamiento> favoritos = ConsultasAlojamientos.obtenerAlojamientosFavoritosPorUsuario(Usuario.getUsuarioActual().getIdUsuario());

        for (Alojamiento aloj : favoritos) {
            VBox tarjeta = new VBox(5);
            tarjeta.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-background-radius: 10px; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.2, 0, 1); -fx-alignment: center;");

            String urlImagen = "http://localhost/carpetaimg/alojamientos/" + aloj.getImagen();
            ImageView imagen = new ImageView(new Image(urlImagen));
            imagen.setFitWidth(200);
            imagen.setFitHeight(130);
            imagen.setPreserveRatio(true);

            Label nombre = new Label(aloj.getNombre());
            nombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label destino = new Label("Destino: " + aloj.getNombreDestino());
            destino.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

            Button botonFavorito = new Button("Quitar de Favoritos");
            botonFavorito.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
            botonFavorito.setOnAction(e -> {
                boolean eliminado = ConsultasAlojamientos.eliminarDeFavoritos(aloj.getIdAlojamiento(), Usuario.getUsuarioActual().getIdUsuario());
                if (eliminado) {
                    Alertas.informacion("El alojamiento ha sido eliminado de favoritos.");//"Favorito eliminado",
                    cargarAlojamientosFavoritos(); // Recarga para actualizar la vista
                } else {
                    Alertas.error("Error", "No se pudo eliminar el alojamiento de favoritos.");
                }
            });

            tarjeta.getChildren().addAll(imagen, nombre, destino, botonFavorito);
            contenedorFavoritos.getChildren().add(tarjeta);
        }
    }

    private void buscarAlojamientosEnTiempoReal(String texto) {
        ObservableList<Alojamiento> listaAlojamientos = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasAlojamientos.cargarDatosAlojamientosFiltrados(listaAlojamientos, texto);
        Conexion.cerrarConexion();
        tablaAlojamientos.setItems(listaAlojamientos);
    }

    public void recargarFavoritos() {
        cargarAlojamientosFavoritos();
    }

    public void cargarAlojamientos() {
        ObservableList<Alojamiento> listaAlojamientos = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasAlojamientos.cargarDatosAlojamientos(listaAlojamientos);
        Conexion.cerrarConexion();

        tablaAlojamientos.setItems(listaAlojamientos);
        columnaIdAlojamiento.setCellValueFactory(new PropertyValueFactory<>("idAlojamiento"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("nombreTipo"));
        columnaContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        columnaImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
        columnaIdDestino.setCellValueFactory(new PropertyValueFactory<>("nombreDestino"));
    }

    @FXML
    private void nuevoAlojamiento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarAlojamiento.fxml"));
            Parent root = loader.load();

            // Pasar la referencia del controlador principal al controlador de agregar alojamiento
            AgregarAlojamientoController controlador = loader.getController();
            controlador.setGestionAlojamientoController(this);  // Aquí se pasa la referencia

            Stage stage = new Stage();
            stage.setTitle("Agregar Alojamiento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
