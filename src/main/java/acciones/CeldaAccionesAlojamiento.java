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
            }
        });

        botonEliminar.setOnAction(e -> {
            Alojamiento alojamiento = getTableRow().getItem();
            if (alojamiento != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar eliminación");
                confirm.setHeaderText("¿Deseas eliminar este alojamiento?");
                confirm.setContentText(alojamiento.getNombre());

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean eliminadoDeFavoritos = ConsultasAlojamientos.eliminarAlojamientoDeFavoritos(alojamiento.getIdAlojamiento());

                        boolean eliminado = ConsultasAlojamientos.eliminarAlojamiento(alojamiento.getIdAlojamiento());

                        if (eliminado) {
                            String img = alojamiento.getImagen();
                            if (img != null && !img.isBlank()) {
                                ConexionFtp.eliminarArchivo(img);
                            }
                            controlador.recargarFavoritos();
                            getTableView().getItems().remove(alojamiento);
                            Alertas.informacion("Alojamiento eliminados correctamente.");
                        

                    } else {
                        mostrarError("Error al eliminar alojamiento.");
                    }
                }
            }
        );
    }

    });

    botonFavorito.setOnAction (e  
        -> {
    Alojamiento alojamiento = getTableRow().getItem();
        Usuario usuario = Usuario.getUsuarioActual();
        if (alojamiento != null && usuario != null) {
            int idAlojamiento = alojamiento.getIdAlojamiento();
            int idUsuario = usuario.getIdUsuario();

            boolean yaEsFavorito = ConsultasAlojamientos.existeFavorito(idUsuario, idAlojamiento);

            boolean exito;
            String mensaje;

            if (yaEsFavorito) {
                exito = ConsultasAlojamientos.eliminarDeFavoritos(idAlojamiento, idUsuario);
                mensaje = exito ? "Alojamiento eliminado de favoritos." : "No se pudo eliminar de favoritos.";
            } else {
                exito = ConsultasAlojamientos.agregarAFavoritos(idAlojamiento, idUsuario);
                mensaje = exito ? "Alojamiento añadido a favoritos." : "No se pudo añadir a favoritos.";
            }

            Alert alerta = new Alert(exito ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alerta.setTitle(yaEsFavorito ? "Favorito eliminado" : "Favorito añadido");
            alerta.setContentText(mensaje);
            alerta.showAndWait();

            if (controlador != null) {
                controlador.recargarFavoritos();
            }
        }
    }

);

    }

        @Override
protected void updateItem
        (Void item, boolean empty
        
            ) {
        super.updateItem(item, empty);
            setGraphic(empty || getTableRow().getItem() == null ? null : contenedor);
        }

    

    private void mostrarVentanaAlojamiento(Alojamiento alojamiento, boolean editable) {
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
        } catch (Exception e) {
            mostrarError("Error al abrir la ventana: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean eliminarAlojamientoPorId(int idAlojamiento) {
        String sql = "DELETE FROM alojamiento WHERE id_alojamiento = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlojamiento);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mostrarError(String mensaje) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText(null);
        error.setContentText(mensaje);
        error.showAndWait();
    }
}
