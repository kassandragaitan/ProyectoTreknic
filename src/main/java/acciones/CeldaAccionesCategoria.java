package acciones;

import bbdd.ConsultasCategoria;
import controladores.AgregarCategoriaController;
import controladores.GestionCategoriaController;
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
import modelo.Categoria;

/**
 * Celda personalizada para mostrar botones de acciones (ver, editar, eliminar)
 * en cada fila de la tabla de categorías.
 */
public class CeldaAccionesCategoria extends TableCell<Categoria, Void> {

    private final HBox contenedor = new HBox(10);
    private final Button botonVer = new Button("Ver");
    private final Button botonEditar = new Button("Editar");
    private final Button botonEliminar = new Button("Eliminar");
    private final GestionCategoriaController controller;

    /**
     * Constructor que configura los botones de acción para la categoría.
     *
     * @param controller Controlador de la vista de gestión de categorías.
     */
    public CeldaAccionesCategoria(GestionCategoriaController controller) {
        this.controller = controller;

        botonVer.getStyleClass().add("table-button");
        botonEditar.getStyleClass().add("table-button");
        botonEliminar.getStyleClass().addAll("table-button", "red");

        contenedor.setAlignment(Pos.CENTER);
        contenedor.getChildren().addAll(botonVer, botonEditar, botonEliminar);

        botonVer.setOnAction(e -> abrirDialog(getTableRow().getItem(), false));

        botonEditar.setOnAction(e -> {
            if (abrirDialog(getTableRow().getItem(), true)) {
                controller.recargarTabla();
            }
        });

        botonEliminar.setOnAction(e -> {
            Categoria cat = getTableRow().getItem();
            if (cat == null) {
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar categoría \"" + cat.getNombre() + "\"?",
                    ButtonType.OK, ButtonType.CANCEL);
            confirm.setHeaderText("¿Estás seguro que quieres eliminar esta categoría?");
            confirm.initOwner(botonEliminar.getScene().getWindow());

            confirm.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    boolean eliminado = ConsultasCategoria.eliminarPorId(cat.getIdCategoria());
                    if (eliminado) {
                        bbdd.Conexion.conectar();
                        String mensaje = "Ha eliminado la categoría: " + cat.getNombre();
                        int idUsuario = modelo.Usuario.getUsuarioActual().getIdUsuario();

                        bbdd.ConsultasNotificaciones.registrarMovimiento(mensaje, new java.util.Date(), idUsuario);
                        bbdd.Conexion.cerrarConexion();

                        controller.recargarTabla();
                        new Alert(Alert.AlertType.INFORMATION,
                                "Categoría eliminada.", ButtonType.OK).showAndWait();
                    } else {
                        new Alert(Alert.AlertType.ERROR,
                                "No se pudo eliminar la categoría.", ButtonType.OK).showAndWait();
                    }
                }
            });
        });
    }

    /**
     * Actualiza el contenido visual de la celda dependiendo si contiene datos o
     * no.
     *
     * @param item Elemento nulo (no se usa).
     * @param empty true si la celda está vacía, false si tiene contenido.
     */
    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty || getTableRow().getItem() == null ? null : contenedor);
    }

    /**
     * Abre una ventana modal para ver o editar la categoría especificada.
     *
     * @param cat Categoría a visualizar o editar.
     * @param editable true si se debe permitir la edición, false para solo
     * vista.
     * @return true si la categoría fue modificada, false en caso contrario.
     */
    private boolean abrirDialog(Categoria cat, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/vistas/AgregarCategoria.fxml"));
            Parent root = loader.load();

            AgregarCategoriaController c = loader.getController();
            c.setTitulo(editable ? "Editar Categoría" : "Ver Categoría");
            c.setGestionCategoriaController(controller);
            c.verCategoria(cat);
            c.setEdicionActiva(editable);

            Stage st = new Stage();
            st.setTitle(editable ? "Editar Categoría" : "Ver Categoría");
            st.setScene(new Scene(root));
            st.initModality(Modality.APPLICATION_MODAL);
            st.setMaximized(false);
            st.setResizable(false);
            st.showAndWait();

            return editable && c.getModificado();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
