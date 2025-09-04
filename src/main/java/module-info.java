/**
 * project dependencies.
 */
module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.prefs;
    requires jdk.jshell;
    requires java.desktop;


    opens com.example.project to javafx.fxml;
    exports com.example.project;
    exports com.example.project.Models.sqlite;
    opens com.example.project.Models.sqlite to javafx.fxml;
    exports com.example.project.Models.sqlite.DAOs;
    opens com.example.project.Models.sqlite.DAOs to javafx.fxml;
    exports com.example.project.Controllers;
    opens com.example.project.Controllers to javafx.fxml;
    exports com.example.project.Models.Tiles;
    opens com.example.project.Models.Tiles to javafx.fxml;
    exports com.example.project.Controllers.gameScreens;
    opens com.example.project.Controllers.gameScreens to javafx.fxml;
    exports com.example.project.Controllers.TileViewControllers;
    opens com.example.project.Controllers.TileViewControllers to javafx.fxml;
    exports com.example.project.Models;
    opens com.example.project.Models to javafx.fxml;
}