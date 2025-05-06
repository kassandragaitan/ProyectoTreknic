/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author k0343
 */
public class ConfiguracionSistema {

    private static ConfiguracionSistema instancia;
    private String colorPrimario = "#3498db";
    private String urlLogo = "";
    private String urlFavicon = "";
    private String cssPersonalizado = "";

    private final String CONFIG_FILE = "settings.properties"; // Archivo donde guardamos

    private ConfiguracionSistema() {
        cargarConfiguracion();
    }

    public static ConfiguracionSistema getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionSistema();
        }
        return instancia;
    }

    public String getColorPrimario() {
        return colorPrimario;
    }

    public void setColorPrimario(String colorPrimario) {
        this.colorPrimario = colorPrimario;
        guardarConfiguracion();
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
        guardarConfiguracion();
    }

    public String getUrlFavicon() {
        return urlFavicon;
    }

    public void setUrlFavicon(String urlFavicon) {
        this.urlFavicon = urlFavicon;
        guardarConfiguracion();
    }

    public String getCssPersonalizado() {
        return cssPersonalizado;
    }

    public void setCssPersonalizado(String cssPersonalizado) {
        this.cssPersonalizado = cssPersonalizado;
        guardarConfiguracion();
    }

    private void guardarConfiguracion() {
        try {
            Properties propiedades = new Properties();
            propiedades.setProperty("colorPrimario", colorPrimario);
            propiedades.setProperty("urlLogo", urlLogo);
            propiedades.setProperty("urlFavicon", urlFavicon);
            propiedades.setProperty("cssPersonalizado", cssPersonalizado);

            FileOutputStream salida = new FileOutputStream(CONFIG_FILE);
            propiedades.store(salida, "Configuración del sistema");
            salida.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarConfiguracion() {
        try {
            Properties propiedades = new Properties();
            FileInputStream entrada = new FileInputStream(CONFIG_FILE);
            propiedades.load(entrada);

            colorPrimario = propiedades.getProperty("colorPrimario", "#3498db");
            urlLogo = propiedades.getProperty("urlLogo", "");
            urlFavicon = propiedades.getProperty("urlFavicon", "");
            cssPersonalizado = propiedades.getProperty("cssPersonalizado", "");

            entrada.close();
        } catch (IOException e) {
            System.out.println("No se encontró configuración previa. Se usarán valores por defecto.");
        }
    }
}