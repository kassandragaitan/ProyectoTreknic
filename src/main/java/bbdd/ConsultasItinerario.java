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
        conectar();
        try {
            String consulta = "INSERT INTO itinerario (nombre, descripcion, fecha_creacion, duracion, id_usuario) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, itinerario.getNombre());
            pst.setString(2, itinerario.getDescripcion());
            pst.setTimestamp(3, new java.sql.Timestamp(itinerario.getFechaCreacion().getTime()));
            pst.setString(4, String.valueOf(itinerario.getDuracion()));
            pst.setInt(5, itinerario.getIdUsuario());

            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static boolean actualizarItinerario(Itinerario itinerario) {
        try {
            String sql = "UPDATE itinerario SET nombre = ?, descripcion = ?, duracion = ? WHERE id_itinerario = ?";
            PreparedStatement stmt = Conexion.conn.prepareStatement(sql);
            stmt.setString(1, itinerario.getNombre());
            stmt.setString(2, itinerario.getDescripcion());
            stmt.setInt(3, itinerario.getDuracion());
            stmt.setInt(4, itinerario.getIdItinerario());

            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<String> cargarUsuariosDeItinerarios() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT u.nombre FROM itinerario i JOIN usuarios u ON i.id_usuario = u.id_usuario";

        try {
            PreparedStatement ps = Conexion.conectar().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexion.cerrarConexion();
        }

        return lista;
    }

    public static void cargarItinerariosPorUsuario(ObservableList<Itinerario> lista, String nombreUsuario) {
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerario i "
                + "JOIN usuarios u ON i.id_usuario = u.id_usuario "
                + "WHERE u.nombre = ?";
        try {
            PreparedStatement ps = Conexion.conn.prepareStatement(consulta);
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Itinerario itinerario = new Itinerario(
                        rs.getInt("id_itinerario"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"),
                        rs.getString("descripcion"),
                        mapEnumDurations(rs.getString("duracion")), // o rs.getInt("duracion")
                        rs.getInt("id_usuario")
                );
                itinerario.setNombreUsuario(rs.getString("nombre_usuario"));

                lista.add(itinerario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> cargarDuracionesItinerarios() {
        ObservableList<String> duraciones = FXCollections.observableArrayList();
        duraciones.add("Todas las duraciones"); // Opción por defecto

        String consulta = "SELECT DISTINCT duracion FROM itinerario ORDER BY duracion";

        try {
            PreparedStatement st = conn.prepareStatement(consulta);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                duraciones.add(rs.getString("duracion"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return duraciones;
    }

    public static void cargarItinerariosPorDuracion(ObservableList<Itinerario> listaItinerarios, String duracionSeleccionada) {
        String consulta;

        if (duracionSeleccionada.equals("Todas las duraciones")) {
            consulta = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion FROM itinerario";
        } else {
            consulta = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion FROM itinerario WHERE duracion = ?";
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(consulta);

            if (!duracionSeleccionada.equals("Todas las duraciones")) {
                stmt.setString(1, duracionSeleccionada);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Itinerario itinerario = new Itinerario(
                        rs.getInt("id_itinerario"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"), // <-- ESTE ES Date
                        rs.getString("descripcion"),
                        rs.getInt("duracion")
                );

                listaItinerarios.add(itinerario);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarDatosItinerariosFiltrados(ObservableList<Itinerario> listaItinerarios, String busqueda) {
        Connection conn = Conexion.conn;

        String consulta = "SELECT i.id_itinerario, i.nombre, i.fecha_creacion, i.descripcion, i.duracion, "
                + "i.id_usuario, u.nombre AS nombre_usuario "
                + "FROM itinerario i "
                + "JOIN usuarios u ON i.id_usuario = u.id_usuario "
                + "WHERE i.nombre LIKE ? OR i.descripcion LIKE ? OR "
                + "CAST(i.duracion AS CHAR) LIKE ? OR u.nombre LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            ps.setString(3, wildcard);
            ps.setString(4, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Itinerario it = new Itinerario(
                            rs.getInt("id_itinerario"),
                            rs.getString("nombre"),
                            rs.getDate("fecha_creacion"),
                            rs.getString("descripcion"),
                            mapEnumDurations(rs.getString("duracion")),
                            rs.getInt("id_usuario")
                    );
                    it.setNombreUsuario(rs.getString("nombre_usuario"));
                    listaItinerarios.add(it);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar itinerarios filtrados:");
            e.printStackTrace();
        }
    }

    public static void cargarDatosItinerarios(ObservableList<Itinerario> listado) {
        conectar();
        try {
//            String consultaCarga = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion, id_usuario FROM itinerario";
            String consultaCarga
                    = "SELECT i.id_itinerario, i.nombre, i.fecha_creacion, i.descripcion, i.duracion, "
                    + "i.id_usuario, u.nombre AS nombre_usuario "
                    + "FROM itinerario i "
                    + "JOIN usuarios u ON i.id_usuario = u.id_usuario";

            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    Itinerario itinerario = new Itinerario(
                            rs.getInt("id_itinerario"),
                            rs.getString("nombre"),
                            rs.getDate("fecha_creacion"),
                            rs.getString("descripcion"),
                            mapEnumDurations(rs.getString("duracion")),
                            rs.getInt("id_usuario")
                    );
                    itinerario.setNombreUsuario(rs.getString("nombre_usuario"));
                    listado.add(itinerario);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    private static int mapEnumDurations(String duration) {
        return duration.equals("3") ? 3 : duration.equals("5") ? 5 : duration.equals("7") ? 7 : 0;
    }

    public static void cargarComboDuracionItinerario(ComboBox comboDuracion) {
        try {
            String consulta = "SHOW COLUMNS FROM itinerario WHERE Field = 'duracion'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if (rs.next()) {
                String duracionStr = rs.getString("Type");
                duracionStr = duracionStr.substring(5, duracionStr.length() - 1).replace("'", "");
                String[] valoresEnum = duracionStr.split(",");
                for (String valor : valoresEnum) {
                    comboDuracion.getItems().add(Integer.parseInt(valor.trim())); // Convierte el valor a Integer y lo añade
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
