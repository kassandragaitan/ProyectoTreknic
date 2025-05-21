/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase encargada de manejar las operaciones relacionadas con la tabla
 * {@code sugerencias} en la base de datos.
 *
 * Incluye métodos para insertar nuevas sugerencias y verificar si ya existe una
 * sugerencia con el mismo título enviada por un usuario determinado.
 *
 * Utiliza {@link Conexion} para gestionar la conexión a la base de datos.
 *
 * @author k0343
 */
public class ConsultasSugerencias {

    /**
     * Inserta una nueva sugerencia en la base de datos con la fecha de envío
     * actual.
     *
     * @param idUsuario ID del usuario que envía la sugerencia.
     * @param titulo Título de la sugerencia (puede ser {@code null} si está
     * vacío).
     * @param mensaje Contenido del mensaje de la sugerencia.
     * @return {@code true} si la inserción fue exitosa, {@code false} en caso
     * de error.
     */
    public static boolean insertarSugerencia(int idUsuario, String titulo, String mensaje) {
        String sql = "INSERT INTO sugerencias (id_usuario, titulo, mensaje, fecha_envio) VALUES (?, ?, ?, NOW())";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setString(2, titulo.isEmpty() ? null : titulo);
            stmt.setString(3, mensaje);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si ya existe una sugerencia con el mismo título enviada por el
     * mismo usuario.
     *
     * @param idUsuario ID del usuario.
     * @param titulo Título de la sugerencia a verificar.
     * @return {@code true} si ya existe una sugerencia con ese título,
     * {@code false} si no.
     */
    public static boolean existeTitulo(int idUsuario, String titulo) {
        String sql = "SELECT COUNT(*) FROM sugerencias WHERE id_usuario = ? AND LOWER(titulo) = LOWER(?)";
        try (
                Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, titulo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
