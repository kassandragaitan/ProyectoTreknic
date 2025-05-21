package bbdd;

import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.Notificacion;

/**
 * Clase encargada de gestionar las operaciones relacionadas con la tabla
 * {@code notificaciones} del sistema. Permite registrar, filtrar, marcar,
 * eliminar y listar notificaciones para los usuarios.
 *
 * Se vincula con la entidad {@link modelo.Notificacion} y {@code usuarios}.
 *
 * @author k0343
 */
public class ConsultasNotificaciones {

    /**
     * Registra una notificación automática con fecha actual y estado no leído.
     *
     * @param descripcion Mensaje o contenido de la notificación.
     * @param idUsuario ID del usuario destinatario de la notificación.
     */
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

    /**
     * Registra manualmente una notificación con una fecha específica.
     *
     * @param descripcion Texto de la notificación.
     * @param fecha Fecha en que se generó.
     * @param idUsuario ID del usuario relacionado.
     */
    public static void registrarMovimiento(String descripcion, Date fecha, int idUsuario) {
        String sql = "INSERT INTO notificaciones (descripcion, fecha, id_usuario, leido) VALUES (?, ?, ?, 0)";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descripcion);
            ps.setDate(2, new java.sql.Date(fecha.getTime()));
            ps.setInt(3, idUsuario);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga todas las notificaciones desde la base de datos.
     *
     * @param listaNotificaciones Lista observable donde se agregan las
     * notificaciones.
     */
    public static void cargarDatosNotificaciones(ObservableList<Notificacion> listaNotificaciones) {
        String consulta = "SELECT n.id_notificacion, n.descripcion, n.fecha, u.nombre AS nombre_usuario, n.leido "
                + "FROM notificaciones n INNER JOIN usuarios u ON n.id_usuario = u.id_usuario";

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

    /**
     * Marca una notificación como leída en la base de datos.
     *
     * @param idNotificacion ID de la notificación a marcar.
     */
    public static void marcarComoLeido(int idNotificacion) {
        String consulta = "UPDATE notificaciones SET leido = 1 WHERE id_notificacion = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setInt(1, idNotificacion);
            pst.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Elimina una notificación específica por su ID.
     *
     * @param idNotificacion ID de la notificación a eliminar.
     * @return true si se eliminó correctamente, false si falló.
     */
    public static boolean eliminarNotificacion(int idNotificacion) {
        boolean eliminado = false;
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement("DELETE FROM notificaciones WHERE id_notificacion = ?")) {
            pst.setInt(1, idNotificacion);
            eliminado = pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.cerrarConexion();
        }
        return eliminado;
    }

    /**
     * Aplica filtros a las notificaciones por usuario, fecha y estado de
     * lectura.
     *
     * @param lista Lista donde se almacenarán los resultados.
     * @param usuario Nombre del usuario (puede ser null).
     * @param fecha Fecha en formato yyyy-MM-dd (puede ser null).
     * @param leido Estado de lectura ("Sí", "No" o null).
     */
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

    /**
     * Obtiene los nombres únicos de los usuarios que han recibido
     * notificaciones.
     *
     * @return Lista observable con nombres de usuarios.
     */
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

    /**
     * Obtiene todas las fechas distintas de envío de notificaciones, ordenadas
     * descendente.
     *
     * @return Lista observable con fechas en formato yyyy-MM-dd.
     */
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
}
