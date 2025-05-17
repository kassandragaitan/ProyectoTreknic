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
import javafx.geometry.Pos;
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
import javafx.stage.StageStyle;
import modelo.Alojamiento;
import modelo.ConexionFtp;
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
        columnaImagen.setVisible(false);
        cargarAlojamientos();
        botonQuitarFiltro.setDisable(true);

        columnaIdAlojamiento.setVisible(false);
        tablaAlojamientos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        campoBuscarAlojamiento.textProperty().addListener((obs, oldV, newV) -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);
            buscarAlojamientosEnTiempoReal(newV);
            botonQuitarFiltro.setDisable(newV.trim().isEmpty());
            comboFiltroPor.setVisible(false);
            comboFiltroPor.setVisible(true);
        });

        columnAcciones.setCellFactory(col -> new CeldaAccionesAlojamiento(this));

        ObservableList<String> filtros = FXCollections.observableArrayList(
                "Selecciona un tipo de filtro...",
                "Filtrar por tipo de alojamiento",
                "Filtrar por destino"
        );
        comboFiltroPor.setItems(filtros);
        comboFiltroPor.getSelectionModel().selectFirst();
        comboValorFiltro.setDisable(true);

        comboFiltroPor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);

            if (newVal == null || newVal.equals(filtros.get(0))) {
                cargarAlojamientos();
                botonQuitarFiltro.setDisable(campoBuscarAlojamiento.getText().trim().isEmpty());
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

            String placeholderValor = "Selecciona un valor...";
            if (!opciones.contains(placeholderValor)) {
                opciones.add(0, placeholderValor);
            }
            comboValorFiltro.setItems(opciones);
            comboValorFiltro.getSelectionModel().selectFirst();
            comboValorFiltro.setDisable(false);

            botonQuitarFiltro.setDisable(
                    campoBuscarAlojamiento.getText().trim().isEmpty()
                    && (comboValorFiltro.getValue() == null || comboValorFiltro.getValue().startsWith("Selecciona"))
            );
        });

        comboValorFiltro.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.startsWith("Selecciona")) {
                cargarAlojamientos();
                botonQuitarFiltro.setDisable(campoBuscarAlojamiento.getText().trim().isEmpty());
                return;
            }
            botonQuitarFiltro.setDisable(false);
            ObservableList<Alojamiento> lista = FXCollections.observableArrayList();
            Conexion.conectar();
            String filtro = comboFiltroPor.getValue();
            if ("Filtrar por tipo de alojamiento".equals(filtro)) {
                ConsultasAlojamientos.cargarAlojamientosPorTipo(lista, newVal);
            } else if ("Filtrar por destino".equals(filtro)) {
                ConsultasAlojamientos.cargarAlojamientosPorDestino(lista, newVal);
            }
            Conexion.cerrarConexion();
            tablaAlojamientos.setItems(lista);
        });

        botonQuitarFiltro.setOnAction(e -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.setDisable(true);
            campoBuscarAlojamiento.clear();
            cargarAlojamientos();
            botonQuitarFiltro.setDisable(true);
        });

        cargarAlojamientosFavoritos();
    }

    public void cargarAlojamientosFavoritos() {
        contenedorFavoritos.getChildren().clear();

        List<Alojamiento> favoritos = ConsultasAlojamientos
                .obtenerAlojamientosFavoritosPorUsuario(
                        Usuario.getUsuarioActual().getIdUsuario()
                );

        if (favoritos.isEmpty()) {
            Label texto = new Label("No hay alojamientos en favoritos aún.");
            texto.setStyle("-fx-text-fill: #999999; -fx-font-size: 15px; -fx-font-weight: normal;");
            texto.setWrapText(true);
            texto.setMaxWidth(400);
            texto.setAlignment(Pos.CENTER);

            VBox contenedorTexto = new VBox(texto);
            contenedorTexto.setAlignment(Pos.TOP_CENTER);
            contenedorTexto.setPrefHeight(260);
            contenedorTexto.setPadding(new javafx.geometry.Insets(40, 0, 0, 0));

            contenedorFavoritos.setAlignment(Pos.TOP_CENTER);
            contenedorFavoritos.getChildren().add(contenedorTexto);
            return;
        }

        contenedorFavoritos.setAlignment(Pos.TOP_LEFT);

        for (Alojamiento aloj : favoritos) {
            VBox tarjeta = new VBox(5);
            tarjeta.setStyle(
                    "-fx-background-color: white; -fx-padding: 10px; "
                    + "-fx-background-radius: 10px; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.2, 0, 1);"
            );
            tarjeta.setAlignment(Pos.CENTER);
            tarjeta.setPrefWidth(200);
            tarjeta.setMaxWidth(200);
            tarjeta.setPrefHeight(180);

            Label lblNombre = new Label(aloj.getNombre());
            lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label lblDestino = new Label("Destino: " + aloj.getNombreDestino());
            lblDestino.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

            Button btnQuitar = new Button("Quitar de Favoritos");
            btnQuitar.setStyle("-fx-background-color: #3874b4; -fx-text-fill: white; -fx-background-radius: 6px;");
            btnQuitar.setOnAction(evt -> {
                boolean ok = ConsultasAlojamientos
                        .eliminarDeFavoritos(
                                aloj.getIdAlojamiento(),
                                Usuario.getUsuarioActual().getIdUsuario()
                        );
                if (ok) {
                    Alertas.informacion("El alojamiento ha sido eliminado de favoritos.");
                    cargarAlojamientosFavoritos();
                } else {
                    Alertas.error("Error", "No se pudo eliminar de favoritos.");
                }
            });

            tarjeta.getChildren().addAll(lblNombre, lblDestino, btnQuitar);
            contenedorFavoritos.getChildren().add(tarjeta);
        }
    }

    private void buscarAlojamientosEnTiempoReal(String texto) {
        ObservableList<Alojamiento> lista = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasAlojamientos.cargarDatosAlojamientosFiltrados(lista, texto);
        Conexion.cerrarConexion();
        tablaAlojamientos.setItems(lista);
    }

    public void cargarAlojamientos() {
        ObservableList<Alojamiento> lista = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasAlojamientos.cargarDatosAlojamientos(lista);
        Conexion.cerrarConexion();
        tablaAlojamientos.setItems(lista);
        tablaAlojamientos.setPlaceholder(new Label("No hay alojamientos registrados."));
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
            AgregarAlojamientoController ctrl = loader.getController();
            ctrl.setGestionAlojamientoController(this);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setTitle("Agregar Alojamiento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarAlojamientos();
            cargarAlojamientosFavoritos();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
