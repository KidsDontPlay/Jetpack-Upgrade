package mrriegel.jjetpacks.helper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Lists;

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

	public static FluidStack getFluid(ItemStack s) {
		if (s == null || s.getItem() == null)
			return null;
		FluidStack a = null;
		a = FluidContainerRegistry.getFluidForFilledItem(s);
		if (a != null)
			return a;
		if (s.getItem() instanceof IFluidContainerItem)
			a = ((IFluidContainerItem) s.getItem()).getFluid(s);
		return a;
	}

}
