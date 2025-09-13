package com.example.project.models.gameScreens;

import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.shopItems.UpgradeTiles;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.List;

public class ShopModel extends GameScreenModel
{
    private final ListProperty<UpgradeTile> currentInShop = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ListProperty<UpgradeTile> currentShopItemsProperty(){
        return currentInShop;
    }

    public ShopModel(Session session)
    {
        super(session);
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

    public void onNextLevelPressed()
    {
        this.session.incrementLevelPoints();
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);
    }
}
