package com.example.project.models.gameScreens;

import com.example.project.controllers.gameScreens.ModelObserver;
import com.example.project.controllers.tileViewControllers.UpgradeTileViewController;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.Session;
import com.example.project.services.shopItems.UpgradeTiles;

import java.util.ArrayList;
import java.util.List;

public class ShopModel extends GameScreenModel
{

    private List<UpgradeTile> currentInShop = new ArrayList<>();

    public ShopModel(Session session, ModelObserver observer)
    {
        super(session, observer);
    }

    public void regenerateShopItems()
    {
        currentInShop.clear();
        for (int i = 0; i < 3; i++)
        {
            currentInShop.add(UpgradeTiles.getTile(i));
        }
    }

    public List<UpgradeTile> getUpgradeCards()
    {
        return List.copyOf(currentInShop);
    }

    public void purchase(UpgradeTile tileClickedOn)
    {
        currentInShop.remove(tileClickedOn);
        // TODO: remove money from money UI. and add upgrade to session.
        notifyObservers();
    }

    /**
     * return true if can purchase the tile.
     * @param tile tile to chech.
     * @return returns value indicating if can buy.
     */
    public boolean canPurchase(UpgradeTile tile)
    {
        return true; // (tile.getCost() <= session.getMoney());
    }
}
