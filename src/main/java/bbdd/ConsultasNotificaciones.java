/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import static bbdd.Conexion.cerrarConexion;
import static bbdd.Conexion.conectar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import modelo.Notificacion;

/**
 *
 * @author k0343
 */
public class ConsultasNotificaciones {

    public static void cargarDatosNotificaciones(ObservableList<Notificacion> listaNotificaciones) {
        String consulta = "SELECT n.id_notificacion, n.descripcion, n.fecha, u.nombre AS nombre_usuario, n.leido "
                + "FROM notificaciones n "
                + "INNER JOIN usuarios u ON n.id_usuario = u.id_usuario";

        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {

            while (rs.next()) {
                Notificacion notificacion = new Notificacion(
                        rs.getInt("id_notificacion"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha"),
                        rs.getString("nombre_usuario"),
                        rs.getBoolean("leido")
                );
                listaNotificaciones.add(notificacion);
            }

        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void marcarComoLeido(int idNotificacion) {
        String consulta = "UPDATE notificaciones SET leido = 1 WHERE id_notificacion = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setInt(1, idNotificacion);
            pst.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static boolean eliminarNotificacion(int idNotificacion) {
        boolean eliminado = false;
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement("DELETE FROM notificaciones WHERE id_notificacion = ?")) {
            pst.setInt(1, idNotificacion);
            eliminado = pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
        return eliminado;
    }

}
