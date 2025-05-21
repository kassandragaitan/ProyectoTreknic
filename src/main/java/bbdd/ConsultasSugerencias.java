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
 *
 * @author k0343
 */
public class ConsultasSugerencias {

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
     * Devuelve true si el usuario ya envió antes una sugerencia con ese título.
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
