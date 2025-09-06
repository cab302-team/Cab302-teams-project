package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.GameScreenModel;
import com.example.project.models.gameScreens.ShopModel;
import com.example.project.services.Logger;
import com.example.project.services.Session;

/**
 * Shop view controller.
 */
public class ShopViewController extends GameScreenController implements ModelObserver
{
    private final ShopModel shopModel;

    /**
     * no arg constructor.
     */
    public ShopViewController()
    {
        this.shopModel = new ShopModel(Session.getInstance(), this);
    }

    /**
     * Constructor with injection for tests.
     * @param logger logger to use.
     */
    public ShopViewController(Logger logger)
    {
        super(logger);
        this.shopModel = new ShopModel(Session.getInstance(), this);
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");
    }

    @Override
    public void onModelChanged() {
        logger.logMessage("TODO shop view changes things.");
    }
}
