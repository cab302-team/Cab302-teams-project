/**
 * project dependencies.
 */
module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.slf4j;
    requires jbcrypt;
    requires javafx.media;
    requires commons.logging;
    requires java.management;

    opens com.example.project to javafx.fxml;
    exports com.example.project;
    exports com.example.project.services.sqlite;
    opens com.example.project.services.sqlite to javafx.fxml;
    exports com.example.project.services.sqlite.dAOs;
    opens com.example.project.services.sqlite.dAOs to javafx.fxml;
    exports com.example.project.controllers;
    opens com.example.project.controllers to javafx.fxml;
    exports com.example.project.models.tiles;
    opens com.example.project.models.tiles to javafx.fxml;
    exports com.example.project.controllers.gameScreens;
    opens com.example.project.controllers.gameScreens to javafx.fxml;
    exports com.example.project.controllers.tiles;
    opens com.example.project.controllers.tiles to javafx.fxml;
    exports com.example.project.controllers.popupControllers;
    opens com.example.project.controllers.popupControllers to javafx.fxml;
    exports com.example.project.models;
    opens com.example.project.models to javafx.fxml;
    exports com.example.project.services;
    opens com.example.project.services to javafx.fxml;
    exports com.example.project.models.gameScreens;
    opens com.example.project.models.gameScreens to javafx.fxml;
    exports com.example.project.models.popups;
    opens com.example.project.models.popups to javafx.fxml;
    exports com.example.project.services.shopItems;
    opens com.example.project.services.shopItems to javafx.fxml;
    exports com.example.project.controllers.gameScreens.animations;
    opens com.example.project.controllers.gameScreens.animations to javafx.fxml;
    exports com.example.project.services.sound;
    opens com.example.project.services.sound to javafx.fxml;
    exports com.example.project.models.tileGroups;
    opens com.example.project.models.tileGroups to javafx.fxml;
}