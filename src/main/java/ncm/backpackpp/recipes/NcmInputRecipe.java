package ncm.backpackpp.recipes;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;

public interface NcmInputRecipe<T extends RecipeInput> extends Recipe<T> {

    @Override
    NcmRecipeSerializer<?> getSerializer();

    @Override
    default RecipeType<?> getType() {
        return getSerializer().getRecipeType();
    }
}