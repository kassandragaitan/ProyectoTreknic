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
import javafx.stage.StageStyle;

public class CeldaAccionesTipoAlojamiento extends TableCell<TipoAlojamiento, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");
    private final GestionTipoDeAlojamientoController controlador;

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

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty || getTableRow().getItem() == null ? null : contenedor);
    }

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
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean eliminarTipoPorId(int id) {
        String sql = "DELETE FROM tipoalojamiento WHERE id_tipo = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
