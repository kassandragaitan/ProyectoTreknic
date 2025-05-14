/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author k0343
 */
public class ConsultasMovimientos {

     public static void registrarMovimiento(String descripcion, Date fecha, int idUsuario) {
        try {
            String sql = "INSERT INTO notificaciones (descripcion, fecha, id_usuario, tipo_notificacion, prioridad, leido, notificacion) "
                       + "VALUES (?, ?, ?, 'Sistema', 'Normal', 0, ?)";
            PreparedStatement ps = Conexion.conectar().prepareStatement(sql);
            ps.setString(1, descripcion);
            ps.setDate(2, new java.sql.Date(fecha.getTime()));
            ps.setInt(3, idUsuario);
            ps.setString(4, descripcion); 
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
  


}
