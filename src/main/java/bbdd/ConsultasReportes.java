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
import modelo.Reporte;

/**
 *
 * @author k0343
 */
public class ConsultasReportes {

    public static boolean registrarReporte(Reporte reporte) {
        boolean exito = false;
        try {
            Conexion.conectar();
            String sql = "INSERT INTO reportes (tipo, descripcion, fecha, id_usuario) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = Conexion.conn.prepareStatement(sql);
            stmt.setString(1, reporte.getTipo());
            stmt.setString(2, reporte.getDescripcion());
            stmt.setTimestamp(3, new Timestamp(reporte.getFecha().getTime()));
            stmt.setInt(4, reporte.getIdUsuario());

            exito = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }


    //REVISAAAARRRRR ESTTTTTOOO KASSAAANNNDRAAAA
    public static ObservableList<InformeTipoAlojamiento> obtenerDatosTiposAlojamiento() {
        ObservableList<InformeTipoAlojamiento> lista = FXCollections.observableArrayList();
        String sql = "SELECT tipoalojamiento.tipo, COUNT(*) AS cantidad "
                + "FROM alojamientos "
                + "JOIN tipoalojamiento ON alojamiento.id_tipo_fk = tipoalojamiento.id_tipo "
                + "GROUP BY tipoalojamiento.tipo "
                + "ORDER BY cantidad DESC";

        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new InformeTipoAlojamiento(
                        rs.getString("tipo"),
                        rs.getInt("cantidad")
                ));
            }

            rs.close();
            ps.close();
            Conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static ObservableList<String> obtenerIdiomasUsuarios() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT idioma_preferido FROM usuarios";

        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("idioma_preferido"));
            }

            rs.close();
            ps.close();
            Conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ObservableList<String> obtenerTiposAlojamiento() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        lista.add("Todos los tipos");

        String sql = "SELECT DISTINCT tipo FROM tipoalojamiento";

        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("tipo"));
            }

            rs.close();
            ps.close();
            Conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static Map<String, Integer> obtenerUsuariosPorMesYIdioma(String idioma) {
        Map<String, Integer> datos = new LinkedHashMap<>();
        String sql = "SELECT MONTH(fecha_registro) AS mes, COUNT(*) AS cantidad "
                + "FROM usuarios WHERE idioma_preferido = ? "
                + "GROUP BY mes ORDER BY mes";

        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idioma);
            ResultSet rs = ps.executeQuery();

            String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
            while (rs.next()) {
                int mesIndex = rs.getInt("mes") - 1;
                String nombreMes = meses[mesIndex];
                datos.put(nombreMes, rs.getInt("cantidad"));
            }

            rs.close();
            ps.close();
            Conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    public static ObservableList<InformeTipoAlojamiento> obtenerDatosTiposAlojamientoPorTipo(String tipo) {
        ObservableList<InformeTipoAlojamiento> lista = FXCollections.observableArrayList();
        String sql = "SELECT tipoalojamiento.tipo, COUNT(*) AS cantidad "
                + "FROM alojamientos "
                + "JOIN tipoalojamiento ON alojamiento.id_tipo_fk = tipoalojamiento.id_tipo "
                + "WHERE tipoalojamiento.tipo = ? "
                + "GROUP BY tipoalojamiento.tipo";

        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new InformeTipoAlojamiento(
                        rs.getString("tipo"),
                        rs.getInt("cantidad")
                ));
            }

            rs.close();
            ps.close();
            Conexion.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
