/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

/**
 *
 * @author k0343
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.PreguntaFrecuente;
import modelo.Sugerencia;
import java.sql.*;

/**
 * Clase encargada de realizar operaciones de base de datos relacionadas con el
 * módulo de soporte, incluyendo preguntas frecuentes y sugerencias.
 *
 * Proporciona funcionalidades para filtrar, listar y eliminar registros en las
 * tablas correspondientes.
 *
 * Relacionada con las entidades {@link modelo.PreguntaFrecuente} y
 * {@link modelo.Sugerencia}. Utiliza la clase {@link Conexion} para la conexión
 * a la base de datos.
 *
 * @author k0343
 */
public class ConsultasSoporte {

    /**
     * Obtiene una lista filtrada de preguntas frecuentes que contienen el texto
     * dado en la pregunta o la respuesta.
     *
     * @param filtro Texto a buscar en los campos de pregunta o respuesta.
     * @return Lista observable de {@link PreguntaFrecuente} que coinciden con
     * el filtro.
     */
    public static ObservableList<PreguntaFrecuente> obtenerPreguntasFiltradas(String filtro) {
        ObservableList<PreguntaFrecuente> lista = FXCollections.observableArrayList();
        String sql = "SELECT pregunta, respuesta FROM preguntas_frecuentes WHERE pregunta LIKE ? OR respuesta LIKE ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String wildcard = "%" + filtro + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new PreguntaFrecuente(rs.getString("pregunta"), rs.getString("respuesta")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene una lista filtrada de sugerencias cuyo título o mensaje coinciden
     * con el filtro.
     *
     * @param filtro Texto de búsqueda para los campos de título o mensaje.
     * @return Lista observable de {@link Sugerencia} que coinciden con el
     * filtro.
     */
    public static ObservableList<Sugerencia> obtenerSugerenciasFiltradas(String filtro) {
        ObservableList<Sugerencia> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_sugerencia, titulo, mensaje, fecha_envio FROM sugerencias "
                + "WHERE titulo LIKE ? OR mensaje LIKE ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String wildcard = "%" + filtro + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Sugerencia(
                            rs.getInt("id_sugerencia"),
                            rs.getString("titulo"),
                            rs.getString("mensaje"),
                            rs.getString("fecha_envio").substring(0, 10)
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Elimina una pregunta frecuente según su texto exacto.
     *
     * @param pregunta Texto de la pregunta a eliminar.
     * @return {@code true} si se eliminó correctamente, {@code false} si
     * ocurrió un error.
     */
    public static boolean eliminarPregunta(String pregunta) {
        String sql = "DELETE FROM preguntas_frecuentes WHERE pregunta = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pregunta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una sugerencia por su identificador único.
     *
     * @param idSugerencia ID de la sugerencia a eliminar.
     * @return {@code true} si la eliminación fue exitosa, {@code false} en caso
     * de fallo.
     */
    public static boolean eliminarSugerencia(int idSugerencia) {
        String sql = "DELETE FROM sugerencias WHERE id_sugerencia = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSugerencia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
