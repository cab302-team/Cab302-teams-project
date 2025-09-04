package com.example.project;

import com.example.project.Controllers.RootLayoutController;
import javafx.stage.Stage;

public interface ISceneManager
{

    public void initialise(Stage stage, RootLayoutController controller);

    public void switchScene(GameScenes type);
}
