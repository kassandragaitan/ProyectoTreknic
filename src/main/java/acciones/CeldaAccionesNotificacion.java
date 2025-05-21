package acciones;

import bbdd.ConsultasNotificaciones;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import modelo.Notificacion;

/**
 * Celda personalizada para mostrar un botón de eliminación en la tabla de
 * notificaciones. Permite eliminar notificaciones directamente desde la
 * interfaz.
 */
public class CeldaAccionesNotificacion extends TableCell<Notificacion, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonEliminar = new Button("Eliminar");

    /**
     * Constructor que inicializa el botón de eliminación y su comportamiento.
     */
    public CeldaAccionesNotificacion() {
        botonEliminar.getStyleClass().addAll("table-button", "red");
        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().add(botonEliminar);

        botonEliminar.setOnAction(e -> {
            Notificacion notif = getTableRow().getItem();
            if (notif != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Eliminar Notificación");
                confirm.setHeaderText(null);
                confirm.setContentText("¿Deseas eliminar la notificación:\n\n" + notif.getDescripcion() + "?");

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean ok = ConsultasNotificaciones.eliminarNotificacion(notif.getIdNotificacion());
                        if (ok) {
                            getTableView().getItems().remove(notif);

                            Alert exito = new Alert(Alert.AlertType.INFORMATION);
                            exito.setTitle(null);
                            exito.setHeaderText(null);
                            exito.setContentText("Notificación eliminada correctamente.");
                            exito.showAndWait();
                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText(null);
                            error.setContentText("No se pudo eliminar la notificación.");
                            error.showAndWait();
                        }
                    }
                });
            }
        });
    }

    /**
     * Método que actualiza la celda gráfica dependiendo de si está vacía o
     * contiene una notificación.
     *
     * @param item Elemento del tipo Void (no utilizado).
     * @param empty true si la celda está vacía, false si contiene un elemento.
     */
    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty || getTableRow().getItem() == null ? null : contenedor);
    }
}
