package acciones;

import Utilidades.Alertas;
import bbdd.ConsultasAlojamientos;
import bbdd.ConsultasNotificaciones;
import controladores.AgregarAlojamientoController;
import controladores.GestionAlojamientoController;
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
import modelo.Alojamiento;
import modelo.ConexionFtp;
import modelo.Usuario;

/**
 * Clase que representa una celda personalizada con botones de acción para cada
 * fila de la tabla de alojamientos. Permite ver, editar, eliminar y marcar como
 * favorito.
 */
public class CeldaAccionesAlojamiento extends TableCell<Alojamiento, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");
    private final Button botonFavorito = new Button("❤");

    private final GestionAlojamientoController controlador;

    /**
     * Constructor que inicializa los botones y define su comportamiento.
     *
     * @param controlador Controlador principal de la vista de gestión de
     * alojamientos.
     */
    public CeldaAccionesAlojamiento(GestionAlojamientoController controlador) {
        this.controlador = controlador;

        botonVer.getStyleClass().add("table-button");
        botonEditar.getStyleClass().add("table-button");
        botonEliminar.getStyleClass().addAll("table-button", "red");
        botonFavorito.getStyleClass().addAll("table-button", "favorito");
        botonFavorito.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");

        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().addAll(botonVer, botonEditar, botonEliminar, botonFavorito);

        botonVer.setOnAction(e -> {
            Alojamiento alojamiento = getTableRow().getItem();
            if (alojamiento != null) {
                mostrarVentanaAlojamiento(alojamiento, false);
            }
        });

        botonEditar.setOnAction(e -> {
            Alojamiento alojamiento = getTableRow().getItem();
            if (alojamiento != null) {
                mostrarVentanaAlojamiento(alojamiento, true);

                controlador.cargarAlojamientos();
                controlador.cargarAlojamientosFavoritos();
            }
        });

        botonEliminar.setOnAction(e -> {
            Alojamiento alojamiento = getTableRow().getItem();
            if (alojamiento == null) {
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setHeaderText("¿Deseas eliminar este alojamiento?");
            confirm.setContentText(alojamiento.getNombre());

            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.OK) {
                    ConsultasAlojamientos.eliminarAlojamientoDeFavoritos(alojamiento.getIdAlojamiento());
                    boolean eliminado = ConsultasAlojamientos.eliminarAlojamiento(alojamiento.getIdAlojamiento());

                    if (eliminado) {
                        String img = alojamiento.getImagen();
                        if (img != null && !img.isBlank()) {
                            ConexionFtp.eliminarArchivo(img);
                        }

                        bbdd.Conexion.conectar();
                        String mensaje = "Ha eliminado el alojamiento: " + alojamiento.getNombre();
                        int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

                        bbdd.ConsultasNotificaciones.registrarMovimiento(mensaje, new java.util.Date(), idUsuario);
                        bbdd.Conexion.cerrarConexion();

                        Alertas.informacion("Alojamiento eliminado correctamente.");
                        controlador.cargarAlojamientos();
                        controlador.cargarAlojamientosFavoritos();
                    } else {
                        mostrarError("Error al eliminar alojamiento.");
                    }
                }
            });
        });

        botonFavorito.setOnAction(e -> {
            Alojamiento alojamiento = getTableRow().getItem();
            Usuario usuario = Usuario.getUsuarioActual();
            if (alojamiento == null || usuario == null) {
                return;
            }
            int idAloj = alojamiento.getIdAlojamiento();
            int idUsr = usuario.getIdUsuario();
            boolean yaEsFavorito = alojamiento.isFavorito();
            boolean exito;
            if (yaEsFavorito) {
                exito = ConsultasAlojamientos.eliminarDeFavoritos(idAloj, idUsr);
                if (exito) {
                    alojamiento.setFavorito(false);
                    botonFavorito.setText("♡");
                    botonFavorito.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
                    Alertas.informacion("Alojamiento eliminado de favoritos.");

                    bbdd.Conexion.conectar();
                    String mensaje = "Ha eliminado de favoritos el alojamiento: " + alojamiento.getNombre();
                    bbdd.ConsultasNotificaciones.registrarMovimiento(mensaje, new java.util.Date(), idUsr);

                    bbdd.Conexion.cerrarConexion();
                }
            } else {
                exito = ConsultasAlojamientos.agregarAFavoritos(idAloj, idUsr);
                if (exito) {
                    alojamiento.setFavorito(true);
                    botonFavorito.setText("❤");
                    botonFavorito.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
                    Alertas.informacion("Alojamiento añadido a favoritos.");

                    bbdd.Conexion.conectar();
                    String mensaje = "Ha añadido a favoritos el alojamiento: " + alojamiento.getNombre();
                    bbdd.ConsultasNotificaciones.registrarMovimiento(mensaje, new java.util.Date(), idUsr);
                    bbdd.Conexion.cerrarConexion();
                }
            }

            if (exito) {
                controlador.cargarAlojamientosFavoritos();
            } else {
                mostrarError("No se pudo actualizar favoritos.");
            }
        });
    }

    /**
     * Actualiza el contenido gráfico de la celda según el estado del
     * alojamiento.
     *
     * @param item Elemento vacío (no se usa en esta implementación).
     * @param empty true si la celda está vacía, false en caso contrario.
     */
    @Override
    protected void updateItem(Void item, boolean empty
    ) {
        super.updateItem(item, empty);

        if (empty || getTableRow().getItem() == null) {
            setGraphic(null);
        } else {
            Alojamiento alojamiento = getTableRow().getItem();
            if (alojamiento != null) {
                botonFavorito.setText(alojamiento.isFavorito() ? "❤" : "♡");
                botonFavorito.setStyle(
                        alojamiento.isFavorito()
                        ? "-fx-font-size: 16px; -fx-text-fill: red;"
                        : "-fx-font-size: 16px; -fx-text-fill: gray;"
                );
            }

            setGraphic(contenedor);
        }
    }

    /**
     * Abre la ventana de visualización o edición del alojamiento.
     *
     * @param alojamiento Objeto alojamiento a mostrar o editar.
     * @param editable True si la ventana debe ser editable, false si solo
     * visualización.
     */
    private void mostrarVentanaAlojamiento(Alojamiento alojamiento, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/vistas/AgregarAlojamiento.fxml"));
            Parent root = loader.load();

            AgregarAlojamientoController ctrl = loader.getController();
            ctrl.setTitulo(editable ? "Editar Alojamiento" : "Ver Alojamiento");
            ctrl.verAlojamiento(alojamiento);
            ctrl.setEdicionActiva(editable);

            Stage stage = new Stage();
            stage.setTitle(editable ? "Editar Alojamiento" : "Ver Alojamiento");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception ex) {
            mostrarError("Error al abrir la ventana: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Muestra un cuadro de error con el mensaje proporcionado.
     *
     * @param msg Mensaje de error a mostrar.
     */
    private void mostrarError(String msg) {
        Alert error = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        error.setHeaderText(null);
        error.showAndWait();
    }
}
