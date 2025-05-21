/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import modelo.Categoria;

/**
 * Clase que gestiona las operaciones CRUD relacionadas con las categorías de
 * destinos. Incluye métodos para registrar, actualizar, eliminar y consultar
 * categorías, ya sea de forma general o con filtros de búsqueda.
 *
 * Esta clase interactúa directamente con la tabla `categorias` en la base de
 * datos.
 *
 * @author k0343
 */
public class ConsultasCategoria {

    /**
     * Registra una nueva categoría en la base de datos.
     *
     * @param categoria Objeto {@link Categoria} con nombre y descripción.
     * @return true si el registro fue exitoso, false si ocurrió un error.
     */
    public static boolean registrarCategoria(Categoria categoria) {
        String consulta = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setString(1, categoria.getNombre());
            pst.setString(2, categoria.getDescripcion());
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Carga las categorías que coincidan parcial o totalmente con un texto de
     * búsqueda.
     *
     * @param listaCategorias Lista observable donde se agregarán los
     * resultados.
     * @param busqueda Texto a buscar en nombre o descripción.
     */
    public static void cargarDatosCategoriasFiltradas(ObservableList<Categoria> listaCategorias, String busqueda) {
        String consulta = "SELECT id_categoria, nombre, descripcion FROM categorias WHERE nombre LIKE ? OR descripcion LIKE ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {
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

    /**
     * Carga todas las categorías existentes en la base de datos.
     *
     * @param listado Lista observable donde se almacenarán los datos.
     */
    public static void cargarDatosCategorias(ObservableList<Categoria> listado) {
        String consultaCarga = "SELECT * FROM categorias";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
            while (rs.next()) {
                listado.add(new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene una lista de categorías en forma de objetos simples (id y
     * nombre).
     *
     * @return Lista de categorías con ID y nombre (sin descripción).
     */
    public static List<Categoria> obtenerCategorias() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre FROM categorias";

        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_categoria");
                String nombre = rs.getString("nombre");
                Categoria c = new Categoria(id, nombre);
                lista.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    /**
     * Verifica si ya existe una categoría registrada con el mismo nombre.
     *
     * @param nombre Nombre de la categoría a comprobar.
     * @return true si el nombre ya existe, false si es único.
     */
    public static boolean existeCategoria(String nombre) {
        String sql = "SELECT COUNT(*) FROM categorias WHERE nombre = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Actualiza los datos de una categoría existente.
     *
     * @param categoria Objeto con ID y nuevos valores a actualizar.
     * @return true si se actualizó correctamente, false en caso contrario.
     */
    public static boolean actualizarCategoria(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, categoria.getNombre());
            pst.setString(2, categoria.getDescripcion());
            pst.setInt(3, categoria.getIdCategoria());
            int filas = pst.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Elimina una categoría de la base de datos según su ID.
     *
     * @param id ID de la categoría a eliminar.
     * @return true si se eliminó correctamente, false si ocurrió un error.
     */
    public static boolean eliminarPorId(int id) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
