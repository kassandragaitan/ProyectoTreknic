/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package acciones;

import bbdd.Conexion;
import bbdd.ConsultasNotificaciones;
import bbdd.ConsultasSoporte;
import java.util.Date;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import modelo.Sugerencia;
import modelo.Usuario;

/**
 *
 * @author k0343
 */
/**
 * Celda personalizada para mostrar un botón de eliminación de sugerencias
 * dentro de una tabla. Permite eliminar una sugerencia y registrar el
 * movimiento.
 */
public class CeldaAccionesSugerencia extends TableCell<Sugerencia, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonEliminar = new Button("Eliminar");

    /**
     * Constructor que configura el botón de eliminar y su evento.
     */
    public CeldaAccionesSugerencia() {
        botonEliminar.getStyleClass().add("boton-eliminar");
        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().add(botonEliminar);

        botonEliminar.setOnAction(e -> {
            Sugerencia sug = getTableRow().getItem();
            if (sug != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Eliminar Sugerencia");
                confirm.setHeaderText(null);
                confirm.setContentText("¿Deseas eliminar la sugerencia:\n" + sug.getTitulo() + "?");

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean ok = ConsultasSoporte.eliminarSugerencia(sug.getId());
                        if (ok) {
                            Conexion.conectar();
                            String mensaje = "Ha eliminado la sugerencia: " + sug.getTitulo();
                            int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

                            ConsultasNotificaciones.registrarMovimiento(
                                    mensaje,
                                    new Date(),
                                    idUsuario
                            );
                            Conexion.cerrarConexion();

                            getTableView().getItems().remove(sug);
                            Alert exito = new Alert(Alert.AlertType.INFORMATION);
                            exito.setContentText("Sugerencia eliminada correctamente.");
                            exito.showAndWait();
                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error");
                            error.setContentText("No se pudo eliminar la sugerencia.");
                            error.showAndWait();
                        }
                    }
                });
            }
        });
    }

    /**
     * Método que actualiza la celda para mostrar el botón si hay una sugerencia
     * en la fila.
     *
     * @param item Elemento de tipo Void (no usado).
     * @param empty true si la celda está vacía, false si contiene un valor.
     */
    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty || getTableRow().getItem() == null ? null : contenedor);
    }
}
