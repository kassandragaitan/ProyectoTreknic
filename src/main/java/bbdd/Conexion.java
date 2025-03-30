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

//    public static boolean registrarUsuario(UsuarioRegistro usuarioRegistro) {
//        conectar();
//        try {
//            String consulta = "INSERT INTO usuarios(nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono) VALUES (?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement pst = conn.prepareStatement(consulta);
//            pst.setString(1, usuarioRegistro.getNombre());
//            pst.setString(2, usuarioRegistro.getEmail());
//            pst.setString(3, usuarioRegistro.getContrasena());
//            pst.setString(4, usuarioRegistro.getTipoUsuario());
//            pst.setString(5, usuarioRegistro.getIdioma());
//            pst.setString(6, usuarioRegistro.getTipoViajero());
//            pst.setString(7, usuarioRegistro.getTelefono());
//            int resultado = pst.executeUpdate();
//            return resultado > 0;
//        } catch (SQLException ex) {
//            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        } finally {
//            cerrarConexion();
//        }
//    }
    public static boolean registrarUsuario(UsuarioRegistro usuarioRegistro) {
        String consulta = "INSERT INTO usuarios(nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setString(1, usuarioRegistro.getNombre());
            pst.setString(2, usuarioRegistro.getEmail());
            pst.setString(3, usuarioRegistro.getContrasena());
            pst.setString(4, usuarioRegistro.getTipoUsuario());
            pst.setString(5, usuarioRegistro.getIdioma());
            pst.setString(6, usuarioRegistro.getTipoViajero());
            pst.setString(7, usuarioRegistro.getTelefono());
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static void cargarComboTipoUsuario(ComboBox comboTipoUsuario) {
        try {
            String consulta = "SELECT tipo_usuario FROM usuarios";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                comboTipoUsuario.getItems().add(rs.getString("tipo_usuario"));
            }
        } catch (SQLException ex) {

            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarComboTipoCompania(ComboBox comboTipoCompania) {
        try {
            String consulta = "SELECT tipo_compania FROM usuarios";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                comboTipoCompania.getItems().add(rs.getString("tipo_compania"));
            }
        } catch (SQLException ex) {

            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarComboIdioma(ComboBox comboIdioma) {
        try {
            String consulta = "SELECT idioma_preferido FROM usuarios";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                comboIdioma.getItems().add(rs.getString("idioma_preferido"));
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
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consultaCarga);

            while (rs.next()) {
                int duracion = mapEnumDurations(rs.getString("duracion"));  // Convertir ENUM a int
                listado.add(new ItinerarioTabla(
                        rs.getInt("id_itinerario"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"),
                        rs.getString("descripcion"),
                        duracion,
                        rs.getInt("id_usuario")
                ));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int mapEnumDurations(String duration) {
        switch (duration) {
            case "3":
                return 3;
            case "5":
                return 5;
            case "7":
                return 7;
            default:
                return 0;  // Valor predeterminado en caso de datos no válidos
        }
    }

}
