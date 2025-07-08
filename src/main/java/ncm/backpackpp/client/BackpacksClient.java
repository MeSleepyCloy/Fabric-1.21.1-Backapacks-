package ncm.backpackpp.client;

import ncm.backpackpp.screen.BPScreenHandlers;
import ncm.backpackpp.screen.BackpackScreen;
import ncm.backpackpp.screen.SmallChestScreen;
import ncm.backpackpp.screen.leatherworkTable.LeatherworkTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class BackpacksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(BPScreenHandlers.BACKPACK_SCREEN_HANDLER, BackpackScreen::new);
        HandledScreens.register(BPScreenHandlers.SMALL_CHEST_SCREEN_HANDLER, SmallChestScreen::new);
        HandledScreens.register(BPScreenHandlers.LEATHERWORK_TABLE_SCREEN_HANDLER, LeatherworkTableScreen::new);
    }
}
