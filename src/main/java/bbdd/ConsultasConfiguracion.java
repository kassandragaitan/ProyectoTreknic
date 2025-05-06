/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.ConfiguracionSistema;

/**
 *
 * @author k0343
 */
public class ConsultasConfiguracion {

    public static ConfiguracionSistema obtenerConfiguracion() {
        ConfiguracionSistema config = new ConfiguracionSistema();
        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM configuracion_sistema LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                config.setNombreSitio(rs.getString("nombre_sitio"));
                config.setEmailContacto(rs.getString("email_contacto"));
                config.setTelefonoSoporte(rs.getString("telefono_soporte"));
                config.setIdioma(rs.getString("idioma_predeterminado"));
                config.setDescripcion(rs.getString("descripcion_sitio"));

                config.setPoliticaContrasena(rs.getString("politica_contrasena"));
                config.setExpiracionSesionMin(rs.getInt("expiracion_sesion_min"));
                config.setIntentosFallidosMax(rs.getInt("intentos_fallidos_max"));
                config.setAutenticacionDosFactores(rs.getBoolean("autenticacion_dos_factores"));
                config.setListaIPs(rs.getString("lista_ips_blanca"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void guardarConfiguracion(ConfiguracionSistema config) {
        try {
            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE configuracion_sistema SET nombre_sitio=?, email_contacto=?, telefono_soporte=?, idioma_predeterminado=?, descripcion_sitio=?, politica_contrasena=?, expiracion_sesion_min=?, intentos_fallidos_max=?, autenticacion_dos_factores=?, lista_ips_blanca=? WHERE id_config=1"
            );
            ps.setString(1, config.getNombreSitio());
            ps.setString(2, config.getEmailContacto());
            ps.setString(3, config.getTelefonoSoporte());
            ps.setString(4, config.getIdioma());
            ps.setString(5, config.getDescripcion());
            ps.setString(6, config.getPoliticaContrasena());
            ps.setInt(7, config.getExpiracionSesionMin());
            ps.setInt(8, config.getIntentosFallidosMax());
            ps.setBoolean(9, config.isAutenticacionDosFactores());
            ps.setString(10, config.getListaIPs());

            ps.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
