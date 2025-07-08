package ncm.backpackpp.init;

import ncm.backpackpp.util.BpIndentifier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public abstract class BPRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
    private final Identifier id;
    private final RecipeType<T> recipeType = createType();

    public BPRecipeSerializer(String id) {
        this.id = BpIndentifier.of(id);
    }

    public Identifier getId() {
        return id;
    }

    public RecipeType<T> getRecipeType() {
        return recipeType;
    }

    private RecipeType<T> createType() {
        return new RecipeType<>() {
            @Override
            public String toString() {
                return id.toString();
            }
        };
    }
}