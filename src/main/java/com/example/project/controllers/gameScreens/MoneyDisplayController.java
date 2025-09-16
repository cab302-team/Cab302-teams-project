package com.example.project.controllers.gameScreens;

import com.example.project.services.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;



/**
 * This controller is for the money displaying component that should show the player's current money amount.
 * This component should automatically update whenever the player's money changes by
 * data binding to the Session money property.
 *
 * @see Session#getMoneyProperty() for the underlying money property
 */

public class MoneyDisplayController
{
    @FXML
    private Label moneyLabel;

    /**
     * Initializes the money display component by binding the label text to the
     * Session money property. This should make it so the displayed amount of money automatically
     * updates whenever the player earns or spends their money.
     *
     * The money is displayed in the format "$X" where X is the current amount of money the player has.
     *
     * @see Session#getMoneyProperty() for the money property being bound
     */
    @FXML
    public void initialize()
    {
        // Bind the label text to the money property with currency formatting
        moneyLabel.textProperty().bind(
                Session.getMoneyProperty().asString("$%d")
        );
    }

    /**
     * Provides access to the money label for external styling or manipulation.
     *
     * @return the Label component displaying the money amount
     */
    public Label getMoneyLabel()
    {
        return moneyLabel;
    }
}

