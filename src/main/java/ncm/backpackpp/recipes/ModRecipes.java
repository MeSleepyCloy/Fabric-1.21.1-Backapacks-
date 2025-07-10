package ncm.backpackpp.recipes;

import ncm.backpackpp.Backpacks_pp;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<LeatherworkingRecipe> LEATHERWORKING_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(Backpacks_pp.MOD_ID, "leatherworking"),
            new LeatherworkingRecipe.Serializer());
    public static final RecipeType<LeatherworkingRecipe> LEATHERWORKING_RECIPE_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(Backpacks_pp.MOD_ID, "leatherworking"), new RecipeType<LeatherworkingRecipe>() {
                @Override
                public String toString() {
                    return "leatherworking";
                }
            });

    public static void registerRecipes() {
    }
}