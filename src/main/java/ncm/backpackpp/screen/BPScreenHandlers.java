package ncm.backpackpp.screen;

import lombok.experimental.UtilityClass;
import ncm.backpackpp.Backpacks_pp;
import ncm.backpackpp.screen.leatherworkTable.LeatherworkTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@UtilityClass
public class BPScreenHandlers {
    public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of("backpacks_pp", "backpack"),
                    new ScreenHandlerType<>(BackpackScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    public static final ScreenHandlerType<SmallChestScreenHandler> SMALL_CHEST_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of("backpacks_pp", "small_chest_screen_handler"),
                    new ExtendedScreenHandlerType<>(SmallChestScreenHandler::new, BlockPos.PACKET_CODEC));

    public static final ScreenHandlerType<LeatherworkTableScreenHandler> LEATHERWORK_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Backpacks_pp.MOD_ID, "leatherwork_screen_handler"),
                    new ExtendedScreenHandlerType<>(LeatherworkTableScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void register() {
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> createType(ScreenHandlerType.Factory<T> factory) {
        return new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES);
    }
}