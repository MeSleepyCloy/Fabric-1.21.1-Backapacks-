package ncm.backpackpp.recipe;

import ncm.backpackpp.init.BPRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;

public interface BPInputRecipe <T extends RecipeInput> extends Recipe<T> {

    @Override
    BPRecipeSerializer<?> getSerializer();

    @Override
    default RecipeType<?> getType() {
        return getSerializer().getRecipeType();
    }
}