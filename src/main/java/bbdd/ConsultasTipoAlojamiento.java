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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import modelo.TipoAlojamiento;

/**
 * Clase de acceso a datos para la entidad {@link TipoAlojamiento}.
 *
 * Permite realizar operaciones CRUD y filtrado en la tabla
 * {@code tipoalojamiento}. Se conecta a la base de datos utilizando la clase
 * {@link Conexion}.
 *
 * Métodos disponibles:
 * <ul>
 * <li>Registrar nuevo tipo de alojamiento</li>
 * <li>Actualizar tipo existente</li>
 * <li>Verificar existencia de un tipo</li>
 * <li>Cargar datos filtrados o generales</li>
 * <li>Cargar en {@code ComboBox}</li>
 * </ul>
 *
 * @author k0343
 */
public class ConsultasTipoAlojamiento {

    /**
     * Inserta un nuevo tipo de alojamiento en la base de datos.
     *
     * @param tipo Objeto {@link TipoAlojamiento} con el nombre del tipo.
     * @return {@code true} si se insertó correctamente, {@code false} en caso
     * de error.
     */
    public static boolean registrarTipoAlojamiento(TipoAlojamiento tipo) {
        String consulta = "INSERT INTO tipoalojamiento (tipo) VALUES (?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {

            pst.setString(1, tipo.getTipo());
            return pst.executeUpdate() > 0;

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Actualiza un tipo de alojamiento existente por su ID.
     *
     * @param tipo Objeto {@link TipoAlojamiento} con los datos actualizados.
     * @return {@code true} si la actualización fue exitosa, {@code false} si
     * ocurrió un error.
     */
    public static boolean actualizarTipoAlojamiento(TipoAlojamiento tipo) {
        String sql = "UPDATE tipoalojamiento SET tipo = ? WHERE id_tipo = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.getTipo());
            stmt.setInt(2, tipo.getIdTipo());
            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Carga los tipos de alojamiento que coincidan con un texto de búsqueda.
     *
     * @param listaTipos Lista observable donde se añadirán los resultados.
     * @param busqueda Texto parcial a buscar en el campo tipo.
     */
    public static void cargarDatosTiposAlojamientoFiltrados(ObservableList<TipoAlojamiento> listaTipos, String busqueda) {
        String consulta = "SELECT id_tipo, tipo FROM tipoalojamiento WHERE tipo LIKE ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {

            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoAlojamiento tipoAloj = new TipoAlojamiento();
                    tipoAloj.setIdTipo(rs.getInt("id_tipo"));
                    tipoAloj.setTipo(rs.getString("tipo"));
                    listaTipos.add(tipoAloj);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar tipos de alojamiento filtrados:");
            e.printStackTrace();
        }
    }

    /**
     * Carga todos los tipos de alojamiento disponibles en un ComboBox.
     *
     * @param comboTipo ComboBox donde se insertarán los tipos.
     */
    public static void cargarDatosTiposAlojamientoRegistrar(ComboBox<TipoAlojamiento> comboTipo) {
        String consultaCarga = "SELECT id_tipo, tipo FROM tipoalojamiento";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {

            while (rs.next()) {
                TipoAlojamiento tipo = new TipoAlojamiento(rs.getInt("id_tipo"), rs.getString("tipo"));
                comboTipo.getItems().add(tipo);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga todos los tipos de alojamiento en una lista observable para mostrar
     * en tabla.
     *
     * @param listado Lista observable donde se añadirán los resultados.
     */
    public static void cargarDatosTiposAlojamiento(ObservableList<TipoAlojamiento> listado) {
        String consultaCarga = "SELECT id_tipo, tipo FROM tipoalojamiento";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {

            while (rs.next()) {
                listado.add(new TipoAlojamiento(
                        rs.getInt("id_tipo"),
                        rs.getString("tipo")
                ));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Verifica si ya existe un tipo de alojamiento con el mismo nombre.
     *
     * @param tipo Nombre del tipo de alojamiento a comprobar.
     * @return {@code true} si ya existe en la base de datos, {@code false} si
     * no.
     */
    public static boolean existeTipoAlojamiento(String tipo) {
        String sql = "SELECT COUNT(*) FROM tipoalojamiento WHERE tipo = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
