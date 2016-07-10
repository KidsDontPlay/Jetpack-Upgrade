package mrriegel.jjetpacks.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mcjty.rftools.blocks.ModBlocks;
import mcjty.rftools.blocks.endergen.EndergenicSetup;
import mcjty.rftools.blocks.generator.CoalGeneratorBlock;
import mcjty.rftools.blocks.generator.CoalGeneratorSetup;
import mcjty.rftools.blocks.powercell.PowerCellSetup;
import mcjty.rftools.blocks.spawner.SpawnerSetup;
import mcjty.rftools.items.DimensionalShardItem;
import mrriegel.jjetpacks.JJetpacks;

public class CraftingRecipes {

	public static void init() {
		if (JJetpacks.rftools) {
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.rftools, 1, 0), "tct", "zbz", "zez", 't', Items.GOLD_NUGGET, 'c', CoalGeneratorSetup.coalGeneratorBlock, 'z', Items.GLOWSTONE_DUST, 'b', Items.IRON_CHESTPLATE, 'e', Blocks.REDSTONE_BLOCK);
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.rftools, 1, 1), "tct", "zbz", "zez", 't', Items.GOLD_INGOT, 'c', SpawnerSetup.matterBeamerBlock, 'z', Items.BLAZE_ROD, 'b', new ItemStack(ModItems.rftools, 1, 0), 'e', PowerCellSetup.powerCellBlock);
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.rftools, 1, 2), "tct", "zbz", "zez", 't', Items.ENDER_PEARL, 'c', EndergenicSetup.endergenicBlock, 'z', mcjty.rftools.items.ModItems.dimensionalShardItem, 'b', new ItemStack(ModItems.rftools, 1, 1), 'e', PowerCellSetup.advancedPowerCellBlock);
		}
	}

}
