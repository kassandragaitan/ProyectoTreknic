/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import bbdd.ConsultasUsuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import acciones.CeldaAccionesUsuario;
import java.io.IOException;
import modelo.Usuario;
import java.text.SimpleDateFormat;
import javafx.scene.control.ComboBox;
import javafx.stage.StageStyle;

public class GestionUsuarioController implements Initializable {

    @FXML
    private TableColumn<Usuario, String> columnUsuario;
    @FXML
    private TableColumn<Usuario, String> columnCorreo;
    @FXML
    private TableColumn<Usuario, String> columnRol;
    @FXML
    private TableColumn<Usuario, Integer> columnTelefono;
    @FXML
    private TableColumn<Usuario, Date> columnFechaRegistro;
    @FXML
    private TableColumn<Usuario, Void> columnAcciones;
    @FXML
    private Button botonNuevoUsuario;
    @FXML
    private TableColumn<Usuario, String> columnIdioma;
    @FXML
    private TableColumn<Usuario, String> columnViajero;
    @FXML
    private TableColumn<Usuario, String> columnContrasena;
    @FXML
    private TextField campoBuscarUsuario;
    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private ComboBox<String> comboFiltroPor;
    @FXML
    private ComboBox<String> comboValorFiltro;
    @FXML
    private Button botonQuitarFiltro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recargarTabla();
        columnCorreo.setVisible(false);
        columnContrasena.setVisible(false);
        columnUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnContrasena.setCellValueFactory(new PropertyValueFactory<>("contrasena"));
        columnRol.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));
        columnTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnIdioma.setCellValueFactory(new PropertyValueFactory<>("idioma"));
        columnViajero.setCellValueFactory(new PropertyValueFactory<>("tipoViajero"));
        columnFechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));

        columnFechaRegistro.setStyle("-fx-alignment: CENTER;");
        columnAcciones.setStyle("-fx-alignment: CENTER;");

        tablaUsuarios.setItems(generarDatosUsuario());
        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaUsuarios.toFront();
        initializeCustomCells();

        botonQuitarFiltro.setDisable(true);

        campoBuscarUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);

            buscarUsuariosEnTiempoReal(newValue);

            botonQuitarFiltro.setDisable(newValue.trim().isEmpty());
        });

        ObservableList<String> filtros = FXCollections.observableArrayList(
                "Selecciona un tipo de filtro...",
                "Filtrar por rol",
                "Filtrar por idioma",
                "Filtrar por tipo de compañía"
        );
        comboFiltroPor.setItems(filtros);
        comboFiltroPor.getSelectionModel().selectFirst();

        comboValorFiltro.setDisable(true);

        comboFiltroPor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);

            if (newVal == null || newVal.equals("Selecciona un tipo de filtro...")) {
                tablaUsuarios.setItems(generarDatosUsuario());
                botonQuitarFiltro.setDisable(true);
                return;
            }

            ObservableList<String> opciones = FXCollections.observableArrayList();
            String placeholder = "Selecciona un valor...";

            Conexion.conectar();
            switch (newVal) {
                case "Filtrar por rol":
                    opciones = ConsultasUsuario.cargarRolesUsuarios();
                    opciones.remove("Todos los roles");
                    break;
                case "Filtrar por idioma":
                    ConsultasUsuario.cargarComboIdioma(comboValorFiltro);
                    Conexion.cerrarConexion();
                    comboValorFiltro.setDisable(false);
                    return;
                case "Filtrar por tipo de compañía":
                    ConsultasUsuario.cargarComboTipoCompania(comboValorFiltro);
                    Conexion.cerrarConexion();
                    comboValorFiltro.setDisable(false);
                    return;
            }
            Conexion.cerrarConexion();

            if (!opciones.contains(placeholder)) {
                opciones.add(0, placeholder);
            }

            comboValorFiltro.setItems(opciones);
            comboValorFiltro.getSelectionModel().selectFirst();
            comboValorFiltro.setDisable(false);

            tablaUsuarios.setItems(generarDatosUsuario());
        });

        comboValorFiltro.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
                String filtro = comboFiltroPor.getValue();

                if ("Filtrar por rol".equals(filtro)) {
                    Conexion.conectar();
                    ConsultasUsuario.cargarUsuariosPorRol(listaUsuarios, newVal);
                    Conexion.cerrarConexion();
                } else {
                    String campo = "";
                    if ("Filtrar por idioma".equals(filtro)) {
                        campo = "idioma_preferido";
                    } else if ("Filtrar por tipo de compañía".equals(filtro)) {
                        campo = "tipo_viajero";
                    }

                    ConsultasUsuario.cargarUsuariosPorCampo(listaUsuarios, campo, newVal);
                }
                tablaUsuarios.setItems(listaUsuarios);
                botonQuitarFiltro.setDisable(false);
            }
        });

        botonQuitarFiltro.setOnAction(e -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);
            campoBuscarUsuario.clear();
            tablaUsuarios.setItems(generarDatosUsuario());
            comboFiltroPor.setVisible(false);
            comboFiltroPor.setVisible(true);
            botonQuitarFiltro.setDisable(true);
        });

    }

    private void buscarUsuariosEnTiempoReal(String texto) {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasUsuario.cargarDatosUsuariosFiltrados(listaUsuarios, texto);
        Conexion.cerrarConexion();
        tablaUsuarios.setItems(listaUsuarios);
    }

    public ObservableList<Usuario> generarDatosUsuario() {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            String query = "SELECT id_usuario, nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro FROM usuarios";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getString("tipo_usuario"),
                        rs.getDate("fecha_registro"),
                        rs.getString("tipo_viajero"),
                        rs.getString("idioma_preferido"),
                        rs.getString("telefono")
                );
                listaUsuarios.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaUsuarios;
    }

    public void recargarTabla() {
        ObservableList<Usuario> listaUsuarios = generarDatosUsuario();
        tablaUsuarios.setItems(listaUsuarios);
        tablaUsuarios.setPlaceholder(new Label("No hay usuarios registrados."));
    }

    private void initializeCustomCells() {
        columnAcciones.setCellFactory(col -> new CeldaAccionesUsuario(this));

        columnUsuario.setCellFactory(col -> new TableCell<Usuario, String>() {
            @Override
            protected void updateItem(String nombre, boolean empty) {
                super.updateItem(nombre, empty);
                if (empty || nombre == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    HBox contenedor = new HBox(10);
                    contenedor.setAlignment(Pos.CENTER_LEFT);
                    Label avatar = new Label("\uD83D\uDC64");
                    avatar.getStyleClass().add("user-avatar");

                    VBox info = new VBox(2);
                    Label nombreLabel = new Label(nombre);
                    nombreLabel.getStyleClass().add("user-name");

                    Label correoLabel = new Label(getTableRow().getItem().getEmail());
                    correoLabel.getStyleClass().add("user-email");

                    info.getChildren().addAll(nombreLabel, correoLabel);
                    contenedor.getChildren().addAll(avatar, info);
                    setGraphic(contenedor);
                }
            }
        });

        columnRol.setCellFactory(col -> new TableCell<Usuario, String>() {
            @Override
            protected void updateItem(String rol, boolean empty) {
                super.updateItem(rol, empty);
                if (empty || rol == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(rol);
                    badge.getStyleClass().add(
                            rol.equalsIgnoreCase("admin") ? "rol-admin" : "rol-visualizador"
                    );
                    HBox container = new HBox(badge);
                    container.setAlignment(Pos.CENTER);
                    setGraphic(container);
                }
            }
        });

        columnFechaRegistro.setCellFactory(col -> new TableCell<Usuario, Date>() {
            private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            protected void updateItem(Date fecha, boolean empty) {
                super.updateItem(fecha, empty);
                setText(empty || fecha == null ? null : formatter.format(fecha));
            }
        });
    }

    @FXML
    private void irAnuevoUsuario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarUsuario.fxml"));
            Parent root = loader.load();

            AgregarUsuarioController controlador = loader.getController();
            controlador.setTitulo("Agregar Usuario");
            controlador.setAdministracionUsuarioController(this);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.setUserData(this);
            stage.setScene(scene);
            stage.setTitle("Agregar Usuario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.showAndWait();

            if (controlador.getModificado()) {
                recargarTabla();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
