package com.example.project.controllers.gameScreens;

import com.example.project.services.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;



/**
 * This controller is for the money display component that should show the player's current money amount.
 * This component should automatically update whenever the player's money changes through
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
     * Session money property. This ensures the displayed money amount automatically
     * updates whenever the player earns or spends money.
     *
     * The money is displayed in the format "$X" where X is the current amount.
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
     * This can be used by parent controllers to apply custom CSS styles or
     * perform additional configuration on the money display.
     *
     * @return the Label component displaying the money amount
     */
    public Label getMoneyLabel()
    {
        return moneyLabel;
    }
}

