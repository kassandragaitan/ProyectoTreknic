/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import static bbdd.Conexion.cerrarConexion;
import static bbdd.Conexion.conectar;
import static bbdd.Conexion.conn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import modelo.Notificacion;
import modelo.PreferenciasNotificacion;

/**
 *
 * @author k0343
 */
public class ConsultasNotificaciones {

    public static void cargarDatosNotificaciones(ObservableList<Notificacion> listaNotificaciones) {
        conectar();
        try {
            String consulta = "SELECT n.id_notificacion, n.descripcion, n.fecha, n.notificacion, u.nombre AS destinatario, n.tipo_notificacion, n.prioridad, n.leido "
                    + "FROM notificaciones n "
                    + "INNER JOIN usuarios u ON n.id_usuario = u.id_usuario";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                Notificacion notificacion = new Notificacion(
                        rs.getInt("id_notificacion"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha"),
                        rs.getString("notificacion"),
                        rs.getString("destinatario"),
                        rs.getString("tipo_notificacion"),
                        rs.getString("prioridad"),
                        rs.getBoolean("leido")
                );
                listaNotificaciones.add(notificacion);
            }
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            cerrarConexion();
        }
    }

    public static void marcarComoLeido(int idNotificacion) {
        conectar();
        try {
            String consulta = "UPDATE notificaciones SET leido = 1 WHERE id_notificacion = ?";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setInt(1, idNotificacion);
            pst.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            cerrarConexion();
        }
    }
// Cargar las preferencias de un usuario

    public static PreferenciasNotificacion cargarPreferencias(int idUsuario) {
        conectar();
        PreferenciasNotificacion preferencias = new PreferenciasNotificacion();
        try {
            String consulta = "SELECT notificaciones_correo, notificaciones_sistema, resumen_diario, hora_inicio, hora_fin, dias FROM configuracion_notificaciones WHERE id_usuario = ? LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(consulta);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                preferencias.setCorreo(rs.getBoolean("notificaciones_correo"));
                preferencias.setSistema(rs.getBoolean("notificaciones_sistema"));
                preferencias.setResumen(rs.getBoolean("resumen_diario"));
                preferencias.setHoraInicio(rs.getString("hora_inicio"));
                preferencias.setHoraFin(rs.getString("hora_fin"));
                preferencias.setDias(rs.getString("dias"));
            }
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            cerrarConexion();
        }
        return preferencias;
    }

// Guardar o actualizar preferencias de un usuario
    public static boolean guardarPreferencias(int idUsuario, PreferenciasNotificacion preferencias) {
        conectar();
        try {
            // Verificar si ya existe la configuración
            String consultaCheck = "SELECT COUNT(*) FROM configuracion_notificaciones WHERE id_usuario = ?";
            PreparedStatement psCheck = conn.prepareStatement(consultaCheck);
            psCheck.setInt(1, idUsuario);
            ResultSet rsCheck = psCheck.executeQuery();
            rsCheck.next();
            int existe = rsCheck.getInt(1);

            if (existe > 0) {
                // Actualizar
                String consultaUpdate = "UPDATE configuracion_notificaciones SET notificaciones_correo = ?, notificaciones_sistema = ?, resumen_diario = ?, hora_inicio = ?, hora_fin = ?, dias = ? WHERE id_usuario = ?";
                PreparedStatement pst = conn.prepareStatement(consultaUpdate);
                pst.setBoolean(1, preferencias.isCorreo());
                pst.setBoolean(2, preferencias.isSistema());
                pst.setBoolean(3, preferencias.isResumen());
                pst.setString(4, preferencias.getHoraInicio());
                pst.setString(5, preferencias.getHoraFin());
                pst.setString(6, preferencias.getDias());
                pst.setInt(7, idUsuario);
                pst.executeUpdate();
            } else {
                // Insertar nuevo
                String consultaInsert = "INSERT INTO configuracion_notificaciones (id_usuario, notificaciones_correo, notificaciones_sistema, resumen_diario, hora_inicio, hora_fin, dias) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(consultaInsert);
                pst.setInt(1, idUsuario);
                pst.setBoolean(2, preferencias.isCorreo());
                pst.setBoolean(3, preferencias.isSistema());
                pst.setBoolean(4, preferencias.isResumen());
                pst.setString(5, preferencias.getHoraInicio());
                pst.setString(6, preferencias.getHoraFin());
                pst.setString(7, preferencias.getDias());
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            cerrarConexion();
        }
    }

}
