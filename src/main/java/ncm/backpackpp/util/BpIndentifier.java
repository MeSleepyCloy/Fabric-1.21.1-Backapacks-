package ncm.backpackpp.util;

import ncm.backpackpp.Backpacks_pp;
import net.minecraft.util.Identifier;

public class BpIndentifier {

    public static Identifier of(String path) {
        return Identifier.tryParse(Backpacks_pp.MOD_ID, path);
    }

    public static String strId(String id) {
        return Backpacks_pp.MOD_ID + ":" + id;
    }
}