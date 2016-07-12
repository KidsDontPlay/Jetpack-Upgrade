package mrriegel.jjetpacks.items;

import java.util.List;

import mrriegel.jjetpacks.CreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorUpgrade extends Item {
	public ItemArmorUpgrade() {
		super();
		this.setCreativeTab(CreativeTab.tab1);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setRegistryName("armor");
		this.setUnlocalizedName(getRegistryName().toString());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 3; i++) {
			ItemStack x = new ItemStack(item, 1, i);
			list.add(x);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}
}
