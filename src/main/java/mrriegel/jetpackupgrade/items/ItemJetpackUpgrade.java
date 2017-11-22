package mrriegel.jetpackupgrade.items;

import mrriegel.limelib.item.CommonSubtypeItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemJetpackUpgrade extends CommonSubtypeItem {
	public ItemJetpackUpgrade() {
		super("jetpack_upgrade", 3);
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.values()[stack.getItemDamage()];
	}
}
