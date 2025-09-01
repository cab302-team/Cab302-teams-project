/**
 * project dependencies.
 */
module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.prefs;


    opens com.example.project to javafx.fxml;
    exports com.example.project;
    exports com.example.project.sqlite;
    opens com.example.project.sqlite to javafx.fxml;
    exports com.example.project.sqlite.DAOs;
    opens com.example.project.sqlite.DAOs to javafx.fxml;
    exports com.example.project.Controllers;
    opens com.example.project.Controllers to javafx.fxml;
}