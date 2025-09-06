package com.example.project.controllers.gameStates;

import com.example.project.models.gameScreens.GameScreenModel;
import com.example.project.models.gameScreens.ShopModel;
import com.example.project.services.Logger;

/**
 * Shop view controller.
 */
public class ShopViewController extends GameScreenController
{
    private ShopModel shopModel;

    /**
     * no arg constructor.
     */
    public ShopViewController() {}

    /**
     * Constructor with injection for tests.
     * @param logger logger to use.
     */
    public ShopViewController(Logger logger)
    {
        super(logger);
    }

    @Override
    public GameScreenModel getModel()
    {
        return this.shopModel;
    }


    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");
    }
}
