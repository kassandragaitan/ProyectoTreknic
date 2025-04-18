/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Animacion;
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
import java.time.LocalDate;
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
import modelo.Usuario;
import java.text.SimpleDateFormat;
import javafx.scene.control.ComboBox;

public class AdministracionUsuarioController implements Initializable {

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
    private ComboBox<String> comboRoles;
    @FXML
    private ComboBox<String> comboEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        campoBuscarUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarUsuariosEnTiempoReal(newValue);
        });

        // Cargar roles desde la BBDD (solo UNA VEZ)
        Conexion.conectar();
        ObservableList<String> roles = ConsultasUsuario.cargarRolesUsuarios();
        Conexion.cerrarConexion();

        comboRoles.setItems(roles);
        comboRoles.getSelectionModel().selectFirst();

        comboRoles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filtrarUsuariosPorRol(newValue);
        });
        Conexion.conectar();
        ObservableList<String> estados = ConsultasUsuario.cargarEstadosUsuarios();
        Conexion.cerrarConexion();

        comboEstado.setItems(estados);
        comboEstado.getSelectionModel().selectFirst();

        comboEstado.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarUsuariosPorEstado(newVal);
        });

    }

    private void filtrarUsuariosPorRol(String rolSeleccionado) {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasUsuario.cargarUsuariosPorRol(listaUsuarios, rolSeleccionado);
        Conexion.cerrarConexion();
        tablaUsuarios.setItems(listaUsuarios);
    }

    private void filtrarUsuariosPorEstado(String estadoSeleccionado) {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasUsuario.cargarUsuariosPorEstado(listaUsuarios, estadoSeleccionado);
        Conexion.cerrarConexion();
        tablaUsuarios.setItems(listaUsuarios);
    }

    private void buscarUsuariosEnTiempoReal(String texto) {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasUsuario.cargarDatosUsuariosFiltrados(listaUsuarios, texto);
        Conexion.cerrarConexion();
        tablaUsuarios.setItems(listaUsuarios);
    }

    private ObservableList<Usuario> generarDatosUsuario() {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            String query = "SELECT id_usuario, nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro, activo FROM usuarios";
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
                usuario.setActivo(rs.getBoolean("activo"));
                listaUsuarios.add(usuario); // ✅ nombre correcto
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaUsuarios;
    }

    private void initializeCustomCells() {
        columnAcciones.setCellFactory(col -> new CeldaAccionesUsuario());

        columnUsuario.setCellFactory(col -> new TableCell<Usuario, String>() {
            @Override
            protected void updateItem(String nombre, boolean empty) {
                super.updateItem(nombre, empty);
                if (empty || nombre == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    HBox contenedor = new HBox(10);
                    contenedor.setAlignment(Pos.CENTER_LEFT);

                    Label avatar = new Label("👤");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/GestionUsuarios.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
