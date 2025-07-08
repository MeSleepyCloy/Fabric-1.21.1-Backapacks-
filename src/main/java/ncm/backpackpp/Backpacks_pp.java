package ncm.backpackpp;

import ncm.backpackpp.init.BPBlocks;
import ncm.backpackpp.init.BPBlockEntities;
import ncm.backpackpp.init.BppItems;
import ncm.backpackpp.init.ModCreativeTabBP;
import ncm.backpackpp.screen.BPScreenHandlers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Backpacks_pp implements ModInitializer {
	public static final String MOD_ID = "backpacks_pp";
	public static final Set<UUID> OPENED_BACKPACKS = new HashSet<>();
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModCreativeTabBP.registerBPItemGroups();
		BppItems.registerModItems();
		BPBlocks.registerAll();
		BPScreenHandlers.register();
		BPBlockEntities.registerBlockEntities();
	}
}