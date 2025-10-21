package com.example.project.controllers.gameScreens;

import com.example.project.models.tiles.UpgradeTileModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * sets a tooltip for desired element
 */
public class TooltipSetup {

    /**
     * different types of elements that require a tooltip
     */
    public enum Element {
        /**
         * tooltip for upgrade tiles
         */
        UPGRADE,
        /**
         * tooltip for daily rewards
         */
        DAILY,
        /**
         * tooltip for target score label
         */
        SCORETOBEAT,
        /**
         * tooltip for round score label
         */
        ROUNDSCORE,
        /**
         * tooltip for game funds label
         */
        FUNDS,
        /**
         * tooltip for username field
         */
        USERNAME,
        /**
         * tooltip for password field
         */
        PASSWORD

    }

    /**
     * @param node Node to attach tooltip to
     * @param element Element type
     * @param model Model associated with element
     */
    public void setupTooltip(Node node, Element element, Object model){
        Node content = setTooltipContent(element, model);

        // default constructor with empty string
        Tooltip tooltip = new Tooltip("");

        // Set the custom graphic content
        tooltip.setGraphic(content);

        tooltip.setPrefWidth(200);
        tooltip.setStyle("-fx-padding: 0;");
        tooltip.setShowDelay(Duration.seconds(0));

        Tooltip.install(node, tooltip);

    }

    private Node setTooltipContent(Element element, Object model) {

        VBox container = createTooltipContainer();

        return switch (element) {
            case UPGRADE -> createUpgradeGraphic(container, model);
            case DAILY -> createTooltipGraphic(container, "Spin the wheel for your daily reward!");
            case SCORETOBEAT -> createTooltipGraphic(container,"The score you must reach before the number of plays run out! Be sure not to spend all of your redraws if you want some $$$");
            case ROUNDSCORE -> createTooltipGraphic(container,"The score you get after you play a word.");
            case FUNDS -> createTooltipGraphic(container,"How much $$$ you have. You earn more based on how many redraws you have left after winning!");
            case USERNAME -> createTooltipGraphic(container,"Username must be between 3-30 characters long.");
            case PASSWORD -> createTooltipGraphic(container,"Password must be at least 8 characters.");
            default -> {
                String description = "no description";
                yield createTooltipGraphic(container, description);
            }
        };
    }

    private VBox createTooltipContainer(){
        VBox container = new VBox(3); // 5 is the spacing between labels
        container.setPadding(new Insets(5));

        container.setAlignment(Pos.TOP_CENTER);

        container.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);

        Color bgColor = Color.color(1.0, 1.0, 1.0, 0.1);
        Color borderColor = Color.WHITE;

        container.setBackground(new Background(new BackgroundFill(bgColor, new CornerRadii(6), Insets.EMPTY)));
        container.setBorder(new Border(new BorderStroke(
                borderColor,
                BorderStrokeStyle.SOLID,
                new CornerRadii(6),
                new BorderWidths(2) // 2px white border
        )));
        return container;
    }

    private Node createTooltipGraphic(VBox container, String description) {

        Label descriptionLabel = createStyledLabel(description, Color.web("#d9d8c7"), Color.BLACK);

        descriptionLabel.setWrapText(true);

        descriptionLabel.setPrefWidth(200);

        container.getChildren().addAll(descriptionLabel);
        return container;
    }

    private Node createUpgradeGraphic(VBox container, Object model) {
        if (!(model instanceof UpgradeTileModel upgradeModel)) {
            return new Label("Error: Invalid model type or missing data.");
        }

        Label nameLabel = createStyledLabel(upgradeModel.getName(), Color.web("#9f1124"), Color.WHITE);

        container.getChildren().add(nameLabel);

        createTooltipGraphic(container, upgradeModel.getDescription());

        Label costLabel = createStyledLabel( String.format("Cost: $%.2f", upgradeModel.getCost()), Color.web("#f98c3f"), Color.WHITE);

        container.getChildren().add(costLabel);

        return container;
    }

    private Label createStyledLabel(String text, Color bgColor, Color txtColor) {
        Label label = new Label(text);
        label.setTextFill(txtColor);

        label.setBackground(new Background(new BackgroundFill(bgColor, new CornerRadii(4), Insets.EMPTY)));
        label.setPadding(new Insets(2, 8, 2, 8));

        label.setStyle("-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14px;");

        return label;
    }
}
