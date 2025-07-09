package ncm.backpackpp.recipes.leatherwork;

import it.unimi.dsi.fastutil.chars.Char2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ncm.backpackpp.Backpacks_pp;
import ncm.backpackpp.recipes.NcmRecipe;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(staticName = "create")
public class LeatherworkRecipeBuilder implements CraftingRecipeJsonBuilder {

    private final Map<Character, Ingredient> inputs = new Char2ObjectLinkedOpenHashMap<>();
    private final List<String> pattern = new ObjectArrayList<>();
    private boolean empty = false;
    private final ItemStack outputStack;

    public static LeatherworkRecipeBuilder create(@NonNull ItemConvertible output) {
        return create(output, 1);
    }

    public static LeatherworkRecipeBuilder create(@NonNull ItemConvertible output, int outputCount) {
        return create(new ItemStack(output, outputCount).getItem());
    }

    public LeatherworkRecipeBuilder empty() {
        empty = true;
        return this;
    }

    public LeatherworkRecipeBuilder input(Character c, ItemConvertible item) {
        inputs.put(c, Ingredient.ofItems(item));
        return this;
    }

    public LeatherworkRecipeBuilder input(Character c, TagKey<Item> tag) {
        inputs.put(c, Ingredient.fromTag(tag));
        return this;
    }

    public LeatherworkRecipeBuilder pattern(String patternStr) {
        if (patternStr.length() != 3) throw new IllegalArgumentException("Empowerment Pattern must have exactly 3x3 size");
        pattern.add(patternStr);
        return this;
    }

    @Override
    public CraftingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        Backpacks_pp.LOGGER.warn("Criterion is not yet supported by Empowerment recipe type.");
        return null;
    }

    @Override
    public CraftingRecipeJsonBuilder group(@Nullable String group) {
        Backpacks_pp.LOGGER.warn("Group is not yet supported by Empowerment recipe type.");
        return null;
    }

    @Override
    public Item getOutputItem() {
        return outputStack.getItem();
    }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        if (!empty) Backpacks_pp.LOGGER.warn("{} recipe does not have any requirements", recipeId);
        if (this.pattern.size() != 3) throw new IllegalArgumentException("Empowerment Pattern must have exactly 3x3 size");
        LeatherworkRecipe.Pattern pattern = LeatherworkRecipe.Pattern.create(inputs, this.pattern);
        LeatherworkRecipe recipe = new LeatherworkRecipe();
        exporter.accept(recipeId, recipe, null);
    }
}
