package com.example.project.controllers;

import com.example.project.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * wrapper class for dev buttons to toggle off or on when we want.
 */
public class DevButton extends Button
{
    /**
     * Constructor.
     */
    public DevButton() {
        super();
        this.setVisible(Application.DEV_BUTTONS_ON);
        // this.setManaged(Application.DEV_BUTTONS_ON);
    }
}
