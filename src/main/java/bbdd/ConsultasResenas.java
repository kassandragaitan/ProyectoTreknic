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
import javafx.collections.ObservableList;
import modelo.Destino;
import modelo.Resena;
import modelo.Usuario;

/**
 *
 * @author k0343
 */
public class ConsultasResenas {

    public static List<Resena> obtenerResenas() {
        List<Resena> resenas = new ArrayList<>();
        String sql = "SELECT r.id_resena, d.nombre as destino_nombre, r.comentario, r.clasificacion, u.nombre as usuario_nombre "
                + "FROM resenas r "
                + "JOIN destinos d ON r.id_destino = d.id_destino "
                + "JOIN usuarios u ON r.id_usuario = u.id_usuario";
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resenas.add(new Resena(
                        rs.getInt("id_resena"),
                        rs.getString("destino_nombre"),
                        rs.getString("comentario"),
                        rs.getInt("clasificacion"),
                        rs.getString("usuario_nombre")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reseñas: " + e.getMessage());
            e.printStackTrace();
        }
        return resenas;
    }

    public static boolean insertarResena(int idDestino, int idUsuario, String comentario, int clasificacion) {
        String sql = "INSERT INTO resenas (id_destino, id_usuario, comentario, clasificacion) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDestino);
            stmt.setInt(2, idUsuario);
            stmt.setString(3, comentario);
            stmt.setInt(4, clasificacion);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Destino> obtenerDestinos() {
        List<Destino> lista = new ArrayList<>();
        String sql = "SELECT id_destino, nombre FROM destinos";
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Destino(rs.getInt("id_destino"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre FROM usuarios";
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("id_usuario"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void buscarResenasPorDestino(ObservableList<Resena> lista, String texto) {
        String sql = "SELECT r.*, u.nombre AS nombre_usuario, d.nombre AS nombre_destino "
                + "FROM resenas r "
                + "JOIN usuarios u ON r.id_usuario = u.id_usuario "
                + "JOIN destinos d ON r.id_destino = d.id_destino "
                + "WHERE d.nombre LIKE ? OR r.comentario LIKE ? OR CAST(r.clasificacion AS CHAR) LIKE ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String wildcard = "%" + texto + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            ps.setString(3, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Resena(
                            rs.getInt("id_resena"),
                            rs.getString("nombre_destino"),
                            rs.getString("comentario"),
                            rs.getInt("clasificacion"),
                            rs.getString("nombre_usuario")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void buscarResenasPorUsuario(ObservableList<Resena> lista, String texto) {
        String sql = "SELECT r.*, u.nombre AS nombre_usuario, d.nombre AS nombre_destino "
                + "FROM resenas r "
                + "JOIN usuarios u ON r.id_usuario = u.id_usuario "
                + "JOIN destinos d ON r.id_destino = d.id_destino "
                + "WHERE u.nombre LIKE ? OR r.comentario LIKE ? OR CAST(r.clasificacion AS CHAR) LIKE ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String wildcard = "%" + texto + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            ps.setString(3, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Resena(
                            rs.getInt("id_resena"),
                            rs.getString("nombre_destino"),
                            rs.getString("comentario"),
                            rs.getInt("clasificacion"),
                            rs.getString("nombre_usuario")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
