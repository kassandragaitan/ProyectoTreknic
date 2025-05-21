/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import static bbdd.Conexion.cerrarConexion;
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
import modelo.Usuario;

/**
 * Clase que proporciona métodos para gestionar los alojamientos en la base de
 * datos. Incluye operaciones de registro, actualización, consulta, filtrado y
 * gestión de favoritos.
 *
 * Esta clase interactúa con las tablas `alojamientos`, `tipoalojamiento`,
 * `destinos` y `favoritos`.
 *
 * @author k0343
 */
public class ConsultasAlojamientos {

    /**
     * Inserta un nuevo alojamiento en la base de datos.
     *
     * @param alojamiento Objeto a registrar.
     * @return true si se registró correctamente, false si ocurrió un error.
     */
    public static boolean registrarAlojamiento(Alojamiento alojamiento) {
        String consulta = "INSERT INTO alojamientos (nombre, id_tipo_fk, contacto, imagen, id_destino_fk) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
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

    /**
     * Actualiza los datos de un alojamiento existente.
     *
     * @param alojamiento Alojamiento con los datos modificados.
     * @return true si se actualizó correctamente, false en caso de error.
     */
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

    /**
     * Carga una lista de tipos de alojamiento desde la base de datos.
     *
     * @return Lista observable de tipos.
     */
    public static ObservableList<String> cargarTiposAlojamiento() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT tipo FROM tipoalojamiento";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("tipo"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Carga una lista de destinos desde la base de datos.
     *
     * @return Lista observable de nombres de destinos.
     */
    public static ObservableList<String> cargarDestinosAlojamiento() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT nombre FROM destinos";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Carga alojamientos filtrados por tipo.
     *
     * @param lista Lista destino donde se cargan los alojamientos.
     * @param tipo Tipo de alojamiento a filtrar.
     */
    public static void cargarAlojamientosPorTipo(ObservableList<Alojamiento> lista, String tipo) {
        String sql = "SELECT a.*, t.tipo, d.nombre AS nombre_destino "
                + "FROM alojamientos a "
                + "JOIN tipoalojamiento t ON a.id_tipo_fk = t.id_tipo "
                + "JOIN destinos d ON a.id_destino_fk = d.id_destino "
                + "WHERE t.tipo = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga alojamientos filtrados por destino.
     *
     * @param lista Lista destino donde se cargan los alojamientos.
     * @param destino Nombre del destino a filtrar.
     */
    public static void cargarAlojamientosPorDestino(ObservableList<Alojamiento> lista, String destino) {
        String sql = "SELECT a.*, t.tipo, d.nombre AS nombre_destino "
                + "FROM alojamientos a "
                + "JOIN tipoalojamiento t ON a.id_tipo_fk = t.id_tipo "
                + "JOIN destinos d ON a.id_destino_fk = d.id_destino "
                + "WHERE d.nombre = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, destino);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga alojamientos que coincidan con un texto de búsqueda en múltiples
     * campos.
     *
     * @param listaAlojamientos Lista donde se cargarán los resultados.
     * @param busqueda Texto a buscar (nombre, contacto, destino o tipo).
     */
    public static void cargarDatosAlojamientosFiltrados(ObservableList<Alojamiento> listaAlojamientos, String busqueda) {
        int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

        String consulta = "SELECT a.id_alojamiento, a.nombre, a.contacto, a.imagen, "
                + "a.id_destino_fk, d.nombre AS nombre_destino, "
                + "a.id_tipo_fk, t.tipo AS nombre_tipo, "
                + "IF(f.id_usuario IS NOT NULL, 1, 0) AS es_favorito "
                + "FROM alojamientos a "
                + "JOIN destinos d ON a.id_destino_fk = d.id_destino "
                + "JOIN tipoalojamiento t ON a.id_tipo_fk = t.id_tipo "
                + "LEFT JOIN favoritos f ON a.id_alojamiento = f.id_alojamiento AND f.id_usuario = ? "
                + "WHERE a.nombre LIKE ? OR a.contacto LIKE ? OR d.nombre LIKE ? OR t.tipo LIKE ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            ps.setInt(1, idUsuario);
            ps.setString(2, wildcard);
            ps.setString(3, wildcard);
            ps.setString(4, wildcard);
            ps.setString(5, wildcard);

            ResultSet rs = ps.executeQuery();
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
                al.setFavorito(rs.getInt("es_favorito") == 1);
                listaAlojamientos.add(al);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar alojamientos filtrados:");
            e.printStackTrace();
        }
    }

    /**
     * Carga datos de tipo de alojamiento en un ComboBox.
     *
     * @param comboTipo ComboBox donde se cargarán los tipos.
     */
    public static void cargarComboTipoAlojamiento(ComboBox<TipoAlojamiento> comboTipo) {
        String consulta = "SELECT id_tipo, tipo FROM tipoalojamiento";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {

            while (rs.next()) {
                TipoAlojamiento tipo = new TipoAlojamiento(rs.getInt("id_tipo"), rs.getString("tipo"));
                comboTipo.getItems().add(tipo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga todos los alojamientos con su información, incluyendo si son
     * favoritos del usuario actual.
     *
     * @param listado Lista destino donde se agregan los alojamientos.
     */
    public static void cargarDatosAlojamientos(ObservableList<Alojamiento> listado) {
        int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

        String sql = "SELECT a.id_alojamiento, a.nombre, a.contacto, a.imagen, "
                + "a.id_destino_fk, d.nombre AS nombre_destino, "
                + "a.id_tipo_fk, t.tipo AS nombre_tipo, "
                + "IF(f.id_usuario IS NOT NULL, 1, 0) AS es_favorito "
                + "FROM alojamientos a "
                + "JOIN destinos d ON a.id_destino_fk = d.id_destino "
                + "JOIN tipoalojamiento t ON a.id_tipo_fk = t.id_tipo "
                + "LEFT JOIN favoritos f ON a.id_alojamiento = f.id_alojamiento AND f.id_usuario = ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idUsuario);
            ResultSet rs = pst.executeQuery();

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
                al.setFavorito(rs.getInt("es_favorito") == 1);
                listado.add(al);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene una lista de alojamientos marcados como favoritos por un usuario
     * específico.
     *
     * @param idUsuario ID del usuario.
     * @return Lista de alojamientos favoritos.
     */
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

    /**
     * Agrega un alojamiento a la lista de favoritos de un usuario. Ignora si ya
     * existe la relación para evitar duplicados.
     *
     * @param idAlojamiento ID del alojamiento.
     * @param idUsuario ID del usuario.
     * @return true si se insertó correctamente, false si no se realizó ningún
     * cambio.
     */
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

    /**
     * Verifica si un alojamiento ya está marcado como favorito por un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param idAlojamiento ID del alojamiento.
     * @return true si ya existe la relación, false en caso contrario.
     */
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

    /**
     * Elimina un alojamiento de forma permanente de la base de datos.
     *
     * @param idAlojamiento ID del alojamiento a eliminar.
     * @return true si se eliminó correctamente, false si ocurrió un error.
     */
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

    /**
     * Elimina todas las referencias de un alojamiento en la tabla de favoritos.
     *
     * @param idAlojamiento ID del alojamiento a eliminar de favoritos.
     * @return true si se eliminó correctamente, false si falló.
     */
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

    /**
     * Elimina un alojamiento específico de la lista de favoritos de un usuario.
     *
     * @param idAlojamiento ID del alojamiento.
     * @param idUsuario ID del usuario.
     * @return true si se eliminó correctamente, false si no se modificó nada.
     */
    public static boolean eliminarDeFavoritos(int idAlojamiento, int idUsuario) {
        String sql = "DELETE FROM favoritos WHERE id_alojamiento = ? AND id_usuario = ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlojamiento);
            stmt.setInt(2, idUsuario);

            int filasAfectadas = stmt.executeUpdate();

            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si ya existe un alojamiento registrado con el nombre indicado.
     *
     * @param nombre Nombre del alojamiento.
     * @return true si el nombre ya está en uso, false si es único.
     */
    public static boolean existeNombreAlojamiento(String nombre) {
        String sql = "SELECT COUNT(*) FROM alojamientos WHERE nombre = ?";
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

}
