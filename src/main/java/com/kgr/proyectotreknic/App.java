package com.kgr.proyectotreknic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vistas/Login.fxml"));
        Parent root = cargador.load();
        scene = new Scene(root);
        stage.setScene(scene);

        stage.setMaximized(true);

        stage.setTitle("Treknic");
        stage.setMinWidth(900);
        stage.setMinHeight(600
        );
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}