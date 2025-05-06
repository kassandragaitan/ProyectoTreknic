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
        String sql = "INSERT INTO destinos (nombre, descripcion, imagen, fecha_creacion, id_categoria_fk) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setString(3, imagen);
            stmt.setString(4, fecha);
            stmt.setInt(5, idCategoria);

            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Destino> obtenerDestinos() {
        List<Destino> lista = new ArrayList<>();
        conectar();
        try {
            String sql = "SELECT * FROM destinos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Destino destino = new Destino();
                destino.setId_destino(rs.getInt("id_destino"));
                destino.setNombre(rs.getString("nombre"));
                destino.setDescripcion(rs.getString("descripcion"));
                destino.setImagen(rs.getString("imagen"));
                destino.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                lista.add(destino);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }
        return lista;
    }

    public static void cargarComboDestino(ComboBox<Destino> comboDestino) {
        conectar();
        try {
            String consulta = "SELECT id_destino, nombre FROM destinos";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                Destino destino = new Destino(rs.getInt("id_destino"), rs.getString("nombre"));
                comboDestino.getItems().add(destino);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosDestinos(ObservableList<Destino> listado) {
        conectar();
        try {
            String consulta = "SELECT d.id_destino, d.nombre, d.descripcion, d.fecha_creacion, d.imagen, "
                    + "COUNT(r.id_resena) as visitas, "
                    + "COALESCE(AVG(r.clasificacion), 0) as valoracion "
                    + "FROM destinos d "
                    + "LEFT JOIN resenas r ON d.id_destino = r.id_destino "
                    + "GROUP BY d.id_destino";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

   
    public static List<Destino> obtenerDestinosPorCategoria(String categoria) {
        List<Destino> lista = new ArrayList<>();
        conectar();
        try {
            String sql = "SELECT id_destino, nombre, descripcion, imagen, fecha_creacion "
                    + "FROM destinos WHERE categoria = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, categoria);
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
        } finally {
            cerrarConexion();
        }
        return lista;
    }

    public static List<Destino> obtenerDestinosPorNombreCategoria(String nombreCategoria) {
        List<Destino> lista = new ArrayList<>();
        conectar();
        try {
            String sql = "SELECT d.id_destino, d.nombre, d.descripcion, d.imagen, d.fecha_creacion "
                    + "FROM destinos d "
                    + "JOIN categoria c ON d.id_categoria_fk = c.id_categoria "
                    + "WHERE c.nombre = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
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
        } finally {
            cerrarConexion();
        }
        return lista;
    }

    public static List<String> obtenerNombresCategorias() {
        List<String> categorias = new ArrayList<>();
        conectar();
        try {
            String sql = "SELECT nombre FROM categoria";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                categorias.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
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
}
