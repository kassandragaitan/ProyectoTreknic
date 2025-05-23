/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.InformeTipoAlojamiento;

/**
 * Clase que contiene métodos para manejar la lógica de reportes y consultas
 * estadísticas relacionadas con alojamientos, usuarios y sus preferencias.
 *
 * Utiliza la clase {@link Conexion} para establecer conexiones con la base de
 * datos. Proporciona métodos para obtener datos para
 * gráficos, y filtrar estadísticas según idioma o tipo de alojamiento.
 *
 * @author k0343
 */
public class ConsultasReportes {

    /**
     * Obtiene una lista con la cantidad de alojamientos por tipo.
     *
     * @return Lista observable de {@link InformeTipoAlojamiento} ordenada por
     * cantidad descendente.
     */
    public static ObservableList<InformeTipoAlojamiento> obtenerDatosTiposAlojamiento() {
        ObservableList<InformeTipoAlojamiento> lista = FXCollections.observableArrayList();
        String sql = "SELECT tipoalojamiento.tipo, COUNT(*) AS cantidad "
                + "FROM alojamientos "
                + "JOIN tipoalojamiento ON alojamientos.id_tipo_fk = tipoalojamiento.id_tipo "
                + "GROUP BY tipoalojamiento.tipo "
                + "ORDER BY cantidad DESC";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new InformeTipoAlojamiento(
                        rs.getString("tipo"),
                        rs.getInt("cantidad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Devuelve una lista de idiomas únicos registrados por los usuarios.
     *
     * @return Lista observable con los idiomas preferidos.
     */
    public static ObservableList<String> obtenerIdiomasUsuarios() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT idioma_preferido FROM usuarios";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("idioma_preferido"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Devuelve una lista de tipos de alojamiento disponibles.
     *
     * @return Lista observable con los tipos. El primer valor es "Todos los
     * tipos".
     */
    public static ObservableList<String> obtenerTiposAlojamiento() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        lista.add("Todos los tipos");
        String sql = "SELECT DISTINCT tipo FROM tipoalojamiento";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("tipo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Obtiene la cantidad de usuarios registrados por mes para un idioma
     * específico.
     *
     * @param idioma Idioma preferido seleccionado.
     * @return Mapa con los nombres de los meses como clave y la cantidad de
     * usuarios como valor.
     */
    public static Map<String, Integer> obtenerUsuariosPorMesYIdioma(String idioma) {
        Map<String, Integer> datos = new LinkedHashMap<>();
        String sql = "SELECT MONTH(fecha_registro) AS mes, COUNT(*) AS cantidad "
                + "FROM usuarios WHERE idioma_preferido = ? "
                + "GROUP BY mes ORDER BY mes";
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idioma);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int mesIndex = rs.getInt("mes") - 1;
                datos.put(meses[mesIndex], rs.getInt("cantidad"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    /**
     * Obtiene la cantidad de alojamientos para un tipo de alojamiento
     * específico.
     *
     * @param tipo Tipo de alojamiento a filtrar.
     * @return Lista observable con los resultados del conteo por tipo.
     */
    public static ObservableList<InformeTipoAlojamiento> obtenerDatosTiposAlojamientoPorTipo(String tipo) {
        ObservableList<InformeTipoAlojamiento> lista = FXCollections.observableArrayList();
        String sql = "SELECT tipoalojamiento.tipo, COUNT(*) AS cantidad "
                + "FROM alojamientos "
                + "JOIN tipoalojamiento ON alojamientos.id_tipo_fk = tipoalojamiento.id_tipo "
                + "WHERE tipoalojamiento.tipo = ? "
                + "GROUP BY tipoalojamiento.tipo";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new InformeTipoAlojamiento(
                        rs.getString("tipo"),
                        rs.getInt("cantidad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
