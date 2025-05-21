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
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import modelo.Itinerario;

/**
 * Clase que gestiona todas las operaciones relacionadas con los itinerarios
 * turísticos en la base de datos. Permite registrar, actualizar, buscar,
 * filtrar y cargar datos para visualización o selección por el usuario.
 *
 * Utiliza la tabla `itinerarios` y se relaciona con `usuarios`.
 *
 * @author k0343
 */
public class ConsultasItinerario {

    /**
     * Registra un nuevo itinerario en la base de datos.
     *
     * @param itinerario Objeto Itinerario con los datos a insertar.
     * @return true si la operación fue exitosa, false si ocurrió un error.
     */
    public static boolean registrarItinerario(Itinerario itinerario) {
        String consulta = "INSERT INTO itinerarios (nombre, descripcion, fecha_creacion, duracion, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setString(1, itinerario.getNombre());
            pst.setString(2, itinerario.getDescripcion());
            pst.setTimestamp(3, new Timestamp(itinerario.getFechaCreacion().getTime()));
            pst.setString(4, itinerario.getDuracion());
            pst.setInt(5, itinerario.getIdUsuario());
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Actualiza un itinerario existente en la base de datos.
     *
     * @param itinerario Objeto Itinerario con los nuevos valores.
     * @return true si se actualizó correctamente, false en caso contrario.
     */
    public static boolean actualizarItinerario(Itinerario itinerario) {
        String sql = "UPDATE itinerarios SET nombre = ?, descripcion = ?, duracion = ? WHERE id_itinerario = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itinerario.getNombre());
            stmt.setString(2, itinerario.getDescripcion());
            stmt.setString(3, itinerario.getDuracion());
            stmt.setInt(4, itinerario.getIdItinerario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carga las fechas distintas de creación de itinerarios.
     *
     * @return Lista observable con fechas en formato yyyy-MM-dd.
     */
    public static ObservableList<String> cargarFechasItinerarios() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT DATE_FORMAT(fecha_creacion, '%Y-%m-%d') AS fecha FROM itinerarios ORDER BY fecha DESC";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("fecha"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Carga todos los itinerarios creados en una fecha específica.
     *
     * @param lista Lista donde se insertarán los itinerarios encontrados.
     * @param fecha Fecha seleccionada para filtrar.
     */
    public static void cargarItinerariosPorFecha(ObservableList<Itinerario> lista, String fecha) {
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario WHERE DATE(i.fecha_creacion) = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(consulta)) {
            stmt.setString(1, fecha);
            ResultSet rs = stmt.executeQuery();
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
                lista.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extrae los valores enumerados (ENUM) del campo duración en la tabla
     * `itinerarios`.
     *
     * @return Lista de valores posibles de duración.
     */
    public static ObservableList<String> cargarTodasLasDuracionesEnum() {
        ObservableList<String> duraciones = FXCollections.observableArrayList();
        String consulta = "SHOW COLUMNS FROM itinerarios WHERE Field = 'duracion'";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {
            if (rs.next()) {
                String tipo = rs.getString("Type");
                tipo = tipo.substring(5, tipo.length() - 1);
                for (String val : tipo.replace("'", "").split(",")) {
                    duraciones.add(val.trim());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return duraciones;
    }

    /**
     * Carga los valores de duración del ENUM dentro de un ComboBox.
     *
     * @param comboDuracion ComboBox a llenar.
     */
    public static void cargarComboDuracionItinerario(ComboBox<String> comboDuracion) {
        comboDuracion.getItems().clear();
        comboDuracion.getItems().add("Seleccione");
        comboDuracion.getItems().addAll(cargarTodasLasDuracionesEnum());
        comboDuracion.getSelectionModel().selectFirst();
    }

    /**
     * Carga itinerarios que coincidan con una duración específica.
     *
     * @param lista Lista observable donde se insertarán los resultados.
     * @param duracion Valor de duración a filtrar.
     */
    public static void cargarItinerariosPorDuracion(ObservableList<Itinerario> lista, String duracion) {
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario WHERE i.duracion = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(consulta)) {
            stmt.setString(1, duracion);
            ResultSet rs = stmt.executeQuery();
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
                lista.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filtra los itinerarios según un texto de búsqueda. Se compara contra
     * nombre, descripción, duración, fecha y nombre del usuario.
     *
     * @param lista Lista donde se agregarán los resultados.
     * @param texto Texto de búsqueda a aplicar.
     */
    public static void cargarDatosItinerariosFiltrados(ObservableList<Itinerario> lista, String texto) {
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario WHERE i.nombre LIKE ? OR i.descripcion LIKE ? OR CAST(i.duracion AS CHAR) LIKE ? OR DATE_FORMAT(i.fecha_creacion, '%Y-%m-%d') LIKE ? OR u.nombre LIKE ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {
            String comodin = "%" + texto + "%";
            for (int i = 1; i <= 5; i++) {
                ps.setString(i, comodin);
            }
            ResultSet rs = ps.executeQuery();
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
                lista.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga todos los itinerarios registrados con sus respectivos datos y
     * nombre del usuario.
     *
     * @param lista Lista observable que se llenará con los resultados.
     */
    public static void cargarDatosItinerarios(ObservableList<Itinerario> lista) {
        String consulta = "SELECT i.*, u.nombre AS nombre_usuario FROM itinerarios i JOIN usuarios u ON i.id_usuario = u.id_usuario";
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {
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
                lista.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifica si ya existe un itinerario con el nombre especificado.
     *
     * @param nombre Nombre del itinerario.
     * @return true si ya existe, false si es único.
     */
    public static boolean existeItinerarioConNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM itinerarios WHERE nombre = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre.trim());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
