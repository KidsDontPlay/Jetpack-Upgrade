package mrriegel.jjetpacks.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CraftingRecipes {

	public static void init() {
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.belt), "s s", "lil", 's', Items.STRING, 'l', Items.LEATHER, 'i', Items.IRON_INGOT);
	}

}
