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
import modelo.Usuario;

/**
 * Clase encargada de gestionar la autenticación y recuperación de cuentas
 * dentro del sistema. Incluye validación de credenciales, verificación de
 * códigos de seguridad, recuperación de contraseña y consulta de usuarios por
 * email.
 *
 * Trabaja con las tablas `usuarios` e `intentos_login`.
 *
 * @author k0343
 */
public class ConsultasLogin {

    /**
     * Valida las credenciales de un usuario mediante email y contraseña.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña introducida.
     * @return 0 si es correcto, 1 si el email no existe, 2 si la contraseña es
     * incorrecta, -1 si ocurre un error.
     */
    public static int validarLogin(String email, String password) {
        try (Connection con = Conexion.conectar()) {
            PreparedStatement ps = con.prepareStatement("SELECT contrasena FROM usuarios WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return 1;
            }

            String contrasenaBD = rs.getString("contrasena");

            if (!contrasenaBD.equals(password)) {
                return 2;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Obtiene un usuario completo a partir de su correo electrónico.
     *
     * @param email Email del usuario.
     * @return Objeto {@link Usuario} si se encuentra, null en caso contrario.
     */
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    /**
     * Verifica si un correo electrónico ya está registrado en la base de datos.
     *
     * @param email Email a verificar.
     * @return true si existe, false si no.
     */
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

    /**
     * Valida un código de verificación temporal generado para recuperación de
     * cuenta.
     *
     * @param email Email del usuario.
     * @param codigo Código introducido por el usuario.
     * @return true si el código es correcto y no ha expirado, false en caso
     * contrario.
     */
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

    /**
     * Actualiza la contraseña de un usuario en base a su email.
     *
     * @param email Email del usuario.
     * @param nuevaPass Nueva contraseña que se desea establecer.
     * @return true si se actualizó correctamente, false si ocurrió un error.
     */
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

    /**
     * Registra o actualiza un código de verificación para recuperación de
     * contraseña, con una expiración de 5 minutos.
     *
     * @param email Email del usuario.
     * @param codigo Código de verificación a guardar.
     */
    public static void registrarCodigoVerificacion(String email, String codigo) {
        try (Connection con = Conexion.conectar()) {
            Timestamp expiracion = new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000);

            PreparedStatement psCheck = con.prepareStatement("SELECT id FROM intentos_login WHERE email = ?");
            psCheck.setString(1, email);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                PreparedStatement psUpdate = con.prepareStatement(
                        "UPDATE intentos_login SET codigo_verificacion = ?, expiracion_codigo = ? WHERE email = ?");
                psUpdate.setString(1, codigo);
                psUpdate.setTimestamp(2, expiracion);
                psUpdate.setString(3, email);
                psUpdate.executeUpdate();
            } else {
                PreparedStatement psInsert = con.prepareStatement(
                        "INSERT INTO intentos_login (email, codigo_verificacion, expiracion_codigo) VALUES (?, ?, ?)");
                psInsert.setString(1, email);
                psInsert.setString(2, codigo);
                psInsert.setTimestamp(3, expiracion);
                psInsert.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
