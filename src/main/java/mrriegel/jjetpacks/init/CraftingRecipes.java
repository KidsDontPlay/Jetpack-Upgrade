package mrriegel.jjetpacks.init;

import mcjty.rftools.blocks.endergen.EndergenicSetup;
import mcjty.rftools.blocks.generator.CoalGeneratorSetup;
import mcjty.rftools.blocks.powercell.PowerCellSetup;
import mcjty.rftools.blocks.spawner.SpawnerSetup;
import mrriegel.jjetpacks.JJetpacks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import de.ellpeck.actuallyadditions.mod.blocks.InitBlocks;
import de.ellpeck.actuallyadditions.mod.items.InitItems;

public class CraftingRecipes {

	public static void init() {
		if (JJetpacks.rftools) {
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.rftools, 1, 0), "tct", "zbz", "zez", 't', Items.GOLD_NUGGET, 'c', CoalGeneratorSetup.coalGeneratorBlock, 'z', Items.GLOWSTONE_DUST, 'b', Items.IRON_CHESTPLATE, 'e', Blocks.REDSTONE_BLOCK);
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.rftools, 1, 1), "tct", "zbz", "zez", 't', Items.GOLD_INGOT, 'c', SpawnerSetup.matterBeamerBlock, 'z', Items.BLAZE_ROD, 'b', new ItemStack(ModItems.rftools, 1, 0), 'e', PowerCellSetup.powerCellBlock);
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.rftools, 1, 2), "tct", "zbz", "zez", 't', Items.ENDER_PEARL, 'c', EndergenicSetup.endergenicBlock, 'z', mcjty.rftools.items.ModItems.dimensionalShardItem, 'b', new ItemStack(ModItems.rftools, 1, 1), 'e', PowerCellSetup.advancedPowerCellBlock);
		}
		if (JJetpacks.botania) {
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.botania, 1, 0), "abc", "ded", 'a', new ItemStack(vazkii.botania.common.item.ModItems.rune, 1, 1), 'c', new ItemStack(vazkii.botania.common.item.ModItems.rune, 1, 3), 'b', Items.IRON_CHESTPLATE, 'd', new ItemStack(vazkii.botania.common.item.ModItems.manaResource, 1, 0), 'e', new ItemStack(vazkii.botania.common.block.ModBlocks.spreader, 1, 0));
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.botania, 1, 1), "abc", "ded", 'a', new ItemStack(vazkii.botania.common.item.ModItems.rune, 1, 4), 'c', new ItemStack(vazkii.botania.common.item.ModItems.rune, 1, 6), 'b', new ItemStack(ModItems.botania, 1, 0), 'd', new ItemStack(vazkii.botania.common.item.ModItems.manaResource, 1, 7), 'e', new ItemStack(vazkii.botania.common.block.ModBlocks.spreader, 1, 2));
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.botania, 1, 2), "abc", "ded", 'a', new ItemStack(vazkii.botania.common.item.ModItems.rune, 1, 11), 'c', new ItemStack(vazkii.botania.common.item.ModItems.rune, 1, 12), 'b', new ItemStack(ModItems.botania, 1, 1), 'd', new ItemStack(vazkii.botania.common.item.ModItems.manaResource, 1, 4), 'e', new ItemStack(vazkii.botania.common.block.ModBlocks.spreader, 1, 3));
		}
		if (JJetpacks.actually) {
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.actually, 1, 0), " a ", "bcb", "ded", 'a', new ItemStack(InitItems.itemMisc, 1, 6), 'b', InitItems.itemDrillUpgradeSpeed, 'c', InitItems.itemChestQuartz, 'd', new ItemStack(InitItems.itemMisc, 1, 7), 'e', InitItems.itemBattery);
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.actually, 1, 1), " a ", "bcb", "ded", 'a', InitBlocks.blockPhantomBooster, 'b', InitItems.itemDrillUpgradeSpeedII, 'c', new ItemStack(ModItems.actually, 1, 0), 'd', new ItemStack(InitBlocks.blockMisc, 1, 6), 'e', InitItems.itemBatteryTriple);
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.actually, 1, 2), " a ", "bcb", "ded", 'a', new ItemStack(InitItems.itemMisc, 1, 19), 'b', InitItems.itemDrillUpgradeSpeedIII, 'c', new ItemStack(ModItems.actually, 1, 1), 'd', new ItemStack(InitItems.itemCrystal, 1, 4), 'e', InitItems.itemBatteryQuintuple);
		}

		if (JJetpacks.blood) {
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.blood, 1, 0), "aba", "cdc", 'a', WayofTime.bloodmagic.registry.ModItems.arcaneAshes, 'b', Items.IRON_CHESTPLATE, 'c', Items.GOLD_INGOT, 'd', WayofTime.bloodmagic.registry.ModItems.sigilAir);
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.blood, 1, 1), "aba", "cdc", 'a', new ItemStack(WayofTime.bloodmagic.registry.ModItems.itemComponent, 1, 8), 'b', new ItemStack(ModItems.blood, 1, 0), 'c', new ItemStack(WayofTime.bloodmagic.registry.ModItems.slate, 1, 2), 'd', new ItemStack(WayofTime.bloodmagic.registry.ModItems.activationCrystal, 1, 0));
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.blood, 1, 2), "aba", "cdc", 'a', WayofTime.bloodmagic.registry.ModItems.sentientArmourGem, 'b', new ItemStack(ModItems.blood, 1, 1), 'c', new ItemStack(WayofTime.bloodmagic.registry.ModItems.slate, 1, 3), 'd', new ItemStack(WayofTime.bloodmagic.registry.ModItems.activationCrystal, 1, 1));
		}
	}

}
