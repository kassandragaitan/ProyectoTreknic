/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author k0343
 */
public class ConfiguracionSistema {

    private String nombreSitio;
    private String emailContacto;
    private String telefonoSoporte;
    private String idioma;
    private String descripcion;

    private String politicaContrasena;
    private int expiracionSesionMin;
    private int intentosFallidosMax;
    private boolean autenticacionDosFactores;
    private String listaIPs;

    private static ConfiguracionSistema instancia;

    private String colorPrimario = "#3498db"; // Azul predeterminado
    private String urlLogo = "";
    private String urlFavicon = "";
    private String cssPersonalizado = "";

    public ConfiguracionSistema() {
    }

    public static ConfiguracionSistema getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionSistema();
        }
        return instancia;
    }

    public String getNombreSitio() {
        return nombreSitio;
    }

    public void setNombreSitio(String nombreSitio) {
        this.nombreSitio = nombreSitio;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public String getTelefonoSoporte() {
        return telefonoSoporte;
    }

    public void setTelefonoSoporte(String telefonoSoporte) {
        this.telefonoSoporte = telefonoSoporte;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPoliticaContrasena() {
        return politicaContrasena;
    }

    public void setPoliticaContrasena(String politicaContrasena) {
        this.politicaContrasena = politicaContrasena;
    }

    public int getExpiracionSesionMin() {
        return expiracionSesionMin;
    }

    public void setExpiracionSesionMin(int expiracionSesionMin) {
        this.expiracionSesionMin = expiracionSesionMin;
    }

    public int getIntentosFallidosMax() {
        return intentosFallidosMax;
    }

    public void setIntentosFallidosMax(int intentosFallidosMax) {
        this.intentosFallidosMax = intentosFallidosMax;
    }

    public boolean isAutenticacionDosFactores() {
        return autenticacionDosFactores;
    }

    public void setAutenticacionDosFactores(boolean autenticacionDosFactores) {
        this.autenticacionDosFactores = autenticacionDosFactores;
    }

    public String getListaIPs() {
        return listaIPs;
    }

    public void setListaIPs(String listaIPs) {
        this.listaIPs = listaIPs;
    }

    public String getColorPrimario() {
        return colorPrimario;
    }

    public void setColorPrimario(String colorPrimario) {
        this.colorPrimario = colorPrimario;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getUrlFavicon() {
        return urlFavicon;
    }

    public void setUrlFavicon(String urlFavicon) {
        this.urlFavicon = urlFavicon;
    }

    public String getCssPersonalizado() {
        return cssPersonalizado;
    }

    public void setCssPersonalizado(String cssPersonalizado) {
        this.cssPersonalizado = cssPersonalizado;
    }
}
