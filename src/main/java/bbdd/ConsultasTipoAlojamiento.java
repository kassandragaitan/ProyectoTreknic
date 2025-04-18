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
        conectar();
        try {
            String consulta = "INSERT INTO tipoalojamiento (tipo) VALUES (?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, tipo.getTipo());

            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosTiposAlojamientoFiltrados(ObservableList<TipoAlojamiento> listaTipos, String busqueda) {
        Connection conn = Conexion.conn;

        String consulta = "SELECT id_tipo, tipo FROM tipoalojamiento WHERE tipo LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
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
        conectar();
        try {
            String consultaCarga = "SELECT id_tipo, tipo FROM tipoalojamiento";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consultaCarga);
            while (rs.next()) {
                TipoAlojamiento tipo = new TipoAlojamiento(rs.getInt("id_tipo"), rs.getString("tipo"));
                comboTipo.getItems().add(tipo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosTiposAlojamiento(ObservableList<TipoAlojamiento> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT id_tipo, tipo FROM tipoalojamiento";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    listado.add(new TipoAlojamiento(
                            rs.getInt("id_tipo"),
                            rs.getString("tipo")
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
