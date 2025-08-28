/**
 * project dependencies.
 */
module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.project to javafx.fxml;
    exports com.example.project;
    exports com.example.project.sqlite;
    opens com.example.project.sqlite to javafx.fxml;
}