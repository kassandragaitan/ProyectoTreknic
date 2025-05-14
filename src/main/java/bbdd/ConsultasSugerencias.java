/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
}
