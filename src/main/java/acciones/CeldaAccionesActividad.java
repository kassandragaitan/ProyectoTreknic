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

public class CeldaAccionesActividad extends TableCell<Actividad, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");
    private final GestionActividadesController controller;

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

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow().getItem() == null) {
            setGraphic(null);
        } else {
            setGraphic(contenedor);
        }
    }

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

    private boolean eliminarActividad(int id) {
        String sql = "DELETE FROM actividades WHERE id_actividad = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
