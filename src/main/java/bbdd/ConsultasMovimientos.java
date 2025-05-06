/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author k0343
 */
public class ConsultasMovimientos {

    public static void registrarMovimiento(String descripcion,Date fecha, int idUsuario) {
        String sql = "INSERT INTO notificaciones (id_usuario, descripcion, fecha, tipo_notificacion, leido, prioridad) "
                + "VALUES (?, ?, NOW(), 'Sistema', 0, 'Normal')";

        try (PreparedStatement ps = Conexion.conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, descripcion);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
