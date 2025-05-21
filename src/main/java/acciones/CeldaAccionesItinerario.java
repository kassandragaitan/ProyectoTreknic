package acciones;

import bbdd.Conexion;
import controladores.AgregarItinerarioController;
import controladores.GestionItinerarioController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import javafx.stage.StageStyle;
import modelo.Itinerario;

/**
 *
 * @author k0343
 */
/**
 * Celda personalizada para mostrar botones de acción en cada fila de la tabla
 * de itinerarios. Permite ver, editar y eliminar un itinerario.
 */
public class CeldaAccionesItinerario extends TableCell<Itinerario, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");
    private final GestionItinerarioController gestionItinerarioController;

    /**
     * Constructor que inicializa los botones y define sus eventos.
     *
     * @param controller Controlador de la vista de gestión de itinerarios.
     */
    public CeldaAccionesItinerario(GestionItinerarioController controller) {
        this.gestionItinerarioController = controller;

        botonVer.getStyleClass().add("table-button");
        botonEditar.getStyleClass().add("table-button");
        botonEliminar.getStyleClass().addAll("table-button", "red");

        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().addAll(botonVer, botonEditar, botonEliminar);

        botonVer.setOnAction(e -> {
            Itinerario itinerario = getTableRow().getItem();
            if (itinerario != null) {
                abrirVentanaGestionItinerario(itinerario, false);
            }
        });

        botonEditar.setOnAction(e -> {
            Itinerario itinerario = getTableRow().getItem();
            if (itinerario != null) {
                abrirVentanaGestionItinerario(itinerario, true);
            }
        });

        botonEliminar.setOnAction(e -> {
            Itinerario itinerario = getTableRow().getItem();
            if (itinerario != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar eliminación");
                confirm.setHeaderText("¿Seguro que deseas eliminar este itinerario?");
                confirm.setContentText("Itinerario: " + itinerario.getNombre());

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (eliminarItinerarioPorId(itinerario.getIdItinerario())) {
                            getTableView().getItems().remove(itinerario);

                            Alert exito = new Alert(Alert.AlertType.INFORMATION);
                            exito.setTitle("Eliminado");
                            exito.setHeaderText("Itinerario eliminado correctamente.");
                            exito.setContentText("Se ha eliminado el itinerario: " + itinerario.getNombre());
                            exito.showAndWait();
                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("No se pudo eliminar el itinerario.");
                            error.showAndWait();
                        }
                    }
                });
            }
        });
    }

    /**
     * Actualiza el contenido gráfico de la celda si contiene datos.
     *
     * @param item Elemento del tipo Void (no se usa).
     * @param empty true si la celda está vacía, false si contiene un
     * itinerario.
     */
    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow().getItem() == null) {
            setGraphic(null);
        } else {
            setGraphic(contenedor);
        }
    }

    /**
     * Abre una ventana modal para visualizar o editar un itinerario.
     *
     * @param itinerario Itinerario a visualizar o editar.
     * @param editable true si se permite editar, false si es solo lectura.
     * @return true si el itinerario fue modificado, false en caso contrario.
     */
    private boolean abrirVentanaGestionItinerario(Itinerario itinerario, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarItinerario.fxml"));
            Parent root = loader.load();

            AgregarItinerarioController controller = loader.getController();
            controller.setGestionItinerarioController(gestionItinerarioController);
            String tituloPanel = editable
                    ? "Editar Itinerario"
                    : "Ver Itinerario";
            controller.setTitulo(tituloPanel);
            controller.verItinerario(itinerario);
            controller.setEdicionActiva(editable);

            Stage stage = new Stage();
            stage.setTitle(editable ? "Editar Itinerario" : "Ver Itinerario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.showAndWait();

            return editable && controller.getModificado();

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un itinerario de la base de datos según su ID. También registra
     * el movimiento como notificación.
     *
     * @param idItinerario ID del itinerario a eliminar.
     * @return true si se eliminó correctamente, false si ocurrió un error.
     */
    private boolean eliminarItinerarioPorId(int idItinerario) {
        String sql = "DELETE FROM itinerarios WHERE id_itinerario = ?";
        Itinerario itinerario = getTableRow().getItem();
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idItinerario);
            boolean eliminado = stmt.executeUpdate() > 0;
            if (eliminado && itinerario != null) {
                String mensaje = "Ha eliminado el itinerario: " + itinerario.getNombre();
                int idUsuario = modelo.Usuario.getUsuarioActual().getIdUsuario();
                bbdd.ConsultasNotificaciones.registrarMovimiento(
                        mensaje,
                        new java.util.Date(),
                        idUsuario
                );
            }
            return eliminado;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Conexion.cerrarConexion();
        }
    }

}
