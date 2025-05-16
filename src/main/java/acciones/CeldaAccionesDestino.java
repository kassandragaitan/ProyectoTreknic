package acciones;

import controladores.AgregarDestinoController;
import controladores.GestionDestinosController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Destino;
import bbdd.ConsultasDestinos;
import Utilidades.Alertas;
import javafx.scene.control.ButtonType;

public class CeldaAccionesDestino {

    private final GestionDestinosController controlador;

    public CeldaAccionesDestino(GestionDestinosController controlador) {
        this.controlador = controlador;
    }

    public HBox crearBotones(Destino destino) {
        Button botonEditar = new Button("Editar");
        Button botonEliminar = new Button("Eliminar");

        botonEditar.getStyleClass().add("table-button");
        botonEliminar.getStyleClass().addAll("table-button", "red");

        botonEditar.setOnAction(e -> abrirVentanaDestino(destino, true));

        botonEliminar.setOnAction(e -> {
            boolean tieneElementosAsociados = ConsultasDestinos.tieneElementosAsociados(destino.getId_destino());

            if (tieneElementosAsociados) {
                String mensaje = "Este destino tiene elementos asociados (actividades, reseñas, alojamientos).\n¿Estás seguro de que deseas eliminarlo?";

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar eliminación");
                confirm.setHeaderText("¿Deseas eliminar este destino?");
                confirm.setContentText(mensaje);

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean eliminado = ConsultasDestinos.eliminarDestinoConAsociados(destino.getId_destino());
                        if (eliminado) {
                            Alertas.informacion("Destino eliminado correctamente.");
                            controlador.recargarTabla();
                        } else {
                            Alertas.error("Error", "No se pudo eliminar el destino.");
                        }
                    }
                });
            } else {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar eliminación");
                confirm.setHeaderText("¿Deseas eliminar este destino?");
                confirm.setContentText(destino.getNombre());

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean eliminado = ConsultasDestinos.eliminarDestino(destino.getId_destino());
                        if (eliminado) {
                            Alertas.informacion("Destino eliminado correctamente.");
                            controlador.recargarTabla();
                        } else {
                            Alertas.error("Error", "No se pudo eliminar el destino.");
                        }
                    }
                });
            }
        });
        return new HBox(10, botonEditar, botonEliminar);
    }

    private void abrirVentanaDestino(Destino destino, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarDestino.fxml"));
            Parent root = loader.load();

            AgregarDestinoController controller = loader.getController();
            controller.setGestionDestinosController(controlador);
            controller.setEdicionActiva(editable);
            controller.cargarDestino(destino);

            Stage stage = new Stage();
            stage.setTitle("Editar Destino");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alertas.error("Error", "No se pudo abrir la ventana: " + ex.getMessage());
        }
    }
}
