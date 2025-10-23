package com.example.project.models.tileGroups;

import com.example.project.controllers.tiles.UpgradeTileController;
import com.example.project.models.tiles.UpgradeTileModel;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.layout.Pane;

import java.util.*;
import java.util.function.Consumer;

/**
 * tile group that observes an observable list and updates the ui nodes for some game tiles.
 */
public class UpgradeTileGroup extends TileGroup<UpgradeTileModel, UpgradeTileController>
{
    /**
     * Constructor.
     * @param container container all tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     * @param onClickAction additional synchronisation actions that need to be called.
     */
    public UpgradeTileGroup(Pane container, ReadOnlyListProperty<UpgradeTileModel> observedList, Consumer<UpgradeTileController> onClickAction)
    {
        super(container, onClickAction, UpgradeTileController.class, observedList);
    }

    /**
     * Constructor with no additional sync action parameter.
     * @param container container tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     */
    public UpgradeTileGroup(Pane container, ReadOnlyListProperty<UpgradeTileModel> observedList)
    {
        super(container, UpgradeTileController.class, observedList);
    }

    /**
     * Makes hashmap that tracks the count of each element in list
     * @param list the input list of elements
     * @param <T> the type of elements in the list
     * @return a map for # of dupes for each unique element
     */
    public static <T> Map<T, Integer> trackDuplicates(List<T> list){
        Map<T, Integer> countMap = new HashMap<>();

        for (T element : list) {
            countMap.compute(element, (key, count) -> (count == null) ? 1 : count + 1);
        }

        return countMap;
    }

    @Override
    protected void updateVisuals()
    {
        List<UpgradeTileModel> allModels = new ArrayList<>();
        for (var controller : tileControllers) {
            allModels.add(controller.getModel());
        }

        Map<UpgradeTileModel, Integer> countMap = trackDuplicates(allModels);

        Set<UpgradeTileModel> uniqueModels = countMap.keySet();

        Set<UpgradeTileController> usedControllers = new HashSet<>();



        container.getChildren().clear();

        for (var uniqueModel : uniqueModels)
        {
            UpgradeTileController controllerToUse = null;
            for (var controller : tileControllers) {
                if (controller.getModel().equals(uniqueModel) && !usedControllers.contains(controller)) {
                    controllerToUse = controller;
                    usedControllers.add(controller); // Mark as used
                    break;
                }
            }

            if (controllerToUse != null) {
                int count = countMap.get(uniqueModel);
                controllerToUse.updateCount(count);
                container.getChildren().add(controllerToUse.getRoot());
            }
        }
    }
}
