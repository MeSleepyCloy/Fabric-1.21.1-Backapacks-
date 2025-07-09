package ncm.backpackpp.recipes;

import lombok.Getter;
import ncm.backpackpp.util.BpIndentifier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public abstract class NcmRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {

    @Getter
    private final Identifier id;
    @Getter(lazy = true)
    private final RecipeType<T> recipeType = createType();

    public NcmRecipeSerializer(String id) {
        this.id = BpIndentifier.of(id);
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