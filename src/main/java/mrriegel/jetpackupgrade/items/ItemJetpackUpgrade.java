package mrriegel.jetpackupgrade.items;

import mrriegel.limelib.item.CommonSubtypeItem;
import net.minecraft.creativetab.CreativeTabs;

public class ItemJetpackUpgrade extends CommonSubtypeItem {
	public ItemJetpackUpgrade() {
		super("jetpack_upgrade", 3);
		this.setCreativeTab(CreativeTabs.COMBAT);
	}
}
