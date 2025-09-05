package com.example.project.controllers.gameStates;

import com.example.project.Logger;

/**
 * Shop view controller.
 */
public class ShopViewController extends GameScreenController
{
    // TODO: All the shop stuff.

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
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");
    }
}
