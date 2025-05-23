/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package acciones;

import bbdd.Conexion;
import bbdd.ConsultasNotificaciones;
import controladores.AgregarTipoAlojamientoController;
import controladores.GestionTipoDeAlojamientoController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.TipoAlojamiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import modelo.Usuario;

/**
 *
 * @author k0343
 */
/**
 * Celda personalizada para la tabla de tipos de alojamiento. Proporciona
 * botones para ver, editar o eliminar cada tipo de alojamiento.
 */
public class CeldaAccionesTipoAlojamiento extends TableCell<TipoAlojamiento, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");
    private final GestionTipoDeAlojamientoController controlador;

    /**
     * Constructor que inicializa los botones de acción y define su
     * comportamiento.
     *
     * @param controlador Controlador de la vista de gestión de tipos de
     * alojamiento.
     */
    public CeldaAccionesTipoAlojamiento(GestionTipoDeAlojamientoController controlador) {
        this.controlador = controlador;
        botonVer.getStyleClass().add("table-button");
        botonEditar.getStyleClass().add("table-button");
        botonEliminar.getStyleClass().addAll("table-button", "red");

        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().addAll(botonVer, botonEditar, botonEliminar);

        botonVer.setOnAction(e -> abrirVentana(getTableRow().getItem(), false));
        botonEditar.setOnAction(e -> abrirVentana(getTableRow().getItem(), true));

        botonEliminar.setOnAction(e -> {
            TipoAlojamiento tipo = getTableRow().getItem();
            if (tipo != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar eliminación");
                confirm.setHeaderText("¿Seguro que deseas eliminar este tipo de alojamiento?");
                confirm.setContentText("Tipo: " + tipo.getTipo());

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (eliminarTipoPorId(tipo.getIdTipo())) {
                            getTableView().getItems().remove(tipo);
                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("No se pudo eliminar el tipo de alojamiento.");
                            error.showAndWait();
                        }
                    }
                });
            }
        });
    }

    /**
     * Actualiza el contenido de la celda gráfica dependiendo de si está vacía o
     * tiene datos.
     *
     * @param item Elemento del tipo Void (no utilizado).
     * @param empty true si la celda está vacía, false si contiene datos.
     */
    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty || getTableRow().getItem() == null ? null : contenedor);
    }

    /**
     * Abre una ventana para ver o editar un tipo de alojamiento.
     *
     * @param tipo Objeto TipoAlojamiento que se desea ver o editar.
     * @param editable true para permitir edición, false para solo
     * visualización.
     */
    private void abrirVentana(TipoAlojamiento tipo, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarTipoAlojamiento.fxml"));
            Parent root = loader.load();

            AgregarTipoAlojamientoController controller = loader.getController();
            controller.setTitulo(editable ? "Editar Tipo de Alojamiento" : "Ver Tipo de Alojamiento");
            controller.verTipoAlojamiento(tipo);
            controller.setEdicionActiva(editable);
            controller.setGestionTipoAlojamientoController(controlador);

            Stage stage = new Stage();
            stage.setTitle(editable ? "Editar Tipo de Alojamiento" : "Ver Tipo de Alojamiento");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.getIcons().add(new Image("/img/montanita.png"));
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Elimina un tipo de alojamiento de la base de datos por su ID. También
     * registra el movimiento en el sistema de notificaciones.
     *
     * @param id ID del tipo de alojamiento a eliminar.
     * @return true si se eliminó correctamente, false si hubo un error.
     */
    private boolean eliminarTipoPorId(int id) {
        String sql = "DELETE FROM tipoalojamiento WHERE id_tipo = ?";
        TipoAlojamiento tipo = getTableRow().getItem();
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            boolean eliminado = stmt.executeUpdate() > 0;
            if (eliminado && tipo != null) {
                String mensaje = "Ha eliminado el tipo de alojamiento " + tipo.getTipo();
                int idUsuario = Usuario.getUsuarioActual().getIdUsuario();
                ConsultasNotificaciones.registrarMovimiento(
                        mensaje,
                        new Date(),
                        idUsuario
                );
            }
            return eliminado;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Conexion.cerrarConexion();
        }
    }

}
