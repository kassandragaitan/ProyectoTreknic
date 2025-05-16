module com.kgr.proyectotreknic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires commons.email;
    requires java.mail;
    requires Mysql;
    requires commons.net;
    opens com.kgr.proyectotreknic to javafx.fxml;
    exports com.kgr.proyectotreknic;
    opens controladores to javafx.fxml;
    exports controladores;
    opens bbdd to javafx.fxml;
    exports bbdd;
     opens modelo to javafx.fxml;
    exports modelo;
    
}
