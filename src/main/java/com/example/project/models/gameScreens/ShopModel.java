package com.example.project.models.gameScreens;

import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.shopItems.UpgradeTiles;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
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
        return this.session.getUpgradeTilesProperty();
    }

    /**
     * This should attempt to purchase an upgrade tile from the shop.
     * Verifies the player has sufficient funds, deducts the cost, removes the item
     * from the shop, and logs the transaction.
     *
     * @param tileClickedOn the upgrade tile the player is trying to purchase
     * @throws IllegalArgumentException if tileClickedOn is empty (null)
     * @see #canPurchase(UpgradeTile) to check affordability before calling this method
     * @see Session#spendMoney(int) for the payment mechanism
     */
    public void purchase(UpgradeTile tileClickedOn)
    {
        if (tileClickedOn == null)
        {
            throw new IllegalArgumentException("Cannot purchase an empty tile");
        }

        if (canPurchase(tileClickedOn))
        {
            boolean success = Session.spendMoney((int) tileClickedOn.getCost());
            if (success)
            {
                currentInShop.remove(tileClickedOn);
                Session.addUpgrade(tileClickedOn);
            }
            if (success)
            {
                Session.addUpgrade(tileClickedOn);
            }
            if (success)
            {
                currentInShop.remove(tileClickedOn);
                // TODO: add upgrade to session upgrade tiles list
                this.logger.logMessage(String.format("Purchased %s for $%d",
                        tileClickedOn.getName(),
                        (int) tileClickedOn.getCost()));
            }
        }
    }

    /**
     * Determines whether the player can afford to purchase the specified upgrade tile.
     * Compares the player's current money against the tile's cost.
     *
     * @param tile the upgrade tile the player wants to buy thus check affordability for
     * @return true if the player has enough money to purchase the tile, false if the player is broke and can't
     * @throws IllegalArgumentException if tile is empty
     * @see Session#getMoney() for current player money
     * @see UpgradeTile#getCost() for tile pricing
     */
    public boolean canPurchase(UpgradeTile tile)
    {
        if (tile == null)
        {
            throw new IllegalArgumentException("Cannot check affordability of an empty tile");
        }
        return Session.getMoney() >= tile.getCost();
    }

    /**
     * This should provide access to the money property for UI data binding.
     * This allows shop UI elements to automatically update when the player's amount of
     * money changes (e.g., after purchases or if they earn money from beating levels).
     *
     * @return ReadOnlyIntegerProperty representing the player's current amount of money
     * @see Session#getMoneyProperty() for the underlying money property
     */
    public ReadOnlyIntegerProperty getMoneyProperty()
    {
        return Session.getMoneyProperty();
    }


    /**
     * exists shop and increments level requirement for the next level.
     */
    public void onNextLevelPressed()
    {
        this.session.updateLevelInfo();
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);
    }
}
