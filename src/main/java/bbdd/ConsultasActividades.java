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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import modelo.Actividad;
import modelo.Destino;

/**
 *
 * @author k0343
 */
public class ConsultasActividades {

    public static boolean registrarActividad(Actividad actividad) {
        conectar();
        try {
            String consulta = "INSERT INTO actividades (nombre, descripcion, id_destino) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
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
public static boolean actualizarActividad(Actividad actividad) {
    Conexion.conectar();
    try {
        String sql = "UPDATE actividades SET nombre = ?, descripcion = ?, id_destino = ? WHERE id_actividad = ?";
        PreparedStatement stmt = Conexion.conn.prepareStatement(sql);
        stmt.setString(1, actividad.getNombre());
        stmt.setString(2, actividad.getDescripcion());
        stmt.setInt(3, actividad.getIdDestino());
        stmt.setInt(4, actividad.getIdActividad());

        int filas = stmt.executeUpdate();
        return filas > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
        Conexion.cerrarConexion();
    }
}
public static ObservableList<String> cargarNombresDestinos() {
    ObservableList<String> lista = FXCollections.observableArrayList();
    String sql = "SELECT DISTINCT nombre FROM destinos ORDER BY nombre";
    try {
        PreparedStatement ps = Conexion.conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(rs.getString("nombre"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}

public static void cargarActividadesPorDestino(ObservableList<Actividad> lista, String nombreDestino) {
    String sql = "SELECT a.id_actividad, a.nombre, a.descripcion, d.nombre AS nombre_destino " +
                 "FROM actividades a " +
                 "JOIN destinos d ON a.id_destino = d.id_destino " +
                 "WHERE d.nombre = ?";

    try {
        PreparedStatement ps = Conexion.conn.prepareStatement(sql);
        ps.setString(1, nombreDestino);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Actividad actividad = new Actividad(
                    rs.getInt("id_actividad"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getString("nombre_destino")
            );
            lista.add(actividad);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public static void cargarDatosActividadesFiltradas(ObservableList<Actividad> listaActividades, String busqueda) {
        Connection conn = Conexion.conn;

        String consulta = "SELECT a.id_actividad, a.nombre, a.descripcion, a.id_destino, d.nombre AS nombre_destino "
                + "FROM actividades a "
                + "JOIN destinos d ON a.id_destino = d.id_destino "
                + "WHERE a.nombre LIKE ? OR a.descripcion LIKE ? OR d.nombre LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
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
                    act.setNombreDestino(rs.getString("nombre_destino")); // importante

                    listaActividades.add(act);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar actividades filtradas:");
            e.printStackTrace();
        }
    }

    public static void cargarDatosActividades(ObservableList<Actividad> listado) {
        try {
//            String consultaCarga = "SELECT id_actividad, nombre, descripcion, id_destino FROM actividades";
            String consultaCarga = "SELECT a.id_actividad, a.nombre, a.descripcion, a.id_destino, d.nombre AS nombre_destino "
                    + "FROM actividades a JOIN destinos d ON a.id_destino = d.id_destino";

            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    Actividad act = new Actividad();
                    act.setIdActividad(rs.getInt("id_actividad"));
                    act.setNombre(rs.getString("nombre"));
                    act.setDescripcion(rs.getString("descripcion"));
                    act.setIdDestino(rs.getInt("id_destino"));
                    act.setNombreDestino(rs.getString("nombre_destino"));
                    listado.add(act);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }
}
