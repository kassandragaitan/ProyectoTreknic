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
import modelo.Destino;

/**
 *
 * @author k0343
 */

public class ConsultasDestinos {

    public static boolean registrarDestino(String nombre, String descripcion, String imagen, String fecha, int idCategoria) {
        String sql = "INSERT INTO destinos (nombre, descripcion, imagen, fecha_creacion, id_categoria_fk) VALUES (?, ?, ?, NOW(), ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, imagen);
            ps.setInt(4, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar destino: " + e.getMessage());
            return false;
        }
    }

    public static boolean existeDestino(String nombre) {
        String sql = "SELECT COUNT(*) FROM destinos WHERE nombre = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean actualizarDestino(int id, String nombre, String descripcion, String imagen, int idCategoria) {
        String sql = "UPDATE destinos SET nombre = ?, descripcion = ?, imagen = ?, id_categoria_fk = ? WHERE id_destino = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, imagen);
            ps.setInt(4, idCategoria);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Destino> obtenerDestinos() {
        List<Destino> lista = new ArrayList<>();
        String sql = "SELECT d.*, c.nombre AS categoria FROM destinos d JOIN categorias c ON d.id_categoria_fk = c.id_categoria";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Destino destino = new Destino();
                destino.setId_destino(rs.getInt("id_destino"));
                destino.setNombre(rs.getString("nombre"));
                destino.setDescripcion(rs.getString("descripcion"));
                destino.setImagen(rs.getString("imagen"));
                destino.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                destino.setCategoria(rs.getString("categoria"));
                lista.add(destino);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void cargarComboDestino(ComboBox<Destino> comboDestino) {
        String consulta = "SELECT id_destino, nombre FROM destinos";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {
            while (rs.next()) {
                Destino destino = new Destino(rs.getInt("id_destino"), rs.getString("nombre"));
                comboDestino.getItems().add(destino);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarDatosDestinos(ObservableList<Destino> listado) {
        String consulta = "SELECT d.id_destino, d.nombre, d.descripcion, d.fecha_creacion, d.imagen, COUNT(r.id_resena) as visitas, COALESCE(AVG(r.clasificacion), 0) as valoracion FROM destinos d LEFT JOIN resenas r ON d.id_destino = r.id_destino GROUP BY d.id_destino";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {
            while (rs.next()) {
                listado.add(new Destino(
                        rs.getInt("id_destino"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_creacion"),
                        rs.getInt("visitas"),
                        rs.getDouble("valoracion")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<Destino> obtenerDestinosPorNombreCategoria(String nombreCategoria) {
        List<Destino> lista = new ArrayList<>();
        String sql = "SELECT d.id_destino, d.nombre, d.descripcion, d.imagen, d.fecha_creacion FROM destinos d JOIN categorias c ON d.id_categoria_fk = c.id_categoria WHERE c.nombre = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreCategoria);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Destino(
                        rs.getInt("id_destino"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_creacion"),
                        rs.getString("imagen")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<String> obtenerNombresCategorias() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT nombre FROM categorias";
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categorias.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public static ObservableList<Destino> obtenerTodosLosDestinos() {
        ObservableList<Destino> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_destino, nombre, descripcion, categoria, fecha FROM destinos WHERE activo = 1";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Destino d = new Destino(
                        rs.getInt("id_destino"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("categoria"),
                        rs.getDate("fecha")
                );
                lista.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    public static ObservableList<String> obtenerFechasDestinos() {
        ObservableList<String> fechas = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT DATE(fecha_creacion) AS fecha FROM destinos ORDER BY fecha DESC";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                fechas.add(rs.getString("fecha"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fechas;
    }

    public static ObservableList<String> obtenerCategorias() {
        ObservableList<String> categorias = FXCollections.observableArrayList();
        String sql = "SELECT nombre FROM categorias";
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categorias.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public static List<Destino> filtrarDestinos(String tipoFiltro, String valor) {
        List<Destino> lista = new ArrayList<>();
        String consulta = "SELECT d.*, c.nombre as categoria FROM destinos d LEFT JOIN categorias c ON d.id_categoria_fk = c.id_categoria WHERE ";
        if ("Filtrar por fecha".equals(tipoFiltro)) {
            consulta += "DATE(d.fecha_creacion) = ?";
        } else if ("Filtrar por categoría".equals(tipoFiltro)) {
            consulta += "c.nombre = ?";
        }
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setString(1, valor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Destino(
                        rs.getInt("id_destino"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fecha_creacion"),
                        rs.getString("imagen"),
                        rs.getString("categoria")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean tieneElementosAsociados(int idDestino) {
        String sqlResenas = "SELECT COUNT(*) FROM resenas WHERE id_destino = ?";
        String sqlAlojamientos = "SELECT COUNT(*) FROM alojamientos WHERE id_destino_fk = ?";
        String sqlActividad = "SELECT COUNT(*) FROM actividades WHERE id_destino = ?";
        boolean tieneResena = false;
        boolean tieneAlojamiento = false;
        boolean tieneActividad = false;

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmtResenas = conn.prepareStatement(sqlResenas);
             PreparedStatement stmtAlojamientos = conn.prepareStatement(sqlAlojamientos);
             PreparedStatement stmtActividad = conn.prepareStatement(sqlActividad)) {

            stmtResenas.setInt(1, idDestino);
            ResultSet rsResenas = stmtResenas.executeQuery();
            if (rsResenas.next()) {
                tieneResena = rsResenas.getInt(1) > 0;
            }

            stmtAlojamientos.setInt(1, idDestino);
            ResultSet rsAlojamientos = stmtAlojamientos.executeQuery();
            if (rsAlojamientos.next()) {
                tieneAlojamiento = rsAlojamientos.getInt(1) > 0;
            }

            stmtActividad.setInt(1, idDestino);
            ResultSet rsActividad = stmtActividad.executeQuery();
            if (rsActividad.next()) {
                tieneActividad = rsActividad.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tieneResena || tieneAlojamiento || tieneActividad;
    }

    public static boolean eliminarDestino(int idDestino) {
        String sqlEliminar = "DELETE FROM destinos WHERE id_destino = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmtEliminar = conn.prepareStatement(sqlEliminar)) {
            stmtEliminar.setInt(1, idDestino);
            return stmtEliminar.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarDestinoConAsociados(int idDestino) {
        try (Connection conn = Conexion.conectar()) {
            try (PreparedStatement stmtEliminarActividades = conn.prepareStatement("DELETE FROM actividades WHERE id_destino = ?");
                 PreparedStatement stmtEliminarResenas = conn.prepareStatement("DELETE FROM resenas WHERE id_destino = ?");
                 PreparedStatement stmtEliminarAlojamientos = conn.prepareStatement("DELETE FROM alojamientos WHERE id_destino_fk = ?");
                 PreparedStatement stmtEliminarDestino = conn.prepareStatement("DELETE FROM destinos WHERE id_destino = ?")) {

                stmtEliminarActividades.setInt(1, idDestino);
                stmtEliminarActividades.executeUpdate();

                stmtEliminarResenas.setInt(1, idDestino);
                stmtEliminarResenas.executeUpdate();

                stmtEliminarAlojamientos.setInt(1, idDestino);
                stmtEliminarAlojamientos.executeUpdate();

                stmtEliminarDestino.setInt(1, idDestino);
                return stmtEliminarDestino.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
