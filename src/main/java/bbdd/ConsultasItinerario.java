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
        conectar();
        try {
            String consulta = "INSERT INTO itinerarios (nombre, descripcion, fecha_creacion, duracion, id_usuario) VALUES (?, ?, ?, ?, ?)";
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
            String sql = "UPDATE itinerarios SET nombre = ?, descripcion = ?, duracion = ? WHERE id_itinerario = ?";
            PreparedStatement stmt = Conexion.conn.prepareStatement(sql);
            stmt.setString(1, itinerario.getNombre());
            stmt.setString(2, itinerario.getDescripcion());
            stmt.setString(3, itinerario.getDuracion());
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
        String consulta = "SELECT DISTINCT u.nombre FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario";

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
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerarios i "
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
                        rs.getString("duracion"),
                        rs.getInt("id_usuario")
                );
                itinerario.setNombreUsuario(rs.getString("nombre_usuario"));

                lista.add(itinerario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> obtenerDuraciones() {
        List<Integer> listaDuraciones = new ArrayList<>();

        try {
            String sql = "SELECT DISTINCT duracion FROM itinerarios ORDER BY duracion";
            PreparedStatement ps = Conexion.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String valor = rs.getString("duracion");
                try {
                    listaDuraciones.add(Integer.parseInt(valor));
                } catch (NumberFormatException e) {
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.err.println("Error al cargar duraciones: " + e.getMessage());
        }

        return listaDuraciones;
    }

    public static ObservableList<String> cargarDuracionesItinerarios() {
        ObservableList<String> duraciones = FXCollections.observableArrayList();

        String consulta = "SELECT DISTINCT duracion FROM itinerarios ORDER BY duracion";

        try {
            PreparedStatement st = Conexion.conn.prepareStatement(consulta);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                duraciones.add(rs.getString("duracion"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return duraciones;
    }

    public static void cargarComboDuracionItinerario(ComboBox<String> comboDuracion) {
        comboDuracion.getItems().clear();
        comboDuracion.getItems().add("Seleccione");

        try {
            String consulta = "SHOW COLUMNS FROM itinerarios WHERE Field = 'duracion'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if (rs.next()) {
                String duracionStr = rs.getString("Type");
                duracionStr = duracionStr.substring(5, duracionStr.length() - 1).replace("'", "");
                String[] valoresEnum = duracionStr.split(",");
                for (String valor : valoresEnum) {
                    comboDuracion.getItems().add(valor.trim());
                }
            }
            comboDuracion.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarItinerariosPorDuracion(ObservableList<Itinerario> listaItinerarios, String duracionSeleccionada) {
        String consulta = "SELECT i.id_itinerario, i.nombre, i.fecha_creacion, i.descripcion, i.duracion, "
                + "i.id_usuario, u.nombre AS nombre_usuario "
                + "FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario "
                + "WHERE i.duracion = ?";

        try {
            PreparedStatement stmt = Conexion.conn.prepareStatement(consulta);
            stmt.setString(1, duracionSeleccionada);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Itinerario itinerario = new Itinerario(
                        rs.getInt("id_itinerario"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"),
                        rs.getString("descripcion"),
                        rs.getString("duracion"),
                        rs.getInt("id_usuario")
                );
                itinerario.setNombreUsuario(rs.getString("nombre_usuario"));
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
                + "FROM itinerarios i "
                + "JOIN usuarios u ON i.id_usuario = u.id_usuario "
                + "WHERE i.nombre LIKE ? OR i.descripcion LIKE ? OR "
                + "CAST(i.duracion AS CHAR) LIKE ? OR u.nombre LIKE ? OR "
                + "DATE_FORMAT(i.fecha_creacion, '%Y-%m-%d') LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            ps.setString(3, wildcard);
            ps.setString(4, wildcard);
            ps.setString(5, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
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
            String consultaCarga
                    = "SELECT i.id_itinerario, i.nombre, i.fecha_creacion, i.descripcion, i.duracion, "
                    + "i.id_usuario, u.nombre AS nombre_usuario "
                    + "FROM itinerarios i "
                    + "JOIN usuarios u ON i.id_usuario = u.id_usuario";

            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    Itinerario itinerario = new Itinerario(
                            rs.getInt("id_itinerario"),
                            rs.getString("nombre"),
                            rs.getDate("fecha_creacion"),
                            rs.getString("descripcion"),
                            rs.getString("duracion"),
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

    public static boolean existeItinerarioConNombre(String nombre) {
        boolean existe = false;
        try {
            String sql = "SELECT COUNT(*) FROM itinerarios WHERE nombre = ?";
            PreparedStatement stmt = Conexion.conectar().prepareStatement(sql);
            stmt.setString(1, nombre.trim());

            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                existe = true;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

}
