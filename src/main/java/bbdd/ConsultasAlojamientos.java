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
import modelo.Alojamiento;
import modelo.TipoAlojamiento;

/**
 *
 * @author k0343
 */
public class ConsultasAlojamientos {

    public static boolean registrarAlojamiento(Alojamiento alojamiento) {
        conectar();
        try {
            String consulta = "INSERT INTO alojamientos (nombre, id_tipo_fk, contacto, imagen, id_destino_fk) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, alojamiento.getNombre());
            pst.setInt(2, alojamiento.getIdTipo());
            pst.setString(3, alojamiento.getContacto());
            pst.setString(4, alojamiento.getImagen());
            pst.setInt(5, alojamiento.getIdDestino());
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static boolean actualizarAlojamiento(Alojamiento alojamiento) {
        String sql = "UPDATE alojamientos SET nombre = ?, id_tipo_fk = ?, contacto = ?, imagen = ?, id_destino_fk = ? WHERE id_alojamiento = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, alojamiento.getNombre());
            stmt.setInt(2, alojamiento.getIdTipo());
            stmt.setString(3, alojamiento.getContacto());
            stmt.setString(4, alojamiento.getImagen());
            stmt.setInt(5, alojamiento.getIdDestino());
            stmt.setInt(6, alojamiento.getIdAlojamiento());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<String> cargarTiposAlojamiento() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT tipo FROM tipoalojamiento";
        try (PreparedStatement ps = Conexion.conn.prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("tipo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static ObservableList<String> cargarDestinosAlojamiento() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT nombre FROM destinos";
        try (PreparedStatement ps = Conexion.conn.prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void cargarAlojamientosPorTipo(ObservableList<Alojamiento> lista, String tipo) {
        String sql = "SELECT a.*, t.tipo, d.nombre AS nombre_destino "
                + "FROM alojamientos a "
                + "JOIN tipoalojamiento t ON a.id_tipo_fk = t.id_tipo "
                + "JOIN destinos d ON a.id_destino_fk = d.id_destino "
                + "WHERE t.tipo = ?";
        try (PreparedStatement ps = Conexion.conn.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Alojamiento a = new Alojamiento(
                        rs.getInt("id_alojamiento"),
                        rs.getString("nombre"),
                        rs.getString("contacto"),
                        rs.getString("imagen"),
                        rs.getString("tipo"),
                        rs.getString("nombre_destino")
                );
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cargarAlojamientosPorDestino(ObservableList<Alojamiento> lista, String destino) {
        String sql = "SELECT a.*, t.tipo, d.nombre AS nombre_destino "
                + "FROM alojamientos a "
                + "JOIN tipoalojamiento t ON a.id_tipo_fk = t.id_tipo "
                + "JOIN destinos d ON a.id_destino_fk = d.id_destino "
                + "WHERE d.nombre = ?";
        try (PreparedStatement ps = Conexion.conn.prepareStatement(sql)) {
            ps.setString(1, destino);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Alojamiento a = new Alojamiento(
                        rs.getInt("id_alojamiento"),
                        rs.getString("nombre"),
                        rs.getString("contacto"),
                        rs.getString("imagen"),
                        rs.getString("tipo"),
                        rs.getString("nombre_destino")
                );
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cargarDatosAlojamientosFiltrados(ObservableList<Alojamiento> listaAlojamientos, String busqueda) {
        Connection conn = Conexion.conn;

        String consulta = "SELECT a.id_alojamiento, a.nombre, a.contacto, a.imagen, "
                + "a.id_destino_fk, d.nombre AS nombre_destino, a.id_tipo_fk, t.tipo AS nombre_tipo "
                + "FROM alojamientos a "
                + "JOIN destinos d ON a.id_destino_fk = d.id_destino "
                + "JOIN tipoalojamiento t ON a.id_tipo_fk = t.id_tipo "
                + "WHERE a.nombre LIKE ? OR a.contacto LIKE ? OR d.nombre LIKE ? OR t.tipo LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            ps.setString(3, wildcard);
            ps.setString(4, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Alojamiento al = new Alojamiento();
                    al.setIdAlojamiento(rs.getInt("id_alojamiento"));
                    al.setNombre(rs.getString("nombre"));
                    al.setContacto(rs.getString("contacto"));
                    al.setImagen(rs.getString("imagen"));
                    al.setIdDestino(rs.getInt("id_destino_fk"));
                    al.setNombreDestino(rs.getString("nombre_destino"));
                    al.setIdTipo(rs.getInt("id_tipo_fk"));
                    al.setNombreTipo(rs.getString("nombre_tipo"));

                    listaAlojamientos.add(al);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar alojamientos filtrados:");
            e.printStackTrace();
        }
    }

    public static void cargarComboTipoAlojamiento(ComboBox<TipoAlojamiento> comboTipo) {
        conectar();
        try {
            String consulta = "SELECT id_tipo, tipo FROM tipoalojamiento";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                TipoAlojamiento tipo = new TipoAlojamiento(rs.getInt("id_tipo"), rs.getString("tipo"));
                comboTipo.getItems().add(tipo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosAlojamientos(ObservableList<Alojamiento> listado) {
        conectar();
        try {
            String consultaCarga
                    = "SELECT a.id_alojamiento, a.nombre, a.contacto, a.imagen, "
                    + "a.id_destino_fk, d.nombre AS nombre_destino, a.id_tipo_fk, t.tipo AS nombre_tipo "
                    + "FROM alojamientos a "
                    + "JOIN destinos d ON a.id_destino_fk = d.id_destino "
                    + "JOIN tipoalojamiento t ON a.id_tipo_fk = t.id_tipo";

            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    Alojamiento al = new Alojamiento();
                    al.setIdAlojamiento(rs.getInt("id_alojamiento"));
                    al.setNombre(rs.getString("nombre"));
                    al.setContacto(rs.getString("contacto"));
                    al.setImagen(rs.getString("imagen"));
                    al.setIdDestino(rs.getInt("id_destino_fk"));
                    al.setNombreDestino(rs.getString("nombre_destino"));
                    al.setIdTipo(rs.getInt("id_tipo_fk"));
                    al.setNombreTipo(rs.getString("nombre_tipo"));

                    listado.add(al);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    public static List<Alojamiento> obtenerAlojamientosFavoritosPorUsuario(int idUsuario) {
        List<Alojamiento> favoritos = new ArrayList<>();
        String sql = "SELECT a.id_alojamiento, a.nombre, a.imagen, d.nombre AS destino "
                + "FROM favoritos f "
                + "JOIN alojamientos a ON f.id_alojamiento = a.id_alojamiento "
                + "JOIN destinos d ON a.id_destino_fk = d.id_destino "
                + "WHERE f.id_usuario = ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alojamiento aloj = new Alojamiento();
                aloj.setIdAlojamiento(rs.getInt("id_alojamiento"));
                aloj.setNombre(rs.getString("nombre"));
                aloj.setImagen(rs.getString("imagen"));
                aloj.setNombreDestino(rs.getString("destino"));
                favoritos.add(aloj);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return favoritos;
    }

    public static boolean agregarAFavoritos(int idAlojamiento, int idUsuario) {
        String sql = "INSERT IGNORE INTO favoritos (id_usuario, id_alojamiento) VALUES (?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idAlojamiento);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean existeFavorito(int idUsuario, int idAlojamiento) {
        String sql = "SELECT COUNT(*) FROM favoritos WHERE id_usuario = ? AND id_alojamiento = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idAlojamiento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean eliminarAlojamiento(int idAlojamiento) {
        String sqlEliminar = "DELETE FROM alojamientos WHERE id_alojamiento = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmtEliminar = conn.prepareStatement(sqlEliminar)) {
            stmtEliminar.setInt(1, idAlojamiento);
            return stmtEliminar.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarAlojamientoDeFavoritos(int idAlojamiento) {
        String sql = "DELETE FROM favoritos WHERE id_alojamiento = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlojamiento);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarDeFavoritos(int idAlojamiento, int idUsuario) {
        // SQL para eliminar el alojamiento de los favoritos de un usuario específico
        String sql = "DELETE FROM favoritos WHERE id_alojamiento = ? AND id_usuario = ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Establecer los parámetros de la consulta (id del alojamiento y el id del usuario)
            stmt.setInt(1, idAlojamiento);
            stmt.setInt(2, idUsuario);

            // Ejecutar la consulta
            int filasAfectadas = stmt.executeUpdate();

            // Si se eliminó alguna fila (es decir, se eliminó un favorito), retornamos true
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // En caso de error, mostramos la traza
            return false; // Retornamos false si hubo un error en la operación
        }
    }

}
