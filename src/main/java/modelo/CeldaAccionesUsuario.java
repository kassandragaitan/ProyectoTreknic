/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import bbdd.Conexion;
import controladores.GestionUsuariosController;
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

/**
 *
 * @author k0343
 */
public class CeldaAccionesUsuario extends TableCell<Usuario, Void> {
    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");

    public CeldaAccionesUsuario() {
        botonVer.getStyleClass().add("table-button");
        botonEditar.getStyleClass().add("table-button");
        botonEliminar.getStyleClass().addAll("table-button", "red");

        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().addAll(botonVer, botonEditar, botonEliminar);

        botonVer.setOnAction(e -> {
            Usuario usuario = getTableRow().getItem();
            if (usuario != null) {
                abrirVentanaGestionUsuario(usuario, false);
            }
        });

        botonEditar.setOnAction(e -> {
            Usuario usuario = getTableRow().getItem();
            if (usuario != null) {
                abrirVentanaGestionUsuario(usuario, true); // campos activados
            }
        });

        botonEliminar.setOnAction(e -> {
            Usuario usuario = getTableRow().getItem();
            if (usuario != null) {
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar eliminación");
                confirm.setHeaderText("¿Seguro que deseas eliminar este usuario?");
                confirm.setContentText("Usuario: " + usuario.getNombre());

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (eliminarUsuarioPorId(usuario.getIdUsuario())) {
                            getTableView().getItems().remove(usuario);
                        } else {
                            Alert error = new Alert(AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("No se pudo eliminar el usuario.");
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

    private void abrirVentanaGestionUsuario(Usuario usuario, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/GestionUsuarios.fxml"));
            Parent root = loader.load();

            GestionUsuariosController controller = loader.getController();
            controller.VerCamposUsuario(usuario);
            controller.EditarCamposUsuario(editable);

            Stage stage = new Stage();
            stage.setTitle(editable ? "Editar usuario" : "Ver usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean eliminarUsuarioPorId(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}