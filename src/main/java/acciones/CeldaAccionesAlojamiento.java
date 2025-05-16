package acciones;

import Utilidades.Alertas;
import bbdd.Conexion;
import bbdd.ConsultasAlojamientos;
import controladores.AgregarAlojamientoController;
import controladores.GestionAlojamientoController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

public class CeldaAccionesAlojamiento extends TableCell<Alojamiento, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");
    private final Button botonFavorito = new Button("❤");

    private final GestionAlojamientoController controlador;

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

            boolean yaEsFavorito = ConsultasAlojamientos.existeFavorito(idUsr, idAloj);
            boolean exito;
            if (yaEsFavorito) {
                exito = ConsultasAlojamientos.eliminarDeFavoritos(idAloj, idUsr);
                if (exito) {
                    botonFavorito.setText("♡");
                    Alertas.informacion("Alojamiento eliminado de favoritos.");
                }
            } else {
                exito = ConsultasAlojamientos.agregarAFavoritos(idAloj, idUsr);
                if (exito) {
                    botonFavorito.setText("❤");
                    Alertas.informacion("Alojamiento añadido a favoritos.");
                }
            }

            if (exito) {
                controlador.cargarAlojamientosFavoritos();
            } else {
                mostrarError("No se pudo actualizar favoritos.");
            }
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || getTableRow().getItem() == null) {
            setGraphic(null);
        } else {
            Alojamiento alojamiento = getTableRow().getItem();
            Usuario usuario = Usuario.getUsuarioActual();

            if (alojamiento != null && usuario != null) {
                boolean esFavorito = ConsultasAlojamientos.existeFavorito(
                        usuario.getIdUsuario(),
                        alojamiento.getIdAlojamiento()
                );

                botonFavorito.setText(esFavorito ? "❤" : "♡");
                botonFavorito.setStyle(
                        esFavorito
                                ? "-fx-font-size: 16px; -fx-text-fill: red;"
                                : "-fx-font-size: 16px; -fx-text-fill: gray;"
                );
            }

            setGraphic(contenedor);
        }
    }

    private void mostrarVentanaAlojamiento(Alojamiento alojamiento, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/vistas/AgregarAlojamiento.fxml"));
            Parent root = loader.load();

            AgregarAlojamientoController ctrl = loader.getController();
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

    private void mostrarError(String msg) {
        Alert error = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        error.setHeaderText(null);
        error.showAndWait();
    }
}
