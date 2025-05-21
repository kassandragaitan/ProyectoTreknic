/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package acciones;

/**
 *
 * @author k0343
 */
import bbdd.Conexion;
import controladores.AgregarActividadController;
import controladores.GestionActividadesController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Actividad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase personalizada para la columna de acciones en la tabla de actividades.
 * Incluye botones para ver, editar y eliminar una actividad.
 */
public class CeldaAccionesActividad extends TableCell<Actividad, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");
    private final GestionActividadesController controller;

    /**
     * Constructor que inicializa los botones y sus eventos asociados.
     *
     * @param controller Referencia al controlador principal de la gestión de
     * actividades.
     */
    public CeldaAccionesActividad(GestionActividadesController controller) {
        this.controller = controller;

        botonVer.getStyleClass().add("table-button");
        botonEditar.getStyleClass().add("table-button");
        botonEliminar.getStyleClass().addAll("table-button", "red");

        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().addAll(botonVer, botonEditar, botonEliminar);

        botonVer.setOnAction(e -> {
            Actividad actividad = getTableRow().getItem();
            if (actividad != null) {
                abrirVentana(actividad, false);
            }
        });

        botonEditar.setOnAction(e -> {
            Actividad actividad = getTableRow().getItem();
            if (actividad != null) {
                abrirVentana(actividad, true);
                controller.recargarTabla();
            }
        });

        botonEliminar.setOnAction(e -> {
            Actividad actividad = getTableRow().getItem();
            if (actividad != null) {
                Alert confirm = new Alert(AlertType.CONFIRMATION,
                        "¿Eliminar actividad \"" + actividad.getNombre() + "\"?",
                        ButtonType.OK, ButtonType.CANCEL);
                confirm.setHeaderText("¿Estás seguro que quieres eliminar esta actividad?");
                confirm.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.OK && eliminarActividad(actividad.getIdActividad())) {
                        controller.recargarTabla();
                    }
                });
            }
        });
    }

    /**
     * Actualiza la celda de la tabla con los botones de acción si hay un
     * elemento válido.
     *
     * @param item Elemento de tipo Void (no se usa).
     * @param empty Indica si la celda está vacía.
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
     * Abre una ventana para ver o editar una actividad según el modo
     * especificado.
     *
     * @param actividad Actividad que se desea ver o editar.
     * @param editable True para modo edición, false para solo visualización.
     */
    private void abrirVentana(Actividad actividad, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarActividad.fxml"));
            Parent root = loader.load();
            AgregarActividadController controllerVentana = loader.getController();
            controllerVentana.setTitulo(editable ? "Editar Actividad" : "Ver Actividad");
            controllerVentana.setGestionActividadesController(controller);

            controllerVentana.verActividad(actividad);
            controllerVentana.setEdicionActiva(editable);

            Stage stage = new Stage();
            stage.setTitle(editable ? "Editar Actividad" : "Ver Actividad");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Elimina una actividad de la base de datos por su ID. También registra el
     * movimiento en el historial de notificaciones si se elimina con éxito.
     *
     * @param id ID de la actividad a eliminar.
     * @return true si la actividad fue eliminada exitosamente, false en caso
     * contrario.
     */
    private boolean eliminarActividad(int id) {
        Actividad actividad = getTableRow().getItem();

        String sql = "DELETE FROM actividades WHERE id_actividad = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            boolean eliminado = stmt.executeUpdate() > 0;
            if (eliminado && actividad != null) {
                String mensaje = "Ha eliminado la actividad: " + actividad.getNombre();
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
