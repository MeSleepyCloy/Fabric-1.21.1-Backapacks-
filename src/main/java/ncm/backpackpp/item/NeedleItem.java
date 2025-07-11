package ncm.backpackpp.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NeedleItem extends Item {

    public NeedleItem(Settings settings) {
        super(settings);
    }

    public static boolean isNeedle(ItemStack stack) {
        return stack.getItem() instanceof NeedleItem;
    }

    public static void damageNeedle(ItemStack stack, LivingEntity user) {
    }

    public boolean isDamageable() {
        return true;
    }
}