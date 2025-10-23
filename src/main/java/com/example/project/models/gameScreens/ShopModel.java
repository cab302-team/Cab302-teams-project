package com.example.project.models.gameScreens;

import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.services.GameScenes;
import com.example.project.services.Logger;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.shopItems.UpgradeTiles;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * Shop Model.
 */
public class ShopModel extends GameScreenModel
{
    protected final int numberOfShopItems = 3;

    private final ListProperty<UpgradeTileModel> currentInShop = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Current items in the shop row that you can buy.
     * @return shop items.
     */
    public ListProperty<UpgradeTileModel> getCurrentShopItemsProperty(){
        return currentInShop;
    }

    /**
     * Constructor for tests.
     * @param session session.
     * @param logger logger to use.
     */
    protected ShopModel(Session session, Logger logger)
    {
        this(session);
        this.logger = logger;
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
        for (int i = 0; i < numberOfShopItems; i++)
        {
            currentInShop.add(UpgradeTiles.getRandomUpgradeTile());
        }
    }

    /**
     * This should attempt to purchase an upgrade tile from the shop.
     * Verifies the player has sufficient funds, deducts the cost, removes the item
     * from the shop, and logs the transaction.
     * @param tileClickedOn the upgrade tile the player is trying to purchase
     * @throws IllegalArgumentException if tileClickedOn is empty (null)
     * @see Session#addUpgrade(UpgradeTileModel) for adding to player's collection
     */
    public void tryPurchase(UpgradeTileModel tileClickedOn)
    {
        if (tileClickedOn == null) {
            throw new IllegalArgumentException("Cannot purchase null tile");
        }

        if (session.getMoneyProperty().get() >= tileClickedOn.getCost())
        {
            currentInShop.remove(tileClickedOn);
            session.addUpgrade(tileClickedOn);
            session.modifyMoney(tileClickedOn.getCost());

            this.logger.logMessage(String.format("Purchased %s for $%d",
                    tileClickedOn.getName(),
                    (int) tileClickedOn.getCost()));
        }
        else
        {
            // TODO: some indication that they can't afford. money sfx or red currency emphasis.
            this.logger.logMessage(String.format("Cannot afford %s (costs $%.2f, have $%f)",
                    tileClickedOn.getName(),
                    tileClickedOn.getCost(),
                    session.getMoneyProperty().get()));
        }
    }

    /**
     * exists shop and increments level requirement for the next level.
     */
    public void onNextLevelPressed()
    {
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);
    }
}
