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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import modelo.Itinerario;

/**
 *
 * @author k0343
 */
public class ConsultasItinerario {

    public static boolean registrarItinerario(Itinerario itinerario) {
        String consulta = "INSERT INTO itinerarios (nombre, descripcion, fecha_creacion, duracion, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setString(1, itinerario.getNombre());
            pst.setString(2, itinerario.getDescripcion());
            pst.setTimestamp(3, new Timestamp(itinerario.getFechaCreacion().getTime()));
            pst.setString(4, itinerario.getDuracion());
            pst.setInt(5, itinerario.getIdUsuario());
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean actualizarItinerario(Itinerario itinerario) {
        String sql = "UPDATE itinerarios SET nombre = ?, descripcion = ?, duracion = ? WHERE id_itinerario = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itinerario.getNombre());
            stmt.setString(2, itinerario.getDescripcion());
            stmt.setString(3, itinerario.getDuracion());
            stmt.setInt(4, itinerario.getIdItinerario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<String> cargarUsuariosDeItinerarios() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT u.nombre FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void cargarItinerariosPorUsuario(ObservableList<Itinerario> lista, String nombreUsuario) {
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario WHERE u.nombre = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Itinerario it = new Itinerario(
                        rs.getInt("id_itinerario"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"),
                        rs.getString("descripcion"),
                        rs.getString("duracion"),
                        rs.getInt("id_usuario")
                );
                it.setNombreUsuario(rs.getString("nombre_usuario"));
                lista.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> obtenerDuraciones() {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT duracion FROM itinerarios ORDER BY duracion";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    lista.add(Integer.parseInt(rs.getString("duracion")));
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static ObservableList<String> cargarDuracionesItinerarios() {
        ObservableList<String> duraciones = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT duracion FROM itinerarios ORDER BY duracion";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                duraciones.add(rs.getString("duracion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return duraciones;
    }

    public static void cargarComboDuracionItinerario(ComboBox<String> comboDuracion) {
        comboDuracion.getItems().clear();
        comboDuracion.getItems().add("Seleccione");
        String consulta = "SHOW COLUMNS FROM itinerarios WHERE Field = 'duracion'";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {
            if (rs.next()) {
                String tipo = rs.getString("Type");
                tipo = tipo.substring(5, tipo.length() - 1).replace("'", "");
                for (String val : tipo.split(",")) {
                    comboDuracion.getItems().add(val.trim());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboDuracion.getSelectionModel().selectFirst();
    }

    public static void cargarItinerariosPorDuracion(ObservableList<Itinerario> lista, String duracion) {
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario WHERE i.duracion = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(consulta)) {
            stmt.setString(1, duracion);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Itinerario it = new Itinerario(
                        rs.getInt("id_itinerario"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"),
                        rs.getString("descripcion"),
                        rs.getString("duracion"),
                        rs.getInt("id_usuario")
                );
                it.setNombreUsuario(rs.getString("nombre_usuario"));
                lista.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cargarDatosItinerariosFiltrados(ObservableList<Itinerario> lista, String texto) {
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario WHERE i.nombre LIKE ? OR i.descripcion LIKE ? OR CAST(i.duracion AS CHAR) LIKE ? OR u.nombre LIKE ? OR DATE_FORMAT(i.fecha_creacion, '%Y-%m-%d') LIKE ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {
            String comodin = "%" + texto + "%";
            for (int i = 1; i <= 5; i++) {
                ps.setString(i, comodin);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Itinerario it = new Itinerario(
                        rs.getInt("id_itinerario"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"),
                        rs.getString("descripcion"),
                        rs.getString("duracion"),
                        rs.getInt("id_usuario")
                );
                it.setNombreUsuario(rs.getString("nombre_usuario"));
                lista.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cargarDatosItinerarios(ObservableList<Itinerario> lista) {
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {
            while (rs.next()) {
                Itinerario it = new Itinerario(
                        rs.getInt("id_itinerario"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"),
                        rs.getString("descripcion"),
                        rs.getString("duracion"),
                        rs.getInt("id_usuario")
                );
                it.setNombreUsuario(rs.getString("nombre_usuario"));
                lista.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean existeItinerarioConNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM itinerarios WHERE nombre = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre.trim());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
