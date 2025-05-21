/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import static bbdd.Conexion.cerrarConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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

    public static void filtrarNotificaciones(ObservableList<Notificacion> lista, String usuario, String fecha, String leido) {
        StringBuilder sql = new StringBuilder("SELECT n.id_notificacion, n.descripcion, n.fecha, u.nombre AS nombre_usuario, n.leido "
                + "FROM notificaciones n INNER JOIN usuarios u ON n.id_usuario = u.id_usuario WHERE 1=1");

        if (usuario != null && !usuario.isEmpty()) {
            sql.append(" AND u.nombre = ?");
        }
        if (fecha != null && !fecha.isEmpty()) {
            sql.append(" AND DATE(n.fecha) = ?");
        }
        if (leido != null) {
            sql.append(" AND n.leido = ?");
        }

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (usuario != null && !usuario.isEmpty()) {
                ps.setString(index++, usuario);
            }
            if (fecha != null && !fecha.isEmpty()) {
                ps.setString(index++, fecha);
            }
            if (leido != null) {
                ps.setBoolean(index++, leido.equals("Sí"));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Notificacion(
                        rs.getInt("id_notificacion"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha"),
                        rs.getString("nombre_usuario"),
                        rs.getBoolean("leido")
                ));
            }
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static ObservableList<String> obtenerUsuariosNotificaciones() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT u.nombre FROM notificaciones n INNER JOIN usuarios u ON n.id_usuario = u.id_usuario";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    public static ObservableList<String> obtenerFechasNotificaciones() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT DATE_FORMAT(fecha, '%Y-%m-%d') AS fecha FROM notificaciones ORDER BY fecha DESC";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("fecha"));
            }
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
        return lista;
    }

    public static void registrarNotificacion(String descripcion, int idUsuario) {
        String sql = "INSERT INTO notificaciones (descripcion, fecha, id_usuario, leido) VALUES (?, NOW(), ?, 0)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, descripcion);
            pst.setInt(2, idUsuario);
            pst.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
