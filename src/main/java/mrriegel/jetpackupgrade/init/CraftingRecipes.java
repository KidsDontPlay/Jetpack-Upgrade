package mrriegel.jetpackupgrade.init;

import mrriegel.limelib.helper.RecipeHelper;
import net.minecraft.item.ItemStack;

public class CraftingRecipes {

	public static void init() {
		RecipeHelper.addShapedRecipe(new ItemStack(ModItems.upgrade), "gdg", "dgd", "gdg", 'g', "ingotGold", 'd', "gemDiamond");
	}

}
