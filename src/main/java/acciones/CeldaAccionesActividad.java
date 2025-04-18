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

    public CeldaAccionesActividad() {
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
            }
        });

        botonEliminar.setOnAction(e -> {
            Actividad actividad = getTableRow().getItem();
            if (actividad != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar eliminación");
                confirm.setHeaderText("¿Seguro que deseas eliminar esta actividad?");
                confirm.setContentText("Actividad: " + actividad.getNombre());

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (eliminarActividad(actividad.getIdActividad())) {
                            getTableView().getItems().remove(actividad);
                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("No se pudo eliminar la actividad.");
                            error.showAndWait();
                        }
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
            AgregarActividadController controller = loader.getController();
            controller.verActividad(actividad);
            controller.setEdicionActiva(editable);

            Stage stage = new Stage();
            stage.setTitle(editable ? "Editar Actividad" : "Ver Actividad");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean eliminarActividad(int id) {
        String sql = "DELETE FROM actividades WHERE id_actividad = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}