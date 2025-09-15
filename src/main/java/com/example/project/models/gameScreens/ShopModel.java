package com.example.project.models.gameScreens;

import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.shopItems.UpgradeTiles;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * Shop Model.
 */
public class ShopModel extends GameScreenModel
{
    private final ListProperty<UpgradeTile> currentInShop = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Current items in the shop row that you can buy.
     * @return shop items.
     */
    public ListProperty<UpgradeTile> currentShopItemsProperty(){
        return currentInShop;
    }

    /**
     * Constructor
     * @param session game session.
     */
    public ShopModel(Session session)
    {
        super(session);
    }

    /**
     * create new shop items.
     */
    public void regenerateShopItems()
    {
        currentInShop.clear();
        for (int i = 0; i < 3; i++)
        {
            currentInShop.add(UpgradeTiles.getTile(i));
        }
    }

    /**
     * get what upgrades the player currently has.
     * @return returns the list of upgrades.
     */
    public ReadOnlyListProperty<UpgradeTile> playersUpgradesProperty()
    {
        return Session.getUpgradeTilesProperty();
    }

    /**
     * purchase an item. remove from shop, add to players items. and decrement players money.
     * @param tileClickedOn tile to buy.
     */
    public void purchase(UpgradeTile tileClickedOn)
    {
        currentInShop.remove(tileClickedOn);
        // TODO: remove money from money UI. and add upgrade to session.
    }

    /**
     * return true if session user can purchase the tile.
     * @param tile tile to check.
     * @return returns value indicating if user can buy.
     */
    public boolean canPurchase(UpgradeTile tile)
    {
        return true; // (tile.getCost() <= session.getMoney());
    }

    /**
     * exists shop and increments level requirement for the next level.
     */
    public void onNextLevelPressed()
    {
        this.session.incrementLevelRequirement();
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);
    }
}
