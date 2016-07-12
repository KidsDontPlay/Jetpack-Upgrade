package mrriegel.jjetpacks.helper;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import mrriegel.jjetpacks.items.ItemJetpackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;

public class Util {

	@Nonnull
	public static String getModNameForItem(@Nonnull Item item) {
		ModContainer m = Loader.instance().getIndexedModList().get(item.getRegistryName().getResourceDomain());
		if (m == null)
			return "Minecraft";
		else
			return m.getName();

	}

	@Nonnull
	public static String getModNameForFluid(@Nonnull Fluid fluid) {
		ModContainer m = Loader.instance().getIndexedModList().get(fluid.getBlock().getRegistryName().getResourceDomain());
		if (m == null)
			return "Minecraft";
		else
			return m.getName();

	}

	public static boolean equalOreDict(ItemStack a, ItemStack b) {
		int[] ar = OreDictionary.getOreIDs(a);
		int[] br = OreDictionary.getOreIDs(b);
		for (int i = 0; i < ar.length; i++)
			for (int j = 0; j < br.length; j++)
				if (ar[i] == br[j])
					return true;
		return false;
	}

	public static <E> boolean contains(List<E> list, E e, Comparator<? super E> c) {
		for (E a : list)
			if (c.compare(a, e) == 0)
				return true;
		return false;
	}

	public static ItemStack getJetpack(EntityPlayer player) {
		ItemStack jet = player.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
		if (jet != null && jet.getItem() instanceof ItemJetpackBase)
			return jet;
		return null;
	}

}
