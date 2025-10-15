package com.example.project.models.gameScreens;

import com.example.project.models.tiles.UpgradeTileModel;
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
    private final ListProperty<UpgradeTileModel> currentInShop = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Current items in the shop row that you can buy.
     * @return shop items.
     */
    public ListProperty<UpgradeTileModel> currentShopItemsProperty(){
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
            // TODO: randomise the three available tiles
            currentInShop.add(UpgradeTiles.getTile(i));
        }
    }

    /**
     * get what upgrades the player currently has.
     * @return returns the list of upgrades.
     */
    public ReadOnlyListProperty<UpgradeTileModel> playersUpgradesProperty()
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
     * @see #canPurchase(UpgradeTileModel) to check affordability before calling this method
     * @see Session#spendMoney(int) for the payment mechanism
     * @see Session#addUpgrade(UpgradeTileModel) for adding to player's collection
     */

    public void purchase(UpgradeTileModel tileClickedOn)
    {
        if (tileClickedOn == null) {
            throw new IllegalArgumentException("Cannot purchase null tile");
        }

        if (canPurchase(tileClickedOn)) {
            boolean success = session.spendMoney((int) tileClickedOn.getCost());
            if (success) {
                currentInShop.remove(tileClickedOn);
                session.addUpgrade(tileClickedOn);
                this.logger.logMessage(String.format("Purchased %s for $%d",
                        tileClickedOn.getName(),
                        (int) tileClickedOn.getCost()));
            }
        } else {
            this.logger.logMessage(String.format("Cannot afford %s (costs $%.2f, have $%d)",
                    tileClickedOn.getName(),
                    tileClickedOn.getCost(),
                    session.getMoneyProperty().get()));
        }
    }

    /**
     * Determines whether the player can afford to purchase the specified upgrade tile.
     * Compares the player's current money against the tile's cost.
     *
     * @param tile the upgrade tile the player wants to buy thus check affordability for
     * @return true if the player has enough money to purchase the tile, false if the player is broke and can't
     * @throws IllegalArgumentException if tile is empty
     * @see UpgradeTileModel#getCost() for tile pricing
     */
    public boolean canPurchase(UpgradeTileModel tile)
    {
        if (tile == null)
        {
            throw new IllegalArgumentException("Cannot check affordability of an empty tile");
        }
        return session.getMoneyProperty().get() >= tile.getCost();
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
