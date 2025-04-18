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
import controladores.AgregarAlojamientoController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import modelo.Alojamiento;
public class CeldaAccionesAlojamiento extends TableCell<Alojamiento, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");

    public CeldaAccionesAlojamiento() {
        botonVer.getStyleClass().add("table-button");
        botonEditar.getStyleClass().add("table-button");
        botonEliminar.getStyleClass().addAll("table-button", "red");

        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().addAll(botonVer, botonEditar, botonEliminar);

        botonVer.setOnAction(e -> {
            Alojamiento alojamiento = getTableRow().getItem();
            if (alojamiento != null) {
                abrirVentanaGestionAlojamiento(alojamiento, false);
            }
        });

        botonEditar.setOnAction(e -> {
            Alojamiento alojamiento = getTableRow().getItem();
            if (alojamiento != null) {
                abrirVentanaGestionAlojamiento(alojamiento, true);
            }
        });

        botonEliminar.setOnAction(e -> {
            Alojamiento alojamiento = getTableRow().getItem();
            if (alojamiento != null) {
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar eliminación");
                confirm.setHeaderText("¿Seguro que deseas eliminar este alojamiento?");
                confirm.setContentText("Alojamiento: " + alojamiento.getNombre());

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (eliminarAlojamientoPorId(alojamiento.getIdAlojamiento())) {
                            getTableView().getItems().remove(alojamiento);
                        } else {
                            Alert error = new Alert(AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("No se pudo eliminar el alojamiento.");
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

    private void abrirVentanaGestionAlojamiento(Alojamiento alojamiento, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarAlojamiento.fxml"));
            Parent root = loader.load();

            AgregarAlojamientoController controller = loader.getController();
            controller.verAlojamiento(alojamiento);
            controller.setEdicionActiva(editable);

            Stage stage = new Stage();
            stage.setTitle(editable ? "Editar Alojamiento" : "Ver Alojamiento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean eliminarAlojamientoPorId(int idAlojamiento) {
        String sql = "DELETE FROM alojamientos WHERE id_alojamiento = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlojamiento);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}