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
import java.sql.Timestamp;
import modelo.Usuario;

/**
 *
 * @author k0343
 */

public class ConsultasLogin {

    public static int validarLogin(String email, String password) {
        try (Connection con = Conexion.conectar()) {
            PreparedStatement ps = con.prepareStatement("SELECT contrasena, activo FROM usuarios WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return 1;
            String contrasenaBD = rs.getString("contrasena");
            boolean activo = rs.getBoolean("activo");

            if (!contrasenaBD.equals(password)) return 2;
            if (!activo) return 3;

            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Usuario obtenerUsuarioPorEmail(String email) {
        Usuario usuario = null;
        String consulta = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getString("tipo_usuario"),
                        rs.getDate("fecha_registro"),
                        rs.getString("tipo_viajero"),
                        rs.getString("idioma_preferido"),
                        rs.getString("telefono")
                );
                usuario.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public static boolean existeEmail(String email) {
        boolean existe = false;
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                existe = rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existe;
    }

    public static boolean validarCodigo(String email, String codigo) {
        try (Connection con = Conexion.conectar()) {
            PreparedStatement ps = con.prepareStatement("SELECT codigo_verificacion, expiracion_codigo FROM intentos_login WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String codigoBD = rs.getString("codigo_verificacion");
                Timestamp expiracion = rs.getTimestamp("expiracion_codigo");

                return codigo.equals(codigoBD) && expiracion != null && expiracion.after(new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean actualizarContrasena(String email, String nuevaPass) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE email = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevaPass);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void registrarCodigoVerificacion(String email, String codigo) {
        try (Connection con = Conexion.conectar()) {
            PreparedStatement psCheck = con.prepareStatement("SELECT id FROM intentos_login WHERE email = ?");
            psCheck.setString(1, email);
            ResultSet rs = psCheck.executeQuery();
            Timestamp expiracion = new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000);

            if (rs.next()) {
                PreparedStatement psUpdate = con.prepareStatement("UPDATE intentos_login SET codigo_verificacion = ?, expiracion_codigo = ? WHERE email = ?");
                psUpdate.setString(1, codigo);
                psUpdate.setTimestamp(2, expiracion);
                psUpdate.setString(3, email);
                psUpdate.executeUpdate();
            } else {
                PreparedStatement psInsert = con.prepareStatement("INSERT INTO intentos_login (email, intentos, codigo_verificacion, expiracion_codigo) VALUES (?, 0, ?, ?)");
                psInsert.setString(1, email);
                psInsert.setString(2, codigo);
                psInsert.setTimestamp(3, expiracion);
                psInsert.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registrarIntentoFallido(String email) {
        try (Connection con = Conexion.conectar()) {
            PreparedStatement psCheck = con.prepareStatement("SELECT id FROM intentos_login WHERE email = ?");
            psCheck.setString(1, email);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                PreparedStatement ps = con.prepareStatement("UPDATE intentos_login SET intentos = intentos + 1, ultimo_intento = NOW() WHERE email = ?");
                ps.setString(1, email);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = con.prepareStatement("INSERT INTO intentos_login (email, intentos, ultimo_intento) VALUES (?, 1, NOW())");
                ps.setString(1, email);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int obtenerIntentosFallidos(String email) {
        int intentos = 0;
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT intentos FROM intentos_login WHERE email = ?")) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                intentos = rs.getInt("intentos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intentos;
    }

    public static void reiniciarIntentos(String email) {
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE intentos_login SET intentos = 0 WHERE email = ?")) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}