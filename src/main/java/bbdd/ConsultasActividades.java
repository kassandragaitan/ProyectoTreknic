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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.Actividad;

/**
 * Clase encargada de realizar operaciones CRUD y consultas específicas sobre la
 * tabla `actividades` en la base de datos.
 *
 * Gestiona inserciones, actualizaciones, búsquedas y filtros de actividades y
 * su relación con destinos.
 *
 * @author k0343
 */
public class ConsultasActividades {

    /**
     * Registra una nueva actividad en la base de datos.
     *
     * @param actividad Objeto {@link Actividad} a insertar.
     * @return true si se insertó correctamente, false en caso contrario.
     */
    public static boolean registrarActividad(Actividad actividad) {
        String consulta = "INSERT INTO actividades (nombre, descripcion, id_destino) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {

            pst.setString(1, actividad.getNombre());
            pst.setString(2, actividad.getDescripcion());
            pst.setInt(3, actividad.getIdDestino());

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
     * Actualiza una actividad existente en la base de datos.
     *
     * @param actividad Objeto {@link Actividad} con los nuevos datos.
     * @return true si se actualizó correctamente, false en caso de error.
     */
    public static boolean actualizarActividad(Actividad actividad) {
        String sql = "UPDATE actividades SET nombre = ?, descripcion = ?, id_destino = ? WHERE id_actividad = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, actividad.getNombre());
            stmt.setString(2, actividad.getDescripcion());
            stmt.setInt(3, actividad.getIdDestino());
            stmt.setInt(4, actividad.getIdActividad());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carga los nombres de todos los destinos disponibles para asociarlos a
     * actividades.
     *
     * @return Lista observable con los nombres de los destinos.
     */
    public static ObservableList<String> cargarNombresDestinos() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT nombre FROM destinos ORDER BY nombre";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Carga las actividades asociadas a un destino específico.
     *
     * @param lista Lista donde se almacenarán los resultados.
     * @param nombreDestino Nombre del destino a filtrar.
     */
    public static void cargarActividadesPorDestino(ObservableList<Actividad> lista, String nombreDestino) {
        String sql = "SELECT a.id_actividad, a.nombre, a.descripcion, d.nombre AS nombre_destino "
                + "FROM actividades a JOIN destinos d ON a.id_destino = d.id_destino "
                + "WHERE d.nombre = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreDestino);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Actividad actividad = new Actividad(
                            rs.getInt("id_actividad"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("nombre_destino")
                    );
                    lista.add(actividad);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga actividades filtradas por término de búsqueda (nombre, descripción
     * o destino).
     *
     * @param listaActividades Lista donde se cargarán los resultados.
     * @param busqueda Texto a buscar en los campos.
     */
    public static void cargarDatosActividadesFiltradas(ObservableList<Actividad> listaActividades, String busqueda) {
        String consulta = "SELECT a.id_actividad, a.nombre, a.descripcion, a.id_destino, d.nombre AS nombre_destino "
                + "FROM actividades a JOIN destinos d ON a.id_destino = d.id_destino "
                + "WHERE a.nombre LIKE ? OR a.descripcion LIKE ? OR d.nombre LIKE ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {
            String comodin = "%" + busqueda + "%";
            ps.setString(1, comodin);
            ps.setString(2, comodin);
            ps.setString(3, comodin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Actividad act = new Actividad();
                    act.setIdActividad(rs.getInt("id_actividad"));
                    act.setNombre(rs.getString("nombre"));
                    act.setDescripcion(rs.getString("descripcion"));
                    act.setIdDestino(rs.getInt("id_destino"));
                    act.setNombreDestino(rs.getString("nombre_destino"));
                    listaActividades.add(act);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar actividades filtradas:");
            e.printStackTrace();
        }
    }

    /**
     * Carga todas las actividades existentes junto a sus respectivos destinos.
     *
     * @param listado Lista observable donde se agregan las actividades
     * cargadas.
     */
    public static void cargarDatosActividades(ObservableList<Actividad> listado) {
        String consultaCarga = "SELECT a.id_actividad, a.nombre, a.descripcion, a.id_destino, d.nombre AS nombre_destino "
                + "FROM actividades a JOIN destinos d ON a.id_destino = d.id_destino";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
            while (rs.next()) {
                Actividad act = new Actividad();
                act.setIdActividad(rs.getInt("id_actividad"));
                act.setNombre(rs.getString("nombre"));
                act.setDescripcion(rs.getString("descripcion"));
                act.setIdDestino(rs.getInt("id_destino"));
                act.setNombreDestino(rs.getString("nombre_destino"));
                listado.add(act);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Verifica si ya existe una actividad con el mismo nombre en un destino
     * específico.
     *
     * @param nombre Nombre de la actividad.
     * @param idDestino ID del destino donde se busca la coincidencia.
     * @return true si ya existe una actividad con ese nombre en el destino,
     * false en caso contrario.
     */
    public static boolean existeActividadConNombreYDestino(String nombre, int idDestino) {
        String sql = "SELECT COUNT(*) FROM actividades WHERE LOWER(nombre) = LOWER(?) AND id_destino = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            ps.setInt(2, idDestino);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
