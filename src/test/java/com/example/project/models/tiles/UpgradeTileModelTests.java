package com.example.project.models.tiles;

import com.example.project.models.gameScreens.LevelModel;
import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UpgradeTileModel}.
 */
@ExtendWith(MockAudioSystemExtension.class)
class UpgradeTileModelTests {

    @Test
    void builder_ShouldConstructTileCorrectly()
    {
        AtomicBoolean executed = new AtomicBoolean(false);
        Consumer<LevelModel> effect = (levelModel) -> executed.set(true);

        UpgradeTileModel tile = new UpgradeTileModel.UpgradeBuilder()
                .name("Speed Boost")
                .description("Increases speed by 20%")
                .imagePath("/path/image.png")
                .cost(3.5)
                .upgradeEffect(effect)
                .build();

        assertEquals("Speed Boost", tile.getName());
        assertEquals("Increases speed by 20%", tile.getDescription());
        assertEquals(3.5, tile.getCost());
        assertEquals("/path/image.png", tile.getAbilityImagePath());
    }

    @Test
    void getFXMLPath_ShouldReturnExpectedPath()
    {
        AtomicBoolean executed;
        executed = new AtomicBoolean(false);
        Consumer<LevelModel> dummyEffect = (model) -> executed.set(true);

        UpgradeTileModel tile = new UpgradeTileModel.UpgradeBuilder()
                .name("Dummy")
                .description("Test")
                .imagePath("/img.png")
                .cost(1.0)
                .upgradeEffect(dummyEffect)
                .build();

        assertEquals("/com/example/project/SingleTiles/upgradeTileView.fxml", tile.getFXMLPath());
    }

    @Test
    void upgradeEffect_ShouldExecuteWithoutError()
    {
        var mockLevelModel = mock(LevelModel.class);
        AtomicBoolean executed = new AtomicBoolean(false);
        Consumer<LevelModel> effect = (model) -> executed.set(true);

        UpgradeTileModel tile = new UpgradeTileModel.UpgradeBuilder()
                .name("Effect Test")
                .description("Run effect")
                .imagePath("/img.png")
                .cost(1.0)
                .upgradeEffect(effect)
                .build();

        assertDoesNotThrow(() -> tile.runUpgradeEffect(mockLevelModel));
        assertTrue(executed.get(), "Upgrade effect should have executed and set flag to true");
    }
}