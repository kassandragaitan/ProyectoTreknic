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
import javafx.scene.control.ComboBox;
import modelo.TipoAlojamiento;

/**
 *
 * @author k0343
 */
public class ConsultasTipoAlojamiento {

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
