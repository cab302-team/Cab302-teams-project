package com.example.project.services;

import javafx.scene.Parent;

import java.io.IOException;

public interface PageLoader
{
    Parent load(String fxmlPath) throws IOException;

    <T> T getController();
}
