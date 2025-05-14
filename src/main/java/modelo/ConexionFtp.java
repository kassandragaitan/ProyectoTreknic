/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import Utilidades.Alertas;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author k0343
 */
public class ConexionFtp {
     public static FTPClient clienteFtp;

    public static boolean conectar() {
        String servidorFtp = "ftp.reynaldomd.com";
        String user = "u812167471.kassandra";
        String pass = "2025-Kassandra";

        clienteFtp = new FTPClient(); 
        try {

            clienteFtp.connect(servidorFtp);

            clienteFtp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            clienteFtp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            clienteFtp.enterLocalPassiveMode();
            return clienteFtp.login(user, pass);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    
    public static boolean subirArchivo(File localFile, String remoteFileName) {
    try (FileInputStream fis = new FileInputStream(localFile)) {
        return clienteFtp.storeFile(remoteFileName, fis);
    } catch (IOException ex) {
        ex.printStackTrace();
        return false;
    }
}


    public static void cargarImagen(String imagen, ImageView img) {

        String urlImagen = "https://reynaldomd.com/kassandra/" + imagen;
        Image image = new Image(urlImagen, true);
        img.setImage(image);
    }

    public static void desconectar() {
        try {
            clienteFtp.logout();
        } catch (IOException ex) {  
            ex.printStackTrace();
        }
    }

    public static void eliminarArchivo(String nombreImagen) {
        conectar();
        try {
            if (!clienteFtp.deleteFile(nombreImagen)) {
            } else {
                System.out.println("Archivo eliminado con éxito: " + nombreImagen);
            }
        } catch (IOException ex) {
            Alertas.error("Error al eliminar", "Error al eliminar el archivo: " + ex.getMessage());
        } finally {
            desconectar();
        }
    }
}