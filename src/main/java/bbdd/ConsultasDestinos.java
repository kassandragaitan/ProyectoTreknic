/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import static bbdd.Conexion.cerrarConexion;
import static bbdd.Conexion.conectar;
import static bbdd.Conexion.conn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import modelo.Destino;

/**
 *
 * @author k0343
 */
public class ConsultasDestinos {

    public static void cargarComboDestino(ComboBox<Destino> comboDestino) {
        conectar();
        try {
            String consulta = "SELECT id_destino, nombre FROM destinos";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                Destino destino = new Destino(rs.getInt("id_destino"), rs.getString("nombre"));
                comboDestino.getItems().add(destino);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosDestinos(ObservableList<Destino> listado) {
        conectar();
        try {
            String consulta = "SELECT d.id_destino, d.nombre, d.descripcion, d.fecha_creacion, d.imagen, "
                    + "COUNT(r.id_resena) as visitas, "
                    + "COALESCE(AVG(r.clasificacion), 0) as valoracion "
                    + "FROM destinos d "
                    + "LEFT JOIN resenas r ON d.id_destino = r.id_destino "
                    + "GROUP BY d.id_destino";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {
                while (rs.next()) {
                    listado.add(new Destino(
                            rs.getInt("id_destino"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha_creacion"),
                            rs.getInt("visitas"),
                            rs.getDouble("valoracion")
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
