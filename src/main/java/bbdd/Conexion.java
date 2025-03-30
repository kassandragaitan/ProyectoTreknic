/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import modelo.Itinerario;
import modelo.ItinerarioTabla;
import modelo.UsuarioRegistro;

/**
 *
 * @author k0343
 */
public class Conexion {

    static Connection conn;
//     public static final String URL = "jdbc:mysql://145.14.151.1/u812167471_alojamientos2";

    public static final String URL = "jdbc:mysql://localhost/treknic";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    public static Connection conectar() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
//            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;

    }

    public static void cerrarConexion() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean registrarUsuario(UsuarioRegistro usuarioRegistro) {
        conectar();
        try {
            String consulta = "INSERT INTO usuarios (nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, usuarioRegistro.getNombre());
            pst.setString(2, usuarioRegistro.getEmail());
            pst.setString(3, usuarioRegistro.getContrasena());
            pst.setString(4, usuarioRegistro.getTipoUsuario());
            pst.setString(5, usuarioRegistro.getIdioma());
            pst.setString(6, usuarioRegistro.getTipoViajero());
            pst.setString(7, usuarioRegistro.getTelefono());
            pst.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // Establecer la fecha y hora actuales como fecha_registro
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

     

    public static void cargarComboTipoUsuario(ComboBox comboTipoUsuario) {
        try {
            String consulta = "SHOW COLUMNS FROM usuarios LIKE 'tipo_usuario'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            if (rs.next()) {
                String enumValues = rs.getString("Type");
                enumValues = enumValues.substring(5, enumValues.length() - 1);
                String[] values = enumValues.split("','");
                for (String value : values) {
                    comboTipoUsuario.getItems().add(value.replace("'", ""));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarComboTipoCompania(ComboBox comboTipoCompania) {
        try {
            String consulta = "SHOW COLUMNS FROM usuarios LIKE 'tipo_viajero'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            if (rs.next()) {
                String tipoCompania = rs.getString("Type");
                tipoCompania = tipoCompania.substring(5, tipoCompania.length() - 1).replace("'", "");
                String[] valoresEnum = tipoCompania.split(",");
                for (String valor : valoresEnum) {
                    comboTipoCompania.getItems().add(valor);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarComboIdioma(ComboBox comboIdioma) {
        try {
            String consulta = "SHOW COLUMNS FROM usuarios WHERE Field = 'idioma_preferido'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            if (rs.next()) {
                String idiomaPreferido = rs.getString("Type");
                idiomaPreferido = idiomaPreferido.substring(5, idiomaPreferido.length() - 1).replace("'", "");
                String[] valoresEnum = idiomaPreferido.split(",");
                for (String valor : valoresEnum) {
                    comboIdioma.getItems().add(valor);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ///////////////////
    public static void cargarDatosItinerarios(ObservableList<ItinerarioTabla> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion, id_usuario FROM itinerario";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    listado.add(new ItinerarioTabla(
                            rs.getInt("id_itinerario"),
                            rs.getString("nombre"),
                            rs.getDate("fecha_creacion"),
                            rs.getString("descripcion"),
                            mapEnumDurations(rs.getString("duracion")),
                            rs.getInt("id_usuario")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int mapEnumDurations(String duration) {
        return duration.equals("3") ? 3 : duration.equals("5") ? 5 : duration.equals("7") ? 7 : 0;
    }

}
