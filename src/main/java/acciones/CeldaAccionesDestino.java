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
import bbdd.Conexion;
import bbdd.ConsultasNotificaciones;
import java.util.Date;
import javafx.scene.control.ButtonType;
import modelo.Usuario;

/**
 *
 * @author k0343
 */
/**
 * Clase encargada de generar los botones de acción (editar y eliminar) para
 * cada fila de la tabla de destinos en la vista de administración.
 */
public class CeldaAccionesDestino {

    private final GestionDestinosController controlador;

    /**
     * Constructor que recibe el controlador de la vista principal.
     *
     * @param controlador Controlador de la vista de gestión de destinos.
     */
    public CeldaAccionesDestino(GestionDestinosController controlador) {
        this.controlador = controlador;
    }

    /**
     * Crea y devuelve una fila de botones (editar y eliminar) para una fila de
     * la tabla de destinos.
     *
     * @param destino Objeto destino asociado a la fila actual.
     * @return HBox que contiene los botones configurados.
     */
    public HBox crearBotones(Destino destino) {
        Button botonEditar = new Button("Editar");
        Button botonEliminar = new Button("Eliminar");

        botonEditar.getStyleClass().add("table-button");
        botonEliminar.getStyleClass().addAll("table-button", "red");

        botonEditar.setOnAction(e -> abrirVentanaDestino(destino, true));

        botonEliminar.setOnAction(e -> {
            boolean tieneElementosAsociados = ConsultasDestinos.tieneElementosAsociados(destino.getId_destino());

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setHeaderText("¿Deseas eliminar este destino?");
            confirm.setContentText(
                    tieneElementosAsociados
                            ? "Este destino tiene elementos asociados (actividades, reseñas, alojamientos).\n¿Estás seguro de que deseas eliminarlo?"
                            : destino.getNombre()
            );
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean eliminado = tieneElementosAsociados
                            ? ConsultasDestinos.eliminarDestinoConAsociados(destino.getId_destino())
                            : ConsultasDestinos.eliminarDestino(destino.getId_destino());
                    if (eliminado) {
                        Conexion.conectar();
                        String mensaje = "Ha eliminado el destino: " + destino.getNombre();
                        int idUsuario = Usuario.getUsuarioActual().getIdUsuario();
                        Conexion.conectar();
                        ConsultasNotificaciones.registrarMovimiento(
                                mensaje,
                                new Date(),
                                idUsuario
                        );

                        ConsultasNotificaciones.registrarNotificacion(
                                mensaje,
                                idUsuario
                        );
                        Conexion.cerrarConexion();
                        Alertas.informacion("Destino eliminado correctamente.");
                        controlador.recargarTabla();
                    } else {
                        Alertas.error("Error", "No se pudo eliminar el destino.");
                    }
                }
            });
        });

        return new HBox(10, botonEditar, botonEliminar);
    }

    /**
     * Abre una ventana modal para editar los datos del destino seleccionado.
     *
     * @param destino Objeto destino a editar.
     * @param editable True si la ventana debe permitir edición, false si solo
     * es para visualizar.
     */
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
