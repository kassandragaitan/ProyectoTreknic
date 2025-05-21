/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package acciones;

import bbdd.ConsultasResenas;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import modelo.Resena;

/**
 *
 * @author k0343
 */
public class CeldaAccionesResena extends TableCell<Resena, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonEliminar = new Button("Eliminar");

    public CeldaAccionesResena() {
        botonEliminar.getStyleClass().add("boton-eliminar");
        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().add(botonEliminar);

        botonEliminar.setOnAction(e -> {
            Resena resena = getTableRow().getItem();
            if (resena != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Eliminar Reseña");
                confirm.setHeaderText(null);
                confirm.setContentText("¿Deseas eliminar esta reseña de \"" + resena.getNombreDestino() + "\"?");

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean ok = ConsultasResenas.eliminarResena(resena.getIdResena());
                        if (ok) {
                            getTableView().getItems().remove(resena);
                            Alert info = new Alert(Alert.AlertType.INFORMATION);
                            info.setContentText("Reseña eliminada correctamente.");
                            info.showAndWait();
                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setContentText("No se pudo eliminar la reseña.");
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
}
