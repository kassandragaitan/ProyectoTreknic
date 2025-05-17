/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

/**
 *
 * @author k0343
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.PreguntaFrecuente;
import modelo.PruebaFuncionalidad;
import modelo.Sugerencia;
import java.sql.*;

public class ConsultasSoporte {

    public static ObservableList<PreguntaFrecuente> obtenerPreguntasFiltradas(String filtro) {
        ObservableList<PreguntaFrecuente> lista = FXCollections.observableArrayList();
        String sql = "SELECT pregunta, respuesta FROM preguntas_frecuentes WHERE pregunta LIKE ? OR respuesta LIKE ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String wildcard = "%" + filtro + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new PreguntaFrecuente(rs.getString("pregunta"), rs.getString("respuesta")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ObservableList<Sugerencia> obtenerSugerenciasFiltradas(String filtro) {
        ObservableList<Sugerencia> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_sugerencia, titulo, mensaje, fecha_envio FROM sugerencias "
                + "WHERE titulo LIKE ? OR mensaje LIKE ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String wildcard = "%" + filtro + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Sugerencia(
                            rs.getInt("id_sugerencia"),
                            rs.getString("titulo"),
                            rs.getString("mensaje"),
                            rs.getString("fecha_envio").substring(0, 10)
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ObservableList<PruebaFuncionalidad> obtenerPruebasFiltradas(String filtro) {
        ObservableList<PruebaFuncionalidad> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_funcionalidad, titulo, descripcion FROM funcionalidades_prueba "
                + "WHERE titulo LIKE ? OR descripcion LIKE ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String wildcard = "%" + filtro + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new PruebaFuncionalidad(
                            rs.getInt("id_funcionalidad"),
                            rs.getString("titulo"),
                            rs.getString("descripcion")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
