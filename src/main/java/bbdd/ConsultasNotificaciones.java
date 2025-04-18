/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import static bbdd.Conexion.cerrarConexion;
import static bbdd.Conexion.conectar;
import static bbdd.Conexion.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import modelo.Notificacion;

/**
 *
 * @author k0343
 */
public class ConsultasNotificaciones {

    public static void cargarDatosNotificaciones(ObservableList<Notificacion> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT n.id_notificacion, n.descripcion, n.fecha, n.notificacion, n.id_usuario, u.nombre as nombreDestinatario "
                    + "FROM notificaciones n "
                    + "JOIN usuarios u ON n.id_usuario = u.id_usuario";
            PreparedStatement pst = conn.prepareStatement(consultaCarga);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                listado.add(new Notificacion(
                        rs.getInt("id_notificacion"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha"),
                        rs.getString("notificacion"),
                        rs.getInt("id_usuario"),
                        rs.getString("nombreDestinatario")
                ));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }
}
