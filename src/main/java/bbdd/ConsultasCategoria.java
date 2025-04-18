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
import modelo.Categoria;

/**
 *
 * @author k0343
 */
public class ConsultasCategoria {

    public static boolean registrarCategoria(Categoria categoria) {
        conectar();
        try {
            String consulta = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, categoria.getNombre());
            pst.setString(2, categoria.getDescripcion());
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosCategoriasFiltradas(ObservableList<Categoria> listaCategorias, String busqueda) {
        Connection conn = Conexion.conn;
        String consulta = "SELECT id_categoria, nombre, descripcion FROM categoria WHERE nombre LIKE ? OR descripcion LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setIdCategoria(rs.getInt("id_categoria"));
                    cat.setNombre(rs.getString("nombre"));
                    cat.setDescripcion(rs.getString("descripcion"));

                    listaCategorias.add(cat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar categorías filtradas:");
            e.printStackTrace();
        }
    }

    public static void cargarDatosCategorias(ObservableList<Categoria> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT id_categoria, nombre, descripcion FROM categoria";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    listado.add(new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }
}
